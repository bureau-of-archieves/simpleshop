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
import simpleshop.service.impl.base.SuburbBaseService;
import simpleshop.service.infrastructure.impl.ModelServiceImpl;

import java.util.List;

@Service
public class SuburbServiceImpl extends SuburbBaseService implements SuburbService {

    @Override
    public Suburb create() {
        //[sponge]defaulting - model object default values are preferably set in the create service function.
        Suburb suburb = super.create();
        suburb.setCountry(new Country("AUS")); //todo externalise to a property file
        return suburb;
    }

    @Autowired
    private CountryDAO countryDAO;

    @Transactional(readOnly = true)
    @Override
    public List<Country> getCountries() {
        return countryDAO.quickSearch("", new PageInfo(0, Constants.MAX_DROPDOWN_LIST_SIZE));
    }
}
