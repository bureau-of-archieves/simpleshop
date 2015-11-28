package simpleshop.webapp.infrastructure;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityContextServiceImpl implements SecurityContextService {

    @Override
    public SecurityContext get() {
        return SecurityContextHolder.getContext();
    }
}
