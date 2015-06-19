package simpleshop.data.test;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import simpleshop.common.JsonConverter;

/**
 * Created with IntelliJ IDEA.
 * User: JOHNZ
 * Date: 8/09/14
 * Time: 2:15 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-sponge-data-test.xml")
@Transactional
@TransactionConfiguration(defaultRollback = false)
public abstract class TransactionalTest {

    protected static final Logger logger = LoggerFactory.getLogger(TransactionalTest.class);

    @Autowired
    protected JsonConverter jsonConverter;

}
