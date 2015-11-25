package simpleshop.webapp.mvc.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.ResultActions;
import simpleshop.dto.CustomerOrder;
import simpleshop.dto.JsonResponse;
import simpleshop.dto.ShoppingCart;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerOrderControllerTest extends BaseControllerTest{

    @Autowired
    private ObjectMapper objectMapper;

    private JsonResponse<CustomerOrder> getResponseObject(ResultActions resultActions) throws Exception{
        String content = resultActions.andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(content, new TypeReference<JsonResponse<CustomerOrder>>(){});
    }

    @Test
    public void getCustomerOrderWithNoShoppingCart() throws Exception {

        //action
        ResultActions resultActions = mockMvc.perform(get("/customer_order/new"));

        //assertion
        resultActions.andExpect(status().isOk());

        //action
        JsonResponse<CustomerOrder> response = getResponseObject(resultActions);

        //assertion
        assertThat(response.getStatus(), equalTo(JsonResponse.STATUS_ERROR));
        assertThat(response.getDescription(), equalTo(CustomerOrderController.NO_SHOPPING_CART_FOUND));

    }

    @Test
    public void getCustomerOrderWithEmptyShoppingCart() throws Exception {

        //arrange
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute(CartController.SHOPPING_CART_SESSION_KEY, new ShoppingCart());

        //action
        ResultActions resultActions = mockMvc.perform(get("/customer_order/new").session(mockHttpSession));

        //assertion
        resultActions.andExpect(status().isOk());

        //action
        JsonResponse<CustomerOrder> response = getResponseObject(resultActions);

        //assertion
        assertThat(response.getStatus(), equalTo(JsonResponse.STATUS_ERROR));
        assertThat(response.getDescription(), equalTo(CustomerOrderController.NO_SHOPPING_CART_ITEM_FOUND));

    }
}
