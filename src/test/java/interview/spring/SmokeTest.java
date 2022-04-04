package interview.spring;

import static org.assertj.core.api.Assertions.assertThat;
import interview.spring.controller.CarController;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class SmokeTest {

    @Autowired
    private CarController controller;

    @Test
    public void contextLoads() throws Exception {
        log.info("smoke test");
        assertThat(controller).isNotNull();
    }
}