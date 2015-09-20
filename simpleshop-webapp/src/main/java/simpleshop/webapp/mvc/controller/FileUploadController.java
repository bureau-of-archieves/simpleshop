package simpleshop.webapp.mvc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import simpleshop.common.StringUtils;
import simpleshop.domain.model.Category;
import simpleshop.dto.EmployeeSearch;
import simpleshop.dto.JsonResponse;
import simpleshop.service.CategoryService;
import simpleshop.webapp.infrastructure.BaseJsonController;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(produces = "application/json")
public class FileUploadController extends BaseJsonController {

    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList("jpg", "png", "gif");
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "/category/{id}/imagePath", method = RequestMethod.POST)
    public String uploadCategoryImage(@PathVariable("id") int id, @RequestParam("image") MultipartFile imageFile, Model model){

        //check extension
        String extension = StringUtils.subStrAfterLast(imageFile.getOriginalFilename(), ".");
        if(StringUtils.isNullOrEmpty(extension) || !IMAGE_EXTENSIONS.contains(extension.toLowerCase())){
            throw new IllegalArgumentException("Can only accept: " + org.apache.commons.lang3.StringUtils.join(IMAGE_EXTENSIONS, ","));
        }

        //upload file
        String uploadDir = servletContext.getRealPath("/WEB-INF/uploads/img"); //todo will need to configure an external location
        Path filePath = Paths.get(uploadDir, imageFile.getOriginalFilename());
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

        //set category image path
        Category category = categoryService.getById(id);
        category.setImagePath(imageFile.getOriginalFilename());
        categoryService.save(category);

        //return result
        JsonResponse<String> response = new JsonResponse<>(JsonResponse.STATUS_OK, null, category.getImagePath());
        return super.outputJson(model, response, null);
    }
}
