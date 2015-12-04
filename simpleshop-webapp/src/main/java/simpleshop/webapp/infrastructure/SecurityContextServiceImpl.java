package simpleshop.webapp.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import simpleshop.domain.model.User;
import simpleshop.service.UserService;

@Service
public class SecurityContextServiceImpl implements SecurityContextService {

    @Autowired
    private UserService userService;

    @Override
    public SecurityContext get() {
        return SecurityContextHolder.getContext();
    }

    @Transactional
    @Override
    public User getUser() {

        SecurityContext securityContext = get();
        if(securityContext == null)
            return null;

        Authentication authentication = securityContext.getAuthentication();
        if(authentication == null)
            return null;

        String username = authentication.getName();
        User user = userService.findUser(username);
        if(user == null)
            return null;

        return user;
    }
}
