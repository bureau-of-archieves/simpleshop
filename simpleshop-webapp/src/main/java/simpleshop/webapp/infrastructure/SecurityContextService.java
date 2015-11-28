package simpleshop.webapp.infrastructure;

import org.springframework.security.core.context.SecurityContext;

/**
 * Get Spring security context.
 */
public interface SecurityContextService {

    SecurityContext get();
}
