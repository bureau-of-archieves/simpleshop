package simpleshop.service;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import simpleshop.data.PageInfo;
import simpleshop.data.infrastructure.ModelDAO;
import simpleshop.data.test.TransactionalTest;
import simpleshop.service.infrastructure.ModelService;

import java.util.List;


/**
 * Base transaction test class for the service layer.
 */
@ContextConfiguration(locations = "classpath:spring-simpleshop-service-test.xml")
public abstract class ServiceTransactionTest extends TransactionalTest {



    protected <T> void cleanUp(ModelService<T, ?> modelService, String keyword){

        List<T> objects = modelService.quickSearch(keyword, new PageInfo(0, 1000));
        if(objects.size() > 0){
            logger.debug("Clearing " + objects.size() + " objects of type " + objects.get(0).getClass().getSimpleName());
            objects.forEach(modelService::delete);
            flush();
        }
    }

}
