package simpleshop.data.metadata;

/**
 * Type of model.
 */
public enum ModelType {
    /**
     * Top level domain model.
     */
    DOMAIN,

    /**
     * Embedded part of a domain model.
     */
    EMBEDDED,

    /**
     * Collection in domain model.
     */
    COLLECTION,

    /**
     * Model which is not persisted to the database.
     */
    DTO
}
