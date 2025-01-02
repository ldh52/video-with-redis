package com.example.mytv.adapter.in.api;

import com.example.mytv.application.port.in.CacheManageUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CacheController.class)
class CacheControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CacheManageUseCase cacheUseCase;

    @Test
    void getCacheNames() throws Exception {
        given(cacheUseCase.getAllCacheNames()).willReturn(List.of("channel", "video", "user"));

        mockMvc
            .perform(
                get("/api/v1/caches/cachenames")
            )
            .andExpectAll(
                status().isOk(),
                jsonPath("$.size()").value(3)
            );
    }
}