package simpleshop.service.infrastructure;

/**
 * Wrapper exception class and implements getLocalizedMessage.
 */
public class SpongeRuntimeException extends RuntimeException {

    public SpongeRuntimeException(String msg){
        super(msg);
    }

    public SpongeRuntimeException(Throwable ex){
        super(ex);
    }

    public SpongeRuntimeException(String msg, Throwable ex){
        super(msg, ex);
    }

    @Override
    public String getLocalizedMessage() {
        //todo implement
        return super.getLocalizedMessage();
    }
}
