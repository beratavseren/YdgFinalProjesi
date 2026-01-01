package org.example.ydgbackend.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class BlackListService {
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;


    @Autowired
    public BlackListService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void blackListToken(String token)
    {
        try {
            if(isBlackListed(token))// db ye sonsuz kere aynı token kaydedilmesin diye kontrol ediyoruz !! endpointe erişmek için rol gerekli olursa bu sorun ortadan kalkar düzelt.
            {
                return;
            }
            redisTemplate.opsForValue().set(token, token, expirationMs, java.util.concurrent.TimeUnit.MILLISECONDS);
        }catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public boolean isBlackListed(String token)
    {
        return redisTemplate.hasKey(token);
    }
}
