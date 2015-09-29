package simpleshop.data.impl;

import org.springframework.stereotype.Repository;
import simpleshop.common.StringUtils;
import simpleshop.data.CountryDAO;
import simpleshop.data.PageInfo;
import simpleshop.data.infrastructure.impl.ModelDAOImpl;
import simpleshop.domain.model.Country;

import java.util.List;


@Repository
public class CountryDAOImpl extends ModelDAOImpl<Country> implements CountryDAO {

    @SuppressWarnings("unchecked")
    @Override
    public List<Country> quickSearch(String keywords, PageInfo pageInfo){
        keywords = StringUtils.wrapLikeKeywords(keywords);
        return super.getList("SELECT co FROM Country co WHERE co.name LIKE ?1 or co.countryCode LIKE ?1", pageInfo, keywords);
    }
}
