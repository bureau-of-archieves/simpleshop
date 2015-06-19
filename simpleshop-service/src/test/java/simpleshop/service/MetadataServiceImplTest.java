package simpleshop.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import simpleshop.data.metadata.ModelMetadata;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Test <code>MetadataServiceImpl</code> methods.
 */
public class MetadataServiceImplTest extends ServiceTransactionTest {

    @Autowired
    private MetadataService metadataService;

    @Test
    public void getMetadataTest() {
        Map<String, ModelMetadata> metadataMap = metadataService.getMetadata();
        assertTrue(metadataMap.size() > 0);

        ModelMetadata customerMetadata = metadataMap.get("Customer");
        assertNotNull(customerMetadata);
        assertEquals("Customer", customerMetadata.getName());
        assertTrue(customerMetadata.getPropertyMetadataMap().size() > 0);
        assertEquals("Name", customerMetadata.getPropertyMetadata("contact.name").getLabel());

    }
}
