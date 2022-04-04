package interview.spring.controller;

import interview.spring.CustomException.ModelNotExistException;
import interview.spring.CustomException.NumNotEnoughException;
import interview.spring.controller.DTO.CarRequest;
import interview.spring.controller.DTO.CarResponse;
import interview.spring.model.Car;
import interview.spring.service.CarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
@Slf4j
public class CarController {
    @Autowired
    private CarService carService;

    private final static String INVALID_PARAMETER = "Please fill in valid parameter! Now only Toyota Camry or BMW 650 model, and you can order 1-100 cars one time.";
    private final static String HAVE_CAR = "OK! You now have the car.";
    private final static String NOT_ENOUGH = "Sorry! There are not enough cars";


    @PostMapping(path = "car", headers = "content-type=application/json")
    public ResponseEntity<String> postCar(@Valid @RequestBody CarRequest carRequest,
                                          BindingResult result) {
        if (result.hasErrors()) {
            // 简单处理一下
            log.warn("Binding Errors: {}", result);

            return new ResponseEntity<>(INVALID_PARAMETER, HttpStatus.BAD_REQUEST);
        }

        try {
            carService.updateCar(carRequest.getUserName(), carRequest.getNum(), carRequest.getModel());

            return new ResponseEntity<>(HAVE_CAR, HttpStatus.OK);
        } catch (ModelNotExistException e) {
            log.error("Database don't have {} model, Please check consistency of data.", carRequest.getModel());
            return new ResponseEntity<>(NOT_ENOUGH, HttpStatus.BAD_REQUEST);
        } catch (NumNotEnoughException e) {
            return new ResponseEntity<>(NOT_ENOUGH, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "cars")
    public ResponseEntity<List<CarResponse>> getAllCar() {
        List<Car> list = carService.getAllCar();
        List<CarResponse> res = new ArrayList();
        for (Car car : list) {
            CarResponse temp = new CarResponse(car.getModel(), car.getTotal(), car.getRemain());
            res.add(temp);
        }
        return new ResponseEntity(res, HttpStatus.OK);
    }

}
