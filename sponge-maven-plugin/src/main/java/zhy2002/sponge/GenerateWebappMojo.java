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
        super("webapp", new String[]{"webapp", "mvc", "controller", "base"});
    }

    @Override
    protected void generateCode(){
        getLog().debug("Generating base controllers for project " + projectName + "...");

        File[] domainClassFiles = MojoUtils.getFilesInDirectory(domainClassLocation);
        if(domainClassFiles == null || domainClassFiles.length == 0){
            getLog().info("No domain class found in: " + domainClassLocation);
            return;
        }

        getLog().debug("Generating base controller classes for domain classes in: " + domainClassLocation);
        getLog().debug("Generating source files in: " + outputDirPath);
        for(File file : domainClassFiles){
            generateModelFile(file, "BaseController.java", outputDirPath.toString(), dirClassLoader);
        }
    }

}
