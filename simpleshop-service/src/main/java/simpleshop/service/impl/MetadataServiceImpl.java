package simpleshop.service.impl;

import org.springframework.stereotype.Service;
import simpleshop.data.metadata.ModelMetadata;
import simpleshop.data.util.DomainUtils;
import simpleshop.domain.model.*;
import simpleshop.domain.model.component.Address;
import simpleshop.domain.model.component.OrderItem;
import simpleshop.domain.model.component.ProductSupplier;
import simpleshop.dto.*;
import simpleshop.service.MetadataService;
import simpleshop.service.infrastructure.impl.BaseServiceImpl;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Get metadata of the domain models.
 */
@Service
public class MetadataServiceImpl extends simpleshop.service.impl.BaseMetadataService {

}
