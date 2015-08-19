package simpleshop.data.test;

import org.hibernate.SessionFactory;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import simpleshop.common.JsonConverter;
import simpleshop.data.PageInfo;
import simpleshop.data.infrastructure.ModelDAO;
import java.util.List;

/**
 * Transactional unit tests.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-sponge-data-test.xml")
@Transactional
@TransactionConfiguration(defaultRollback = false)
public abstract class TransactionalTest {

    protected static final Logger logger = LoggerFactory.getLogger(TransactionalTest.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    protected JsonConverter jsonConverter;

    protected <T> void cleanUp(ModelDAO<T> modelDAO, String keyword){

        List<T> objects = modelDAO.quickSearch(keyword, new PageInfo(0, 1000));
        if(objects.size() > 0){
            logger.debug("Clearing " + objects.size() + " objects of type " + objects.get(0).getClass().getSimpleName());
            objects.forEach(modelDAO::delete);
            flush();
        }
    }

    protected void flush(){
        sessionFactory.getCurrentSession().flush();
    }

}
