

package simpleshop.service.impl.base;

import org.springframework.beans.factory.annotation.Autowired;
import simpleshop.data.SupplierDAO;
import simpleshop.data.infrastructure.ModelDAO;
import simpleshop.domain.model.Supplier;
import simpleshop.dto.SupplierSearch;
import simpleshop.service.SupplierService;
import simpleshop.service.infrastructure.impl.ModelServiceImpl;

public abstract class SupplierBaseService extends ModelServiceImpl<Supplier, SupplierSearch> implements SupplierService {

    @Autowired
    protected SupplierDAO supplierDAO;

    @Override
    protected ModelDAO<Supplier> getModelDAO() {
        return supplierDAO;
    }

    @Override
    public Supplier create() {
        return new Supplier();
    }

}
