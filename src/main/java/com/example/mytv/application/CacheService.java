package com.example.mytv.application;

import com.example.mytv.application.port.in.CacheManageUseCase;
import com.example.mytv.application.port.out.CacheManagePort;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CacheService implements CacheManageUseCase {

    private final CacheManagePort cacheManagePort;

    public CacheService(CacheManagePort cacheManagePort) {
        this.cacheManagePort = cacheManagePort;
    }

    @Override
    public List<String> getAllCacheNames() {
        return cacheManagePort.getAllCacheNames();
    }

    @Override
    public Boolean deleteCache(String cacheKey) {
        return cacheManagePort.deleteCache(cacheKey);
    }
}
