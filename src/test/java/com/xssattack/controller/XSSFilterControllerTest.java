package com.xssattack.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.xssattack.model.Address;
import com.xssattack.model.Employee;
import com.xssattack.utils.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class XSSFilterControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void whenPostEmployee_get_successfully_message() throws Exception {
        Employee alex = new Employee("1",
                "Sushil",
                "Manager",
                43,
                List.of(new Address("Sanpada", "India", "400705")),
                Collections.singleton("9819183318"),
                Collections.singletonMap("Honda", 6088L));

        ResultActions resultActions = mvc.perform(post("/test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(alex)))
                .andExpect(status().isOk());
        String expectedResult = "Post call successfully";
        String actualResponse = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("Expected "+expectedResult);
        System.out.println("actualResponse "+actualResponse);
        org.junit.Assert.assertEquals(expectedResult, actualResponse);
    }

    @Test
    public void bad_request_when_script_tag_present() throws Exception {
        Employee alex = new Employee("1",
                "Sushil",
                "<script>alert(1)</script>",
                43,
                List.of(new Address("Sanpada", "India", "400705")),
                Collections.singleton("9819183318"),
                Collections.singletonMap("Honda", 6088L));

        ResultActions resultActions = mvc.perform(post("/test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(alex)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status", is(403)))
                .andExpect(jsonPath("$.message", is("XSS attack error")));
        String actualResponse = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("actualResponse "+actualResponse);
    }

    @Test
    public void bad_request_when_post_request_with_xss() throws Exception {
        ResultActions resultActions = mvc.perform(post("/hello/<div> <script>alert`1`</script> </div>")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status", is(403)))
                .andExpect(jsonPath("$.message", is("XSS attack error")));
        String actualResponse = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("actualResponse "+actualResponse);
    }

}