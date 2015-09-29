package simpleshop.webapp.mvc.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import simpleshop.domain.model.Category;
import simpleshop.dto.JsonResponse;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Test the category controller.
 */
public class CategoryControllerTest extends BaseControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void test1() throws Exception{

        //action
        ResultActions resultActions = mockMvc.perform(get("/category/1"));

        //assertion
        resultActions.andExpect(status().isOk());

        //action
        String content = resultActions.andReturn().getResponse().getContentAsString();
        JsonResponse<Category> response = objectMapper.readValue(content, new TypeReference<JsonResponse<Category>>(){});

        //assertion
        assertThat(response.getStatus(), equalTo(JsonResponse.STATUS_OK));
        assertThat(response.getContent().getId(), equalTo(1));
    }
}
