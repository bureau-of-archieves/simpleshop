package simpleshop.data.infrastructure;

import simpleshop.data.PageInfo;
import simpleshop.data.metadata.ModelMetadata;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZHY on 28/10/2014.
 */
public interface ModelDAO<T> extends BaseDAO{

    T load(Serializable id);

    T get(Serializable id);

    void save(T domainObject);

    void delete(T domainObject);

    List<T> search(ModelMetadata searchMetadata, ModelMetadata modelMetadata, PageInfo searchObject);

    List<T> quickSearch(String keywords, PageInfo pageInfo);
}
