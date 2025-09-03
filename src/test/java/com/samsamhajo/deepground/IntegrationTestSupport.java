package com.samsamhajo.deepground;

import com.samsamhajo.deepground.auth.jwt.JwtProvider;
import com.samsamhajo.deepground.auth.oauth.OAuth2AuthenticationSuccessHandler;
import com.samsamhajo.deepground.global.config.MongoConfig;
import com.samsamhajo.deepground.global.config.S3Config;
import com.samsamhajo.deepground.global.upload.S3Uploader;
import com.samsamhajo.deepground.support.TestMailConfig;
import com.samsamhajo.deepground.support.TestRedisConfig;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import({TestMailConfig.class, TestRedisConfig.class})
public abstract class IntegrationTestSupport {
    
    @MockBean
    protected S3Config s3Config;
    
    @MockBean
    protected S3Uploader s3Uploader;
    
    @MockBean
    protected MongoConfig mongoConfig;

    @MockBean
    protected JwtProvider jwtProvider;

    @MockBean
    protected OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
}