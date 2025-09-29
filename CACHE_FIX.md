# CacheManager 충돌 오류 해결 가이드

## 발생한 문제
```
java.lang.IllegalStateException: No CacheResolver specified, and no unique bean of type CacheManager found. 
Mark one as primary or declare a specific CacheManager to use.
NoUniqueBeanDefinitionException: expected single matching bean but found 3: 
cacheManager,userCacheManager,productCacheManager
```

## 해결 방법

### 1. 다중 CacheManager 제거 (단순화)
기존의 3개 CacheManager를 1개로 통합하여 충돌을 완전히 제거했습니다.

**수정된 CacheConfig.java:**
```java
@Configuration
@EnableCaching
public class CacheConfig {
    
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(caffeineCacheBuilder());
        return cacheManager;
    }

    private Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(1000)
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .recordStats();
    }
}
```

### 2. 캐시 사용법
단일 CacheManager를 사용하면서 캐시 이름으로 구분:

```java
@Cacheable(value = "users", key = "#id")
public User findUserById(Long id) { ... }

@Cacheable(value = "products", key = "#id")
public Product findProductById(Long id) { ... }

@Cacheable(value = "approvals", key = "#id")
public Approval findApprovalById(Long id) { ... }
```

### 3. 캐시 성능 모니터링
Caffeine 캐시 통계 활성화로 성능 모니터링 가능:
- 히트율 (Hit Rate)
- 미스율 (Miss Rate) 
- 축출 횟수 (Eviction Count)

## 실행 명령
```bash
gradlew.bat clean
gradlew.bat bootRun
```

이제 CacheManager 충돌 없이 애플리케이션이 정상 시작됩니다.
