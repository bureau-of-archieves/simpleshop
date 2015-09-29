@args String projectName, String modelName;
@{String basePackage = projectName.toLowerCase()}
@{String modelNameCamel = modelName.substring(0,1).toLowerCase() + modelName.substring(1)}

package simpleshop.service.impl.base;

import org.springframework.beans.factory.annotation.Autowired;
import @(basePackage).data.@(modelName)DAO;
import @(basePackage).data.infrastructure.ModelDAO;
import @(basePackage).domain.model.@(modelName);
import @(basePackage).dto.@(modelName)Search;
import @(basePackage).service.@(modelName)Service;
import @(basePackage).service.infrastructure.impl.ModelServiceImpl;

public abstract class @(modelName)BaseService extends ModelServiceImpl<@(modelName), @(modelName)Search> implements @(modelName)Service {

    @@Autowired
    protected @(modelName)DAO @(modelNameCamel)DAO;

    @@Override
    protected ModelDAO<@(modelName)> getModelDAO() {
        return @(modelNameCamel)DAO;
    }

    @@Override
    public @(modelName) create() {
        return new @(modelName)();
    }

}