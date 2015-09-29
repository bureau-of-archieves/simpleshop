package simpleshop.webapp.mvc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import simpleshop.common.StringUtils;
import simpleshop.domain.model.Category;
import simpleshop.domain.model.Product;
import simpleshop.dto.EmployeeSearch;
import simpleshop.dto.JsonResponse;
import simpleshop.service.CategoryService;
import simpleshop.service.ProductService;
import simpleshop.webapp.infrastructure.BaseJsonController;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(produces = "application/json")
public class FileUploadController extends BaseJsonController {

    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList("jpg", "png", "gif");
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);
    private static final String IMAGE_UPLOAD_ROOT = "/WEB-INF/uploads/img/"; //todo will need to configure an external location

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private CategoryService categoryService;

    private String uploadImage(String prefix, MultipartFile imageFile){

        String uploadDir = servletContext.getRealPath(IMAGE_UPLOAD_ROOT);
        String savedFilename = prefix + "-" + imageFile.getOriginalFilename().replaceAll("\\s+", "_");
        Path filePath = Paths.get(uploadDir, savedFilename);
        File file = filePath.toFile();
        try{
            boolean created = file.createNewFile();
            if(!created){
                logger.warn("Overwriting existing file: " + filePath);
            }
            FileOutputStream fileOutputStream = null;
            try{
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(imageFile.getBytes());
            } finally {
                if(fileOutputStream != null){
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            }
        }catch (IOException ex){
            throw new RuntimeException(ex);
        }
        return savedFilename;
    }

    private void checkImageExtension(@RequestParam("image") MultipartFile imageFile) {
        String extension = StringUtils.subStrAfterLast(imageFile.getOriginalFilename(), ".");
        if(StringUtils.isNullOrEmpty(extension) || !IMAGE_EXTENSIONS.contains(extension.toLowerCase())){
            throw new IllegalArgumentException("Can only accept: " + org.apache.commons.lang3.StringUtils.join(IMAGE_EXTENSIONS, ","));
        }
    }

    @RequestMapping(value = "/category/{id}/image_path", method = RequestMethod.POST)
    public @ResponseBody JsonResponse<String>  uploadCategoryImage(@PathVariable("id") int id, @RequestParam("image") MultipartFile imageFile, Model model){

        //check extension
        checkImageExtension(imageFile);

        //upload file
        String savedFileName = uploadImage("cat" + id, imageFile);

        //update model property
        Category category = categoryService.getById(id);
        category.setImagePath(savedFileName);
        categoryService.save(category);

        //return result
        return JsonResponse.createSuccess(category.getImagePath());
    }

    @RequestMapping(value = "/product/{id}/images", method = RequestMethod.POST)
    public @ResponseBody JsonResponse<List<String>> uploadProductImages(@PathVariable("id") int id, @RequestParam("images") List<MultipartFile> imageFiles, Model model){

        //check extension
        imageFiles.forEach(this::checkImageExtension);

        //upload files
        ArrayList<String> savedFilenames = new ArrayList<>();
        for(MultipartFile imageFile : imageFiles){
            String savedFileName = uploadImage("prod" + id, imageFile);
            savedFilenames.add(savedFileName);
        }

//        //update model property <- do this with the product model save.
//        Product product = productService.getById(id);
//        product.getImages().addAll(savedFilenames);
//        productService.save(product);

        //return result
        return JsonResponse.createSuccess(savedFilenames);
    }

}
