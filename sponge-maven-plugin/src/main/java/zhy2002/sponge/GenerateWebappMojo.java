package zhy2002.sponge;

import org.apache.maven.plugins.annotations.Mojo;
import simpleshop.domain.metadata.Icon;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Sponge code generation plugin.
 */
@Mojo( name = "webapp")
public class GenerateWebappMojo extends BaseMojo {

    public GenerateWebappMojo(){
        super("webapp");
    }

    @Override
    protected void generateCode(){
        getLog().info("Generating base controllers for project " + projectName + "...");

        File[] domainClassFiles = MojoUtils.getFilesInDirectory(domainClassLocation);
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

    private void generateBaseController(File domainClassFile, String outputDir, ClassLoader dirClassLoader) {

        if(!domainClassFile.getName().endsWith(".class"))
            return;

        //load the resource
        String templateFileName = "BaseController.java";
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
