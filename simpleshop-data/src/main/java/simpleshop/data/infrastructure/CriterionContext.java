package simpleshop.data.infrastructure;

import org.hibernate.Session;
import simpleshop.common.Pair;
import simpleshop.data.metadata.ModelMetadata;
import simpleshop.data.metadata.PropertyMetadata;

/**
 * The context under which Hibernate Criterion instances are being built.
 */
public interface CriterionContext {

    Pair<ModelMetadata, PropertyMetadata> getMetadata(String qualifiedPropertyName);

    Session getSession();

}
