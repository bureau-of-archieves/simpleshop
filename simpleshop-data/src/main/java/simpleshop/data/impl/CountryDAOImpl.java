package simpleshop.data.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import simpleshop.common.StringUtils;
import simpleshop.data.CountryDAO;
import simpleshop.data.PageInfo;
import simpleshop.data.infrastructure.impl.ModelDAOImpl;
import simpleshop.domain.model.Country;

import java.io.Serializable;
import java.util.List;


@Repository
public class CountryDAOImpl extends ModelDAOImpl<Country> implements CountryDAO {

    @Override
    public Country load(Serializable id) {
        return super.load(Country.class, id);
    }

    @Override
    public Country get(Serializable id) {
        return super.get(Country.class, id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Country> quickSearch(String keywords, PageInfo pageInfo){
        keywords = StringUtils.wrapLikeKeywords(keywords);
        Query query = super.createQuery("SELECT co FROM Country co WHERE co.name LIKE ?1 or co.countryCode LIKE ?1", pageInfo, keywords);
        return query.list();
    }
}
