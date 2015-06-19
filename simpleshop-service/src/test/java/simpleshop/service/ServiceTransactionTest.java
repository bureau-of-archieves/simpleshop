package simpleshop.service;

import org.springframework.test.context.ContextConfiguration;
import simpleshop.data.test.TransactionalTest;


/**
 * Created by ZHY on 6/12/2014.
 */
@ContextConfiguration(locations = "classpath:spring-simpleshop-service-test.xml")
public abstract class ServiceTransactionTest extends TransactionalTest {

}
