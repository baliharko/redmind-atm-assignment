package com.baliharko.redmindatm.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@AutoConfigureDataMongo
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AtmControllerTest {

    @Autowired
    private AtmController atmController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/atm/all")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].value").value(1000))
                .andExpect(jsonPath("$.[1].value").value(1000))
                .andExpect(jsonPath("$.[2].value").value(500))
                .andExpect(jsonPath("$.[3].value").value(500))
                .andExpect(jsonPath("$.[4].value").value(500))
                .andExpect(jsonPath("$.[5].value").value(100))
                .andExpect(jsonPath("$.[6].value").value(100))
                .andExpect(jsonPath("$.[7].value").value(100))
                .andExpect(jsonPath("$.[8].value").value(100))
                .andExpect(jsonPath("$.[9].value").value(100));
    }

    @Test
    public void getCurrentBalanceTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/atm/balance")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().string("4000.0"));
    }

    @Test
    public void withdrawBillsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/atm/withdraw/500")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].value").value(500))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("REDMIND - ATM Assignment")
    public void redmindAtmWithdrawTest() throws Exception {

        // Starting balance - 4000
        mockMvc.perform(MockMvcRequestBuilders
                .get("/atm/balance")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().string("4000.0"));


        // Withdrawing 1500 - should receive 1x 1000 and 1x 500 bill.
        mockMvc.perform(MockMvcRequestBuilders
                .get("/atm/withdraw/1500")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].value").value(1000))
                .andExpect(jsonPath("$.[1].value").value(500))
                .andExpect(jsonPath("$.length()").value(2));


        // Withdrawing 700 - should receive 1x 500 and 2x 100 bills.
        mockMvc.perform(MockMvcRequestBuilders
                .get("/atm/withdraw/700")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].value").value(500))
                .andExpect(jsonPath("$.[1].value").value(100))
                .andExpect(jsonPath("$.[2].value").value(100))
                .andExpect(jsonPath("$.length()").value(3));


        // Withdrawing 400 - should throw error with message "Not enough bills"
        mockMvc.perform(MockMvcRequestBuilders
                .get("/atm/withdraw/400")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Not enough bills"));


        // Balance check - 1800
        mockMvc.perform(MockMvcRequestBuilders
                .get("/atm/balance")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().string("1800.0"));


        // Withdrawing 1100 - should receive 1x 1000 and 1x 100 bill.
        mockMvc.perform(MockMvcRequestBuilders
                .get("/atm/withdraw/1100")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].value").value(1000))
                .andExpect(jsonPath("$.[1].value").value(100))
                .andExpect(jsonPath("$.length()").value(2));


        // Withdrawing 1000 - should throw error with message "Insufficient funds"
        mockMvc.perform(MockMvcRequestBuilders
                .get("/atm/withdraw/1000")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Insufficient funds"));


        // Withdrawing 700 - should receive 1x 500 and 2x 100 bills.
        mockMvc.perform(MockMvcRequestBuilders
                .get("/atm/withdraw/700")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].value").value(500))
                .andExpect(jsonPath("$.[1].value").value(100))
                .andExpect(jsonPath("$.[2].value").value(100))
                .andExpect(jsonPath("$.length()").value(3));


        // Withdrawing 300 - should throw error with message "Insufficient funds"
        mockMvc.perform(MockMvcRequestBuilders
                .get("/atm/withdraw/300")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Insufficient funds"));


        // Balance check - 0
        mockMvc.perform(MockMvcRequestBuilders
                .get("/atm/balance")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().string("0.0"));
    }

}