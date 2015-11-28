package zhy2002.sponge;

import org.apache.maven.plugins.annotations.Mojo;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Generate service layer source.
 */
@Mojo( name = "service")
public class GenerateServiceMojo extends BaseMojo {

    public GenerateServiceMojo(){
        super("service", new String[]{"service", "impl", "base"});
    }

    @Override
    protected void generateCode(){
        getLog().info("Generating base services for project " + projectName + "...");
        getLog().debug("Domain classes are found under: " + domainClassLocation);

        generateMetadataBaseService();
        generateModelBaseServices();
    }

    private void generateMetadataBaseService() {

        //load the resource
        String templateFileName = "MetadataBaseService.java";
        String template = getClassPathResource(templateFileName);

        //generate file
        getLog().debug("Generating MetadataBaseService in: " + outputDirPath);
        List<File> domainClassFiles = MojoUtils.getFilesUnderDirectory(domainClassLocation, ".class", new String[]{"type"});
        ArrayList<String> modelNames = new ArrayList<>();
        domainClassFiles.forEach(f -> modelNames.add(f.getName()));

        List<File> dtoClassFiles = MojoUtils.getFilesUnderDirectory(dtoClassLocation, ".class", null);
        dtoClassFiles.forEach(f -> modelNames.add(f.getName()));

        String result = rythmEngine.render(template, projectName, modelNames);
        saveTo(Paths.get(outputDirPath.toString(), templateFileName).toString(), result);
        getLog().info(templateFileName + " is created.");
    }

    private void generateModelBaseServices(){
        getLog().debug("Generating base services for project " + projectName + "...");

        File[] domainClassFiles = MojoUtils.getFilesInDirectory(domainClassLocation);
        if(domainClassFiles == null || domainClassFiles.length == 0){
            getLog().info("No domain class found in: " + domainClassLocation);
            return;
        }

        getLog().debug("Generating base service classes for domain classes in: " + domainClassLocation);
        getLog().debug("Generating source files in: " + outputDirPath);
        for(File file : domainClassFiles){
            generateModelFile(file, "BaseService.java", outputDirPath.toString(), dirClassLoader);
            generateModelFile(file, "ServiceImpl.java", outputDirPath.getParent().toString(), dirClassLoader, false);
            generateModelFile(file, "Service.java", outputDirPath.getParent().getParent().toString(), dirClassLoader, false);
        }
    }


}