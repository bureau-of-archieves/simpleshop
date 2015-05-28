package simpleshop.data.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public aspect DAOImplLogging {

    private static Logger logger = LoggerFactory.getLogger(DAOImplLogging.class);

    private pointcut publicDAOImplMethods() : execution(public * simpleshop.data..*DAOImpl.*(..));

    before() : publicDAOImplMethods(){
        logger.debug("Before: " + thisJoinPoint.getSignature());
    }
}
