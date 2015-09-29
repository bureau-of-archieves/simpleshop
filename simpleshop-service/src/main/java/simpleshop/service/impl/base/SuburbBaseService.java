

package simpleshop.service.impl.base;

import org.springframework.beans.factory.annotation.Autowired;
import simpleshop.data.SuburbDAO;
import simpleshop.data.infrastructure.ModelDAO;
import simpleshop.domain.model.Suburb;
import simpleshop.dto.SuburbSearch;
import simpleshop.service.SuburbService;
import simpleshop.service.infrastructure.impl.ModelServiceImpl;

public abstract class SuburbBaseService extends ModelServiceImpl<Suburb, SuburbSearch> implements SuburbService {

    @Autowired
    protected SuburbDAO suburbDAO;

    @Override
    protected ModelDAO<Suburb> getModelDAO() {
        return suburbDAO;
    }

    @Override
    public Suburb create() {
        return new Suburb();
    }

}
