package simpleshop.service.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ZHY on 27/01/2015.
 */
public aspect ServiceImplLogging {

    private static Logger logger = LoggerFactory.getLogger(ServiceImplLogging.class);

    private pointcut publicServiceImplMethods() : execution(public * sponge.service..*ServiceImpl.*(..));

    before() : publicServiceImplMethods(){
        logger.debug("Before: " + thisJoinPoint.getSignature());
    }
}
