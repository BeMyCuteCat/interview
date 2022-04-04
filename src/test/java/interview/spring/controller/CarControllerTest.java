package interview.spring.controller;

import interview.spring.controller.DTO.CarRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class CarControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Sql(scripts = "/data.sql")
    public void getCarNormal() {
        log.info("****************getCarNormal*****************");

        CarRequest request = new CarRequest("test", "Toyota Camry", 1);
        ResponseEntity<String> res = this.restTemplate.postForEntity("http://localhost:" + port + "/car", request, String.class);
        assertThat(res.getStatusCodeValue()).isEqualTo(200);
        assertThat(res.getBody()).isEqualTo("OK! You now have the car.");

        ResponseEntity<List> cars = this.restTemplate.getForEntity("http://localhost:" + port + "/cars", List.class);
        assertThat(cars.getBody().toString()).isEqualTo("[{model=Toyota Camry, total=2, remain=1}, {model=BMW 650, total=2, remain=2}]");
    }

    @Test
    public void getCarInvalidNumParameter() {
        log.info("****************getCarInvalidParameter*****************");
        CarRequest request = new CarRequest(null,"ee", 1000);
        ResponseEntity<String> res = this.restTemplate.postForEntity("http://localhost:" + port + "/car", request, String.class);
        assertThat(res.getStatusCodeValue()).isEqualTo(400);
        assertThat(res.getBody()).isEqualTo("Please fill in valid parameter! Now only Toyota Camry or BMW 650 model, and you can order 1-100 cars one time.");

    }

    @Test
    public void getCarNoModel() {
        log.info("****************getCarNoModel*****************");
        CarRequest request = new CarRequest(null,"ee", 1);
        ResponseEntity<String> res = this.restTemplate.postForEntity("http://localhost:" + port + "/car", request, String.class);
        assertThat(res.getStatusCodeValue()).isEqualTo(400);
        assertThat(res.getBody()).isEqualTo("Please fill in valid parameter! Now only Toyota Camry or BMW 650 model, and you can order 1-100 cars one time.");

    }

    @Test
    @Sql("/data.sql")
    public void getCarNotEnoughCar() {
        log.info("****************getCarNotEnoughCar*****************");
        CarRequest request = new CarRequest("test","Toyota Camry", 10);
        ResponseEntity<String> res = this.restTemplate.postForEntity("http://localhost:" + port + "/car", request, String.class);
        assertThat(res.getStatusCodeValue()).isEqualTo(400);
        assertThat(res.getBody()).isEqualTo("Sorry! There are not enough cars");

    }

    @Test
    @Sql("/data.sql")
    public void getAll() {
        log.info("****************getAll*****************");
        ResponseEntity<List> list = this.restTemplate.getForEntity("http://localhost:" + port + "/cars", List.class);

        assertThat(list.getBody().toString()).isEqualTo("[{model=Toyota Camry, total=2, remain=2}, {model=BMW 650, total=2, remain=2}]");
    }

}