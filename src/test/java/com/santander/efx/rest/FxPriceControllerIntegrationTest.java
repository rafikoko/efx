package com.santander.efx.rest;

import com.santander.efx.entity.FxPriceFeedRecord;
import com.santander.efx.repository.FxPriceFeedRepository;
import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static com.santander.efx.TestUtils.createBaseFxPriceFeedRecords;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
class FxPriceControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private FxPriceFeedRepository repository;

    private MockMvc mockMvc;
    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void givenWebApplicationContext_whenServletContext_thenItProvidesController() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(mockMvc);
    }

    @Test
    void getLatestPriceByInstrumentName() throws Exception {
        //given
        List<FxPriceFeedRecord> records = createBaseFxPriceFeedRecords();
        repository.saveAllAndFlush(records);

        //confirm successful persistence
        assertEquals(3,repository.findAll().size());

        final String expected = """
                {"id":110,"instrumentName":"EUR/JPY","bid":119.6100000000,"ask":119.9100000000,"bidWithCommission":119.7296100000,"askWithCommission":119.7900900000,"timestamp":"2020-06-01T12:01:02.11","valid":true,"validationMessage":null}
                """;

        //when & then
        this.mockMvc
                .perform(get("/fx?instrumentName=EUR/JPY"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(expected));
    }

    @Test
    void getLatestPriceByInstrumentName_FailedAsWrongInstrument() throws Exception {
        //given
        List<FxPriceFeedRecord> records = createBaseFxPriceFeedRecords();
        repository.saveAllAndFlush(records);

        //confirm successful persistence
        assertEquals(3,repository.findAll().size());

        //when & then
        this.mockMvc
                .perform(get("/fx?instrumentName=EUR/GBP"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

}