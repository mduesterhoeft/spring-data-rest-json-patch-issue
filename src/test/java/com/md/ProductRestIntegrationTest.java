package com.md;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.is;
import static org.springframework.data.rest.webmvc.RestMediaTypes.HAL_JSON;
import static org.springframework.data.rest.webmvc.RestMediaTypes.JSON_PATCH_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.EntityLinks;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductRestIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private EntityLinks entityLinks;

    private ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();

    private MockMvc mockMvc;

    private Product product;
    private String patchPayload;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    }

    @Test
    public void should_patch_gtin() throws Exception {
        givenProduct();
        givenPatchItem();

        mockMvc.perform(MockMvcRequestBuilders.patch(entityLinks.linkForSingleResource(product).toUri())
                .accept(HAL_JSON)
                .contentType(JSON_PATCH_JSON)
                .content(patchPayload))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("gtin.type", is("EAN")))
        ;
    }

    @SneakyThrows
    private void givenPatchItem() {
        HashMap<String, Object> newGtin = new HashMap<>();
        newGtin.put("type", "EPN");
        newGtin.put("value", "some");

        HashMap<String, Object> patchItem = new HashMap<>();
        patchItem.put("op", "replace");
        patchItem.put("path", "/gtin");
        patchItem.put("value", newGtin);
        patchPayload = new ObjectMapper().writeValueAsString(singletonList(patchItem));
    }

    private void givenProduct() {
        product = new Product();
        product.setName("some");
        Gtin gtin = new Gtin();
        gtin.setType("ISBN");
        gtin.setValue("1212-223-343");
        product.setGtin(gtin);
        product = productRepository.saveAndFlush(product);
    }
}
