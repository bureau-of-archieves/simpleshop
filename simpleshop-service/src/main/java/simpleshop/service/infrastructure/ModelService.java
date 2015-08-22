package simpleshop.service.infrastructure;

import simpleshop.data.PageInfo;
import simpleshop.dto.ModelSearch;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * A set of methods shared by all model services.
 */
public interface ModelService<T, S extends ModelSearch> extends BaseService {

    /**
     * [convention:defaults]the default values of a new model is set in the create method of its service class.
     * Create a model with default values.
     * @return a transient model instance.
     */
    @NotNull  T create();

    /**
     * Save a model instance to the database.
     * @param model model instance to save..
     */
    void save(@NotNull T model);

    /**
     * Delete a model by id.
     * @param id id of the object.
     */
    void delete(Serializable id);

    /**
     * Delete a domain object.
     * @param domainObject the object to delete.
     */
    void delete(T domainObject);

    /**
     * [convention:modelId]All model classes have 1 and only 1 id property, and it's type is not composite. Domain classes with composite id is understood as a n-way relationship.
     * Get a model instance by its id.
     * @param id id.
     * @return the model instance, null if not found.
     */
    T getById(Serializable id);


    void sessionFlush();

    /**
     * Search for a list of matching models.
     * @param searchParams instance of the corresponding search class for the model class..
     * @return a list of results.
     */
    @NotNull List<T> search(@NotNull S searchParams);

    /**
     * Get a list of model instances by keywords. Each model service defines how the keywords are used.
     * This is used by auto-complete UI element.
     * @param keywords any string.
     * @param pageInfo page info.
     * @return a list of matched instances or an empty list.
     */
    @NotNull List<T> quickSearch(String keywords, PageInfo pageInfo);

    /**
     * A list of properties that should be ignored by the JSON serializer.
     * @return array of property names.
     */
    @NotNull String[] ignoredJsonProperties();

    /**
     * List of properties that should be automatically loaded.
     * @return list of property names.
     */
    Collection<String> lazyLoadedProperties(); //can return null

}
