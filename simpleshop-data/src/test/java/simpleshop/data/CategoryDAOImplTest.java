package simpleshop.data;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import simpleshop.data.test.TestConstants;
import simpleshop.data.test.TransactionalTest;
import simpleshop.domain.model.*;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class CategoryDAOImplTest extends TransactionalTest {

    @Autowired
    private CategoryDAO categoryDAO;

    @Test
    public void quickSearchTest() {
        List<Category> categories = categoryDAO.quickSearch(TestConstants.SUB_CATEGORY_1, new PageInfo());

        assertThat(categories.size(), equalTo(1));

        for (Category category : categories) {
            assertThat(category.getName(), containsString(TestConstants.SUB_CATEGORY_1));
        }

        categories = categoryDAO.quickSearch("KPRC321FE", new PageInfo());
        assertThat(categories.size(), equalTo(0));
    }

    @Test
    public void createDeleteTest() {

        //can save without parent
        Category category = new Category();
        category.setName(TestConstants.CATEGORY_MARK + "1");
        categoryDAO.save(category);
        categoryDAO.sessionFlush();
        assertThat(category.getId(), notNullValue());

        //can save with parent
        Category subCategory = new Category();
        subCategory.setParent(category);
        subCategory.setName(TestConstants.CATEGORY_MARK + "_Sub1");
        subCategory.setMenuItem(true);
        subCategory.setDescription("Random description");
        subCategory.setImagePath("cate_pic.jpg");
        categoryDAO.save(subCategory);
        categoryDAO.sessionFlush();
        assertThat(subCategory.getId(), notNullValue());

        categoryDAO.evict(category);
        categoryDAO.evict(subCategory);

        //can load the same values.
        subCategory = categoryDAO.load(subCategory.getId());
        assertThat(subCategory.getImagePath(), equalTo("cate_pic.jpg"));
        assertThat(subCategory.getMenuItem(), equalTo(true));
        assertThat(subCategory.getDescription(), equalTo("Random description"));

        category = subCategory.getParent();
        assertThat(category.getName(), equalTo(TestConstants.CATEGORY_MARK + "1"));
    }

    @Before
    public void cleanUp() {
        cleanUp(categoryDAO, TestConstants.CATEGORY_MARK);
    }


}
