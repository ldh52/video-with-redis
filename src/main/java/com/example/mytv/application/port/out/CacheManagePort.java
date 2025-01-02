package com.example.mytv.application.port.out;

import java.util.List;

public interface CacheManagePort {
    List<String> getAllCacheNames();

    List<String> getAllCacheNames(String pattern);

    Boolean deleteCache(String cacheKey);
}
