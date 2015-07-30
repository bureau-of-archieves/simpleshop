package zhy2002.sponge;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Generate service layer source.
 */
@Mojo( name = "service")
public class GenerateServiceMojo extends AbstractMojo {

    public void execute() throws MojoExecutionException
    {
        getLog().info( "Generating service layer source code..." );
    }
}