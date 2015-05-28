package simpleshop.data;

import simpleshop.data.metadata.ModelMetadata;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZHY on 28/10/2014.
 */
public interface ModelDAO<T> extends BaseDAO{

    public T load(Serializable id);

    public T get(Serializable id);

    public void save(T domainObject);

    public void delete(T domainObject);

    public List<T> search(ModelMetadata searchMetadata, ModelMetadata modelMetadata, PageInfo searchObject);

    public List<T> quickSearch(String keywords, PageInfo pageInfo);
}
