package simpleshop.data.infrastructure;

import simpleshop.common.DeProxyStrategy;
import simpleshop.data.util.DomainUtils;

/**
 * Strategy for handling Hibernate proxies.
 */
public class HibernateDeProxyStrategy implements DeProxyStrategy {
    @Override
    public Class<?> getProxiedClass(Object proxy) {
        return DomainUtils.getProxiedClass(proxy);
    }
}
