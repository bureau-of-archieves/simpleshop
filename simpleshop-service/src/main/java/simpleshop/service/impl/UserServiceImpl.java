package simpleshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import simpleshop.data.PageInfo;
import simpleshop.data.UserDAO;
import simpleshop.domain.model.User;
import simpleshop.service.UserService;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Transactional(readOnly = true)
    @Override
    public User findUser(String username) {
        User user = userDAO.get(username);
        if(user != null){ //load collections
            user.getAuthorities().iterator().hasNext();
            user.getPermissions().entrySet().iterator().hasNext();
        }
        return user;
    }

    @Transactional
    @Override
    public User saveUser(User user) {
        userDAO.save(user);
        return user;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAllUsers() {
        return userDAO.quickSearch("", new PageInfo(0, Integer.MAX_VALUE));
    }
}
