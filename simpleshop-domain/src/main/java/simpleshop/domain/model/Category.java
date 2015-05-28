package simpleshop.domain.model;

import simpleshop.Constants;
import simpleshop.domain.metadata.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by ZHY on 10/01/2015.
 */
@Entity
@Table(name = "categories")
public class Category {

    private Integer id;
    private String name;
    private String description;
    private String imagePath;
    private Category parent;
    private Boolean menuItem = Boolean.FALSE;

    @Id
    @GeneratedValue
    @Column(nullable = false, insertable = false, updatable = false)
    @ItemValue
    @ItemText
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Summary
    @Column(length = Constants.MID_STRING_LENGTH, nullable = false)
    @NotNull
    @ItemText(order = 1, separator = " - ")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Summary
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

}
