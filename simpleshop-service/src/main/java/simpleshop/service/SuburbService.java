package simpleshop.service;

import simpleshop.domain.model.Country;
import simpleshop.domain.model.Suburb;
import simpleshop.dto.SuburbSearch;
import simpleshop.service.infrastructure.ModelService;

import java.util.List;


public interface SuburbService extends ModelService<Suburb, SuburbSearch> {

    List<Country> getCountries();
}
