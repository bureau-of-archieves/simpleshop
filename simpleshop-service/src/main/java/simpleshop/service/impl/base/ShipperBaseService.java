

package simpleshop.service.impl.base;

import org.springframework.beans.factory.annotation.Autowired;
import simpleshop.data.ShipperDAO;
import simpleshop.data.infrastructure.ModelDAO;
import simpleshop.domain.model.Shipper;
import simpleshop.dto.ShipperSearch;
import simpleshop.service.ShipperService;
import simpleshop.service.infrastructure.impl.ModelServiceImpl;

public abstract class ShipperBaseService extends ModelServiceImpl<Shipper, ShipperSearch> implements ShipperService {

    @Autowired
    protected ShipperDAO shipperDAO;

    @Override
    protected ModelDAO<Shipper> getModelDAO() {
        return shipperDAO;
    }

    @Override
    public Shipper create() {
        return new Shipper();
    }

}
