package zhy2002.sponge;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.hibernate.bytecode.internal.javassist.FieldHandled;
import simpleshop.domain.metadata.Icon;

import java.io.File;
import java.nio.file.Paths;

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

    public void execute() throws MojoExecutionException
    {
        getLog().info( "Generating webapp source code for project " + projectName + "..." );
        basePackage = projectName.toLowerCase();
        buildDirectory = Paths.get(buildDirectory, "classes").toString();
        String domainBuildDirectory = buildDirectory.replace("-webapp" + File.separator, "-domain" + File.separator);
        ClassLoader dirClassLoader = new DirClassLoader(this.getClass().getClassLoader(), domainBuildDirectory);
        String domainClassLocation = Paths.get(domainBuildDirectory, basePackage, "domain", "model").toString();
        domainClassPackage = basePackage + ".domain.model.";
        File dir = new File(domainClassLocation);
        File[] files = dir.listFiles();
        if(files == null)
            return;
        getLog().info("Generating controller classes for domain classes in " + domainClassLocation);
        for(File file : files){
            generateBaseController(file, dirClassLoader);
        }
    }

    private void generateBaseController(File file, ClassLoader dirClassLoader) {
        if(!file.getName().endsWith(".class"))
            return;

        String className = file.getName().substring(0, file.getName().length() - 6);
        try {
            String fullClassName = domainClassPackage + className;
            getLog().debug("Loading class: " + fullClassName);
            Class<?> domainClass = dirClassLoader.loadClass(fullClassName);
            String modelName = domainClass.getSimpleName();
            if(domainClass.getAnnotation(Icon.class) != null){
                getLog().info(modelName + "BaseController is created.");
            }

        }catch (ClassNotFoundException ex){
            getLog().warn(ex.toString());
        }
    }
}
