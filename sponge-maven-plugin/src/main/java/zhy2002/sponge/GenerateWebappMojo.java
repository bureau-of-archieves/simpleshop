package zhy2002.sponge;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Sponge code generation plugin.
 */
@Mojo( name = "webapp")
public class GenerateWebappMojo extends AbstractMojo {


    public void execute() throws MojoExecutionException
    {
        getLog().info( "Generating webapp source code..." );


    }
}
