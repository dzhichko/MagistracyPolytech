package com.example.magistracypolytech;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class MagistracyPolytechApplicationTests extends AbstractContainerBaseTest {
    @Test
    public void start(){
        log.info("Start of testing");
    }
}
