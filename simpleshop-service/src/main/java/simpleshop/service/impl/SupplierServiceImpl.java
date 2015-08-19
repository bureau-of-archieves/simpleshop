package simpleshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simpleshop.data.SupplierDAO;
import simpleshop.data.infrastructure.ModelDAO;
import simpleshop.domain.model.Supplier;
import simpleshop.dto.SupplierSearch;
import simpleshop.service.SupplierService;
import simpleshop.service.infrastructure.impl.ModelServiceImpl;

@Service
public class SupplierServiceImpl extends ContactServiceImpl<Supplier, SupplierSearch> implements SupplierService {

    @Autowired
    private SupplierDAO supplierDAO;

    @Override
    protected ModelDAO<Supplier> getModelDAO() {
        return supplierDAO;
    }

    @Override
    public Supplier create() {
        return new Supplier();
    }

}
