package com.samsamhajo.deepground;
import com.samsamhajo.deepground.global.config.MongoConfig;
import com.samsamhajo.deepground.global.config.RedisConfig;
import com.samsamhajo.deepground.global.config.S3Config;
import com.samsamhajo.deepground.global.upload.S3Uploader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;


@SpringBootTest
class DeepgroundApplicationTests {

  @MockBean
  protected S3Config s3Config;

  @MockBean
  protected RedisTemplate<String, String> redisTemplate;

  @MockBean
  protected MongoTemplate mongoTemplate;

  @MockBean
  protected S3Uploader s3Uploader;

  @MockBean
  protected RedisConfig redisConfig;

  @MockBean
  protected MongoConfig mongoConfig;

  @MockBean
  protected JavaMailSender javaMailSender;

}
