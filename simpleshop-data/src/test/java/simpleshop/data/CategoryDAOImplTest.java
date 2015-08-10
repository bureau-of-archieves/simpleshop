package simpleshop.data;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import simpleshop.data.test.TransactionalTest;
import simpleshop.domain.model.*;

import java.util.List;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class CategoryDAOImplTest extends TransactionalTest {

    @Autowired
    private CategoryDAO categoryDAO;


    @Test
    public void quickSearchTest(){
        List<Category> categories = categoryDAO.quickSearch("F", new PageInfo());

        assertThat(categories.size(), greaterThanOrEqualTo(1));

        for(Category category : categories){
            assertThat(category.getName(), startsWith("F"));
        }

    }


}
