package simpleshop.webapp.infrastructure;

import org.springframework.security.core.context.SecurityContext;
import simpleshop.domain.model.User;

/**
 * Get Spring security context.
 */
public interface SecurityContextService {

    SecurityContext get();

    User getUser();
}
