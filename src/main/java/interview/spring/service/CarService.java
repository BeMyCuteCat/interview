package interview.spring.service;

import interview.spring.CustomException.ModelNotExistException;
import interview.spring.CustomException.NumNotEnoughException;
import interview.spring.model.Car;
import interview.spring.model.User;
import interview.spring.repository.CarRepository;
import interview.spring.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;


    public void updateCar(String name, int num, String model) throws ModelNotExistException, NumNotEnoughException {
        log.info("model : {}, num : {}", model, num);
        //防止查询不存在造成空指针
        Optional<Car> optional = Optional.ofNullable(carRepository.findByModel(model));

        //数据库不存在需要的车型，打印错误日志
        if (!optional.isPresent()) throw new ModelNotExistException();

        Car car = optional.get();
        int remain;
        if ((remain = car.getRemain() - num) < 0) {
            throw new NumNotEnoughException();
        }
        car.setRemain(remain);
        carRepository.save(car);

        User user = new User();
        user.setName(name);
        user.setModel(model);
        user.setNum(num);
        userRepository.save(user);
        log.debug("there is now {} {} cars", remain, model);
    }

    public List<Car> getAllCar() {
        log.info("step into getAllCar()");
        return carRepository.findAll(Sort.by("id"));
    }

    public Car getCar(String model) {
        return carRepository.findByModel(model);
    }


}
