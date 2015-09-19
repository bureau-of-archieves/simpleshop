package simpleshop.domain.model;

import org.hibernate.annotations.BatchSize;
import simpleshop.Constants;
import simpleshop.domain.metadata.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Category domain object.
 * There are two types of Category objects:
 * menuItem = true  - an item in the menu tree.
 * menuItem = false - use as a standalone tag.
 */
@Entity
@Table(name = "categories")
@DisplayFormat("concat:'id':' - ':'name'")
@Icon("tags")//[sponge]searchable - searchable iff domain object has icon and a search object.
public class Category {

    private Integer id;

    /**
     * Name of the category.
     */
    private String name;

    /**
     * A short description.
     */
    private String description;

    /**
     * Category image.
     */
    private String imagePath;

    /**
     * Parent category for menu items.
     * Tag category can have a parent but things under a tag does not belong to the parent tag.
     */
    private Category parent;

    /**
     * If is a menu item or not.
     */
    private Boolean menuItem = Boolean.FALSE;

    /**
     * Used for hierarchical search.
     * searchPrefix = parentSearchPrefix + "_" + id.
     * For the root category it is "_" + id.
     */
    private String prefix;

    @Id
    @GeneratedValue
    @Column(nullable = false, insertable = false, updatable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(length = Constants.MID_STRING_LENGTH, nullable = false)
    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = Constants.LONG_STRING_LENGTH)
    @ItemDescription
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "image_path", length = Constants.LONG_STRING_LENGTH)
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @BatchSize(size = Constants.BATCH_SIZE)
    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    @Column(name = "menu_item", nullable = false)
    @NotNull
    @Description("Whether this category is a part of product menu tree")
    public Boolean getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(Boolean menuItem) {
        this.menuItem = menuItem;
    }

    @Column(name = "prefix", length = Constants.MID_STRING_LENGTH)
    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
