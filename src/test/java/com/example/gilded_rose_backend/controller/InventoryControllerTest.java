package com.example.gilded_rose_backend.controller;

import com.example.gilded_rose_backend.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class InventoryControllerTest {
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        InventoryService service = new InventoryService();
        InventoryController controller = new InventoryController(service);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void shouldUpdateInventoryList() throws Exception {
        String json = """
                [
                    {
                        "name": "Normal Item",
                        "sellIn": 5,
                        "quality": 10
                    
                    }
                ]
                """;

        mockMvc.perform(post("/api/inventory/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sellIn").value(4))
                .andExpect(jsonPath("$[0].quality").value(9));
    }
}
