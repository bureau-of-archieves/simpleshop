@args String projectName, String modelName;
@{String basePackage = projectName.toLowerCase()}
@{String modelNameCamel = modelName.substring(0,1).toLowerCase() + modelName.substring(1)}
package @(basePackage).service;

import @(basePackage).domain.model.@(modelName);
import @(basePackage).dto.@(modelName)Search;
import @(basePackage).service.infrastructure.ModelService;

public interface @(modelName)Service extends ModelService<@(modelName), @(modelName)Search> {

}