package com.samsamhajo.deepground;

import com.samsamhajo.deepground.global.config.S3Config;
import com.samsamhajo.deepground.global.upload.S3Uploader;
import com.samsamhajo.deepground.support.TestContainers;
import com.samsamhajo.deepground.support.TestMailConfig;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.context.annotation.Import;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import({TestMailConfig.class})
@ImportTestcontainers(TestContainers.class)
public abstract class IntegrationTestSupport {
    
    @MockBean
    protected S3Config s3Config;
    
    @MockBean
    protected S3Uploader s3Uploader;
}
