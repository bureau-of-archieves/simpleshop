package simpleshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import simpleshop.Constants;
import simpleshop.data.CountryDAO;
import simpleshop.data.PageInfo;
import simpleshop.data.SuburbDAO;
import simpleshop.data.infrastructure.ModelDAO;
import simpleshop.domain.model.Country;
import simpleshop.domain.model.Suburb;
import simpleshop.dto.SuburbSearch;
import simpleshop.service.SuburbService;
import simpleshop.service.infrastructure.impl.ModelServiceImpl;

import java.util.List;

@Service
public class SuburbServiceImpl extends ModelServiceImpl<Suburb, SuburbSearch> implements SuburbService {

    @Autowired
    private SuburbDAO suburbDAO;

    @Override
    protected ModelDAO<Suburb> getModelDAO() {
        return suburbDAO;
    }

    @Override
    public Suburb create() {
        //[sponge]defaulting - model object default values are preferably set in the create service function.
        Suburb suburb = new Suburb();
        suburb.setCountry(new Country("AUS")); //todo externalise to a property file
        return suburb;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Suburb> quickSearch(String keywords, PageInfo pageInfo) {
        return suburbDAO.quickSearch(keywords, pageInfo);
    }

    @Autowired
    private CountryDAO countryDAO;

    @Transactional(readOnly = true)
    @Override
    public List<Country> getCountries() {
        return countryDAO.quickSearch("", new PageInfo(0, Constants.MAX_DROPDOWN_LIST_SIZE));
    }
}
