
package simpleshop.domain.aop;


import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Add Jackson annotations to domain classes so as to ignore certain properties in JSON serialization.
 */
public aspect HibernateJacksonAnnotations {

    //how to write type patterns in AJ: https://eclipse.org/aspectj/doc/released/progguide/semantics-pointcuts.html#type-patterns

    declare @type: quickshop.domain.model..*: @JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"}, ignoreUnknown = true);
    declare @type: quickshop.domain.model..*: @JsonFilter("propNameFilter");

}