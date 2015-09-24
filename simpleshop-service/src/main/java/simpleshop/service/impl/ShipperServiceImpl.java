package simpleshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simpleshop.data.ShipperDAO;
import simpleshop.data.infrastructure.ModelDAO;
import simpleshop.domain.model.Shipper;
import simpleshop.dto.ShipperSearch;
import simpleshop.service.ShipperService;

@Service
public class ShipperServiceImpl extends ContactServiceImpl<Shipper, ShipperSearch> implements ShipperService {

    @Autowired
    private ShipperDAO shipperDAO;

    @Override
    protected ModelDAO<Shipper> getModelDAO() {
        return shipperDAO;
    }

    @Override
    public Shipper create() {
        return new Shipper();
    }

}
