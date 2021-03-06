package simpleshop.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import simpleshop.data.CategoryDAO;
import simpleshop.data.test.TestConstants;
import simpleshop.domain.model.Category;
import simpleshop.dto.CategorySearch;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class CategoryServiceImplTest extends ServiceTransactionTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryDAO categoryDAO;

    @Before
    public void cleanUp() {
        super.cleanUp(categoryDAO, TestConstants.CATEGORY_MARK);
    }

    @Test
    public void createDeleteTest() {

        Category category = new Category();
        category.setName("My Root Category");
        category.setDescription(TestConstants.CATEGORY_MARK);

        categoryService.save(category);

        assertThat(category.getId(), notNullValue());
        assertThat(category.getPrefix(), equalTo("_" + category.getId()));

        Category subCategory = new Category();
        subCategory.setName("My Child Category 1");
        subCategory.setDescription(TestConstants.CATEGORY_MARK);
        subCategory.setParent(category);
        categoryService.save(subCategory);
        flush();

        assertThat(subCategory.getId(), notNullValue());
        assertThat(subCategory.getPrefix(), equalTo(category.getPrefix() + "_" + subCategory.getId()));

        categoryService.delete(category);
        categoryService.delete(subCategory);
    }

    @Test
    public void searchTest() {

        Category root = new Category();
        root.setName("Root");
        root.setDescription(TestConstants.CATEGORY_MARK);
        categoryService.save(root);

        Category child1 = new Category();
        child1.setName("Child1");
        child1.setDescription(TestConstants.CATEGORY_MARK);
        child1.setParent(root);
        categoryService.save(child1);

        Category child2 = new Category();
        child2.setName("Child2");
        child2.setDescription(TestConstants.CATEGORY_MARK);
        child2.setParent(root);
        categoryService.save(child2);

        Category grandChild1 = new Category();
        grandChild1.setName("Grand Child1");
        grandChild1.setDescription(TestConstants.CATEGORY_MARK);
        grandChild1.setParent(child1);
        categoryService.save(grandChild1);

        flush();

        //get all
        CategorySearch categorySearch = new CategorySearch();
        categorySearch.setPageSize(20);
        categorySearch.setName(TestConstants.CATEGORY_MARK);
        List<Category> result = categoryService.search(categorySearch);
        assertThat(result.size(), equalTo(4));

        //get child
        categorySearch.setParentCategory(root);
        result = categoryService.search(categorySearch);
        assertThat(result.size(), equalTo(2));

        //get descendants
        categorySearch.setParentCategory(null);
        categorySearch.setPrefix(root.getPrefix());
        result = categoryService.search(categorySearch);
        assertThat(result.size(), equalTo(4));

        //search by name
        categorySearch.setName("Grand");
        result = categoryService.search(categorySearch);
        assertThat(result.size(), equalTo(1));

    }

    @Test
    public void getDropdownItemsTest(){
        List<Category> result = categoryService.getDropdownItems();

        assertThat(result.size(), greaterThanOrEqualTo(3));

    }

    @Test
    public void beanValidationIsNotEnabledInHibernate(){

        //arrange
        Category parent = new Category();
        parent.setName("Parent");
        parent.setDescription(TestConstants.CATEGORY_MARK);
        categoryService.save(parent);
        flush();

        Category child = new Category();
        child.setName("Child");
        child.setDescription(TestConstants.CATEGORY_MARK);
        child.setParent(parent);
        categoryService.save(child);
        flush();

        //action
        try {
            parent.setName("P");
            parent.setParent(child);
            categoryService.save(parent);
            flush();

            //assertion no validation error

        } finally {
            categoryService.delete(child);
            categoryService.delete(parent);
        }

    }
}
