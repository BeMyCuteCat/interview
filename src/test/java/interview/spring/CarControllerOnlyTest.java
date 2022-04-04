package interview.spring;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import interview.spring.CustomException.ModelNotExistException;
import interview.spring.CustomException.NumNotEnoughException;
import interview.spring.model.Car;
import interview.spring.service.CarService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.LinkedList;
import java.util.List;


/**
 * 仅对Controller 层测试，Service 层 mock 模拟
 */
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class CarControllerOnlyTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private CarService service;

    @BeforeEach
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getCarNormal() throws Exception {

        log.info("******getCarControllerOnly********start****");

        String jsonRequest = "{\"userName\":\"test\",\"model\":\"BMW 650\",\"num\":1}";

        this.mockMvc.perform(post("/car").content(jsonRequest).contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK! You now have the car."));
    }

    @Test
    public void getCarInvalidNumParameter() throws Exception {
        log.info("****************getCarInvalidParameter*****************");

        String jsonRequest = "{\"model\":\"Toyota Camry\",\"num\":1000}";

        this.mockMvc.perform(post("/car").content(jsonRequest).contentType("application/json"))
                .andExpect(status().is(400))
                .andExpect(content().string("Please fill in valid parameter! Now only Toyota Camry or BMW 650 model, and you can order 1-100 cars one time."));

    }

    @Test
    public void getCarNoModel() throws Exception {
        log.info("****************getCarNoModel*****************");
        doThrow(new ModelNotExistException()).when(service).updateCar("test", 1, "Not Exist");

        String jsonRequest = "{\"userName\":\"test\",\"model\":\"Not Exist\",\"num\":1}";
        this.mockMvc.perform(post("/car").content(jsonRequest).contentType("application/json"))
                .andExpect(status().is(400))
                .andExpect(content().string("Please fill in valid parameter! Now only Toyota Camry or BMW 650 model, and you can order 1-100 cars one time."));
    }

    @Test
    public void getCarNotEnoughCar() throws Exception {
        log.info("****************getCarNotEnoughCar*****************");
        doThrow(new NumNotEnoughException()).when(service).updateCar("test", 3, "BMW 650");
        String jsonRequest = "{\"userName\":\"test\",\"model\":\"BMW 650\",\"num\":3}";
        this.mockMvc.perform(post("/car").content(jsonRequest).contentType("application/json"))
                .andExpect(status().is(400))
                .andExpect(content().string("Sorry! There are not enough cars"));
    }

    @Test
    public void getAll() throws Exception {
        System.out.println("****************getAll*****************");

        List<Car> res = new LinkedList<>();
        res.add(new Car(1L,"Toyota Camry",  2, 2));
        res.add(new Car(2L,"BMW 650", 2, 2));
        when(service.getAllCar()).thenReturn(res);

        this.mockMvc.perform(get("/cars"))
                .andExpect(status().is(200))
                .andDo(new ResultHandler() {
                    @Override
                    public void handle(MvcResult mvcResult) throws Exception {
                        System.out.println(mvcResult.getResponse().getContentAsString());
                    }
                })
                .andExpect(content().string("[{\"model\":\"Toyota Camry\",\"total\":2,\"remain\":2},{\"model\":\"BMW 650\",\"total\":2,\"remain\":2}]"));
    }


}
