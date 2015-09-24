package zhy2002.sponge;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.rythmengine.RythmEngine;
import simpleshop.domain.metadata.Icon;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Sponge code generation plugin.
 */
@Mojo( name = "webapp")
public class GenerateWebappMojo extends AbstractMojo {

    @Parameter(property = "webapp.projectName", required = true)
    private String projectName;

    @Parameter(defaultValue = "${project.build.sourceDirectory}")
    private String buildSourceDirectory;

    @Parameter(defaultValue = "${project.build.directory}")
    private String buildDirectory;

    private String basePackage;
    private String domainClassPackage;
    private RythmEngine rythmEngine;
    private String domainClassLocation;
    ClassLoader dirClassLoader;

    private void init(){
        basePackage = projectName.toLowerCase();
        buildDirectory = Paths.get(buildDirectory, "classes").toString();
        String domainBuildDirectory = buildDirectory.replace("-webapp" + File.separator, "-domain" + File.separator);
        dirClassLoader = new DirClassLoader(this.getClass().getClassLoader(), domainBuildDirectory);
        domainClassLocation = Paths.get(domainBuildDirectory, basePackage, "domain", "model").toString();
        domainClassPackage = basePackage + ".domain.model.";
        Map<String, Object> config = new HashMap<>();
        config.put("codegen.compact", false);
        rythmEngine = new RythmEngine(config);
    }

    private static File[] getFilesInDirectory(String dir){
        File directory = new File(dir);
        return directory.listFiles();
    }

    private void generateBaseControllers(){
        getLog().info("Generating base controllers for project " + projectName + "...");

        File[] domainClassFiles = getFilesInDirectory(domainClassLocation);
        if(domainClassFiles == null || domainClassFiles.length == 0){
            getLog().info("No domain class found in: " + domainClassLocation);
            return;
        }

        getLog().debug("Generating base controller classes for domain classes in: " + domainClassLocation);
        Path outputDirPath = Paths.get(buildSourceDirectory, basePackage, "webapp", "mvc", "controller", "base");
        outputDirPath.toFile().mkdirs();

        getLog().debug("Generating source files in: " + outputDirPath);
        for(File file : domainClassFiles){
            generateBaseController(file, outputDirPath.toString(), dirClassLoader);
        }
    }

    public void execute() throws MojoExecutionException
    {
        init();
        generateBaseControllers();
    }

    private void generateBaseController(File domainClassFile, String outputDir, ClassLoader dirClassLoader) {

        if(!domainClassFile.getName().endsWith(".class"))
            return;

        //load the resource
        String templateFileName = "BaseController.java";
        String template;
        try {
            template = readContent(templateFileName);
        } catch (IOException ex){
            throw new RuntimeException(ex);
        }

        //generate file
        String className = domainClassFile.getName().substring(0, domainClassFile.getName().length() - 6);
        String fullClassName = domainClassPackage + className;

        try {
            getLog().debug("Loading class: " + fullClassName);
            Class<?> domainClass = dirClassLoader.loadClass(fullClassName);
            if(domainClass.getAnnotation(Icon.class) == null){
                return; //bypass the ones without icon.
            }

            String modelName = domainClass.getSimpleName();
            String result = rythmEngine.render(template, projectName, modelName);

            String fileName = modelName + templateFileName;
            saveTo(Paths.get(outputDir, fileName).toString(), result);
            getLog().info(fileName + " is created.");

        }catch (ClassNotFoundException ex){
            getLog().warn(ex.toString());
        }
    }

    /**
     * Read the content of a class path resource file.
     * @param templateFileName absolute path to the file.
     * @return the content.
     * @throws IOException
     */
    private String readContent(String templateFileName) throws IOException{

        InputStream inputStream = null;
        BufferedReader reader = null;

        try {
            inputStream = this.getClass().getClassLoader().getResourceAsStream(templateFileName);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder out = new StringBuilder();
            String newLine = System.getProperty("line.separator");
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
                out.append(newLine);
            }
            return out.toString();
        }finally {
            if(reader != null){
                reader.close();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
    }

    /**
     * Save the content as a file.
     * @param fileName location of the file.
     * @param content content of the file.
     */
    private void saveTo(String fileName, String content){

        try(FileWriter fileWriter = new FileWriter(fileName)){
           fileWriter.write(content);
        }catch (IOException ex){
            getLog().error(ex);
        }
    }
}
