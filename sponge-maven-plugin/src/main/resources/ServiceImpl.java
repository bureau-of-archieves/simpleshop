@args String projectName, String modelName;
@{String basePackage = projectName.toLowerCase()}
@{String modelNameCamel = modelName.substring(0,1).toLowerCase() + modelName.substring(1)}
package @(basePackage).service.impl;

import org.springframework.stereotype.Service;
import @(basePackage).service.@(modelName)Service;
import @(basePackage).service.impl.base.@(modelName)BaseService;

@@Service
public class @(modelName)ServiceImpl extends @(modelName)BaseService implements @(modelName)Service {

}
