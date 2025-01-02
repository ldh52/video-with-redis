package com.example.mytv.adapter.in.api;

import com.example.mytv.application.port.in.CacheManageUseCase;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/caches")
public class CacheController {
    private final CacheManageUseCase cacheUseCase;

    public CacheController(CacheManageUseCase cacheUseCase) {
        this.cacheUseCase = cacheUseCase;
    }

    @GetMapping("cachenames")
    List<String> cacheNames() {
        return cacheUseCase.getAllCacheNames();
    }

    @DeleteMapping(params = "cacheKey")
    void deleteCache(@RequestParam String cacheKey) {
        cacheUseCase.deleteCache(cacheKey);
    }
}
