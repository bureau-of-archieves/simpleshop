package simpleshop.data.infrastructure;

import org.hibernate.Session;
import simpleshop.common.Pair;
import simpleshop.data.metadata.ModelMetadata;
import simpleshop.data.metadata.PropertyMetadata;

/**
 * The context under which Hibernate Criterion instances are being built.
 */
public interface CriterionContext extends CriteriaBuilder {

    Session getSession();

    int getNestedLevel();

    String getLevelAlias(String alias);

    Pair<ModelMetadata, PropertyMetadata> getMetadata(String alias, String propertyName);

    static String getLevelAlias(String alias, int level){
        if(level == 0)
            return alias;

        return alias + "_x" + level;
    }

}
