package simpleshop.data.impl;

import org.springframework.stereotype.Repository;
import simpleshop.data.ExchangeRateDAO;
import simpleshop.data.infrastructure.impl.ModelDAOImpl;
import simpleshop.domain.model.ExchangeRate;

import java.io.Serializable;


@Repository
public class ExchangeRateDAOImpl extends ModelDAOImpl<ExchangeRate> implements ExchangeRateDAO {

    @Override
    public ExchangeRate load(Serializable id) {
        return super.load(ExchangeRate.class, id);
    }

    @Override
    public ExchangeRate get(Serializable id) {
        return super.get(ExchangeRate.class, id);
    }

}
