package simpleshop.data.infrastructure;

/**
 * Thrown when there is a configuration error in the Sponge system.
 */
public class SpongeConfigurationException extends RuntimeException {

    public SpongeConfigurationException(String msg){
        super(msg);
    }

    public SpongeConfigurationException(String msg, Throwable ex){
        super(msg, ex);
    }

}
