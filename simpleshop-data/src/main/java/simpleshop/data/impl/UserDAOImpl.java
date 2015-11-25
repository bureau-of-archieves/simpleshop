package simpleshop.data.impl;


import org.springframework.stereotype.Repository;
import simpleshop.common.StringUtils;
import simpleshop.data.PageInfo;
import simpleshop.data.UserDAO;
import simpleshop.data.infrastructure.impl.ModelDAOImpl;
import simpleshop.domain.model.User;

import java.util.List;

@Repository
public class UserDAOImpl extends ModelDAOImpl<User> implements UserDAO {
    @SuppressWarnings("unchecked")
    @Override
    public List<User> quickSearch(String keywords, PageInfo pageInfo){
        keywords = StringUtils.wrapLikeKeywords(keywords);
        return super.getList("SELECT usr FROM User usr WHERE usr.username LIKE ?1", pageInfo, keywords);
    }
}