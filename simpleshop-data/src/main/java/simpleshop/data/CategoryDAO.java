package simpleshop.data;


import simpleshop.data.infrastructure.ModelDAO;
import simpleshop.domain.model.Category;

import java.util.List;

/**
 * Put Category specific data operations here.
 */
public interface CategoryDAO extends ModelDAO<Category> {

    List<Category> getDropdownItems(int maxSize);
}
