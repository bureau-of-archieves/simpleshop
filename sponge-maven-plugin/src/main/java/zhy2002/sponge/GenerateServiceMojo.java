package zhy2002.sponge;

import org.apache.maven.plugins.annotations.Mojo;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Generate service layer source.
 */
@Mojo( name = "service")
public class GenerateServiceMojo extends BaseMojo {

    public GenerateServiceMojo(){
        super("service");
    }

    @Override
    protected void generateCode(){
        getLog().info("Generating base services for project " + projectName + "...");
        getLog().debug("Domain classes are found under: " + domainClassLocation);

        generateBaseMetadataService();
    }

    private void generateBaseMetadataService() {

        //ensure dir exists
        Path outputDirPath = Paths.get(buildSourceDirectory, basePackage, "service", "impl", "base");
        outputDirPath.toFile().mkdirs();

        //load the resource
        String templateFileName = "BaseMetadataService.java";
        String template = getClassPathResource(templateFileName);

        //generate file
        getLog().debug("Generating BaseMetadataService in: " + outputDirPath);
        List<File> domainClassFiles = MojoUtils.getFilesUnderDirectory(domainClassLocation, ".class", new String[]{"type"});
        ArrayList<String> modelNames = new ArrayList<>();
        domainClassFiles.forEach(f -> modelNames.add(f.getName()));

        List<File> dtoClassFiles = MojoUtils.getFilesUnderDirectory(dtoClassLocation, "Search.class", null);
        dtoClassFiles.forEach(f -> modelNames.add(f.getName()));

        String result = rythmEngine.render(template, modelNames);
        saveTo(Paths.get(outputDirPath.toString(), templateFileName).toString(), result);
        getLog().info(templateFileName + " is created.");
    }


}