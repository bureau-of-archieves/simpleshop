package simpleshop.service;


import simpleshop.domain.model.User;

import java.util.List;

public interface UserService {

    User findUser(String username);

    User saveUser(User user);

    List<User> findAllUsers();
}
