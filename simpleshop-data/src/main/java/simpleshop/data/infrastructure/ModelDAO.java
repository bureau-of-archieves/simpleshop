package simpleshop.data.infrastructure;

import simpleshop.data.PageInfo;
import simpleshop.data.metadata.ModelMetadata;

import java.io.Serializable;
import java.util.List;

/**
 * DAO methods shared by all model DAOs.
 */
public interface ModelDAO<T> extends BaseDAO{

    T load(Serializable id);

    T get(Serializable id);

    void save(T domainObject);

    void delete(T domainObject);

    List<T> search(ModelMetadata searchMetadata, ModelMetadata modelMetadata, PageInfo searchObject);

    List<T> quickSearch(String keywords, PageInfo pageInfo);

    Class<T> getModelClass();
}
