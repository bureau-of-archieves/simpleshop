package zhy2002.sponge;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.rythmengine.RythmEngine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Base class for all mojos.
 */
public abstract class BaseMojo extends AbstractMojo {

    protected BaseMojo(String moduleSuffix){
       this.moduleSuffix = moduleSuffix;
    }

    @Parameter(property = "projectName", required = true)
    protected String projectName;

    @Parameter(defaultValue = "${project.build.sourceDirectory}")
    protected String buildSourceDirectory;

    @Parameter(defaultValue = "${project.build.directory}")
    protected String buildDirectory;

    private String moduleSuffix;
    protected String basePackage;
    protected String domainClassPackage;
    protected RythmEngine rythmEngine;
    protected String domainClassLocation;
    protected String dtoClassLocation;
    protected ClassLoader dirClassLoader;

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

}
