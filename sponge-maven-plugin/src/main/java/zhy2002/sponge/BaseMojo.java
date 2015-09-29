package zhy2002.sponge;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.rythmengine.RythmEngine;
import simpleshop.common.CollectionUtils;
import simpleshop.domain.metadata.Icon;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Base class for all mojos.
 */
public abstract class BaseMojo extends AbstractMojo {

    protected BaseMojo(String moduleSuffix, String[] outputDirParts){
        this.moduleSuffix = moduleSuffix;
        this.outputDirParts = outputDirParts;
    }

    @Parameter(property = "projectName", required = true)
    protected String projectName;

    @Parameter(defaultValue = "${project.build.sourceDirectory}")
    protected String buildSourceDirectory;

    @Parameter(defaultValue = "${project.build.directory}")
    protected String buildDirectory;

    private String moduleSuffix;
    private String[] outputDirParts;
    protected String basePackage;
    protected String domainClassPackage;
    protected RythmEngine rythmEngine;
    protected String domainClassLocation;
    protected String dtoClassLocation;
    protected ClassLoader dirClassLoader;
    protected Path outputDirPath;

    protected void init(){
        basePackage = projectName.toLowerCase();
        buildDirectory = Paths.get(buildDirectory, "classes").toString();
        String domainBuildDirectory = buildDirectory.replace("-" + moduleSuffix + File.separator, "-domain" + File.separator);
        String dtoBuildDirectory = buildDirectory.replace("-" + moduleSuffix + File.separator, "-dto" + File.separator);
        dirClassLoader = new DirClassLoader(this.getClass().getClassLoader(), domainBuildDirectory);
        domainClassLocation = Paths.get(domainBuildDirectory, basePackage, "domain", "model").toString();
        dtoClassLocation = Paths.get(dtoBuildDirectory, basePackage, "dto").toString();
        domainClassPackage = basePackage + ".domain.model.";
        Map<String, Object> config = new HashMap<>();
        config.put("codegen.compact", false);
        rythmEngine = new RythmEngine(config);
        outputDirPath = Paths.get(buildSourceDirectory, CollectionUtils.concat(new String[]{basePackage}, outputDirParts));
        outputDirPath.toFile().mkdirs(); //ensure dir exists
    }

    protected abstract void generateCode();

    public void execute() throws MojoExecutionException
    {
        init();
        generateCode();
    }

    /**
     * Save the content as a file.
     * @param fileName location of the file.
     * @param content content of the file.
     */
    protected void saveTo(String fileName, String content){

        try(FileWriter fileWriter = new FileWriter(fileName)){
            fileWriter.write(content);
        }catch (IOException ex){
            getLog().error(ex);
        }
    }

    protected String getClassPathResource(String templateFileName) {
        String template;
        try {
            template = MojoUtils.readContent(this.getClass(), templateFileName);
        } catch (IOException ex){
            throw new RuntimeException(ex);
        }
        return template;
    }

    protected void generateModelFile(File domainClassFile,String templateFileName, String outputDir, ClassLoader dirClassLoader) {
        if(!domainClassFile.getName().endsWith(".class"))
            return;

        //load the resource
        String template = getClassPathResource(templateFileName);

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

}
