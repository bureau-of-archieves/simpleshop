package simpleshop.data.impl;

import org.springframework.stereotype.Repository;
import simpleshop.data.ExchangeRateDAO;
import simpleshop.data.infrastructure.impl.ModelDAOImpl;
import simpleshop.domain.model.ExchangeRate;


@Repository
public class ExchangeRateDAOImpl extends ModelDAOImpl<ExchangeRate> implements ExchangeRateDAO {

}
