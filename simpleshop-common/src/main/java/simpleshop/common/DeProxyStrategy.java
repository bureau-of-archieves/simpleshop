package simpleshop.common;

public interface DeProxyStrategy {

    /**
     * Get the class of the proxy's target.
     * @param target the proxy.
     * @return A class which the proxy can act as an instance of.
     */
    Class<?> getProxiedClass(Object proxy);

}
