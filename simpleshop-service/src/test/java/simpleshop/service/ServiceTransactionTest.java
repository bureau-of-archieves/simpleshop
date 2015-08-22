package simpleshop.service;

import org.springframework.test.context.ContextConfiguration;
import simpleshop.data.test.TransactionalTest;

/**
 * Base transaction test class for the service layer.
 */
@ContextConfiguration(locations = "classpath:spring-simpleshop-service-test.xml")
public abstract class ServiceTransactionTest extends TransactionalTest {

}
