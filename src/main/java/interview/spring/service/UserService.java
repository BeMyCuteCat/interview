package interview.spring.service;

import interview.spring.CustomException.UserNotExistException;
import interview.spring.model.User;
import interview.spring.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserRepository repository;

    public List<User> getUserInfo(String name) throws UserNotExistException {
        log.info("getUserInfo() name : {}", name);
        Optional<List<User>> optional = Optional.ofNullable(repository.findByName(name));

        if (!optional.isPresent()) throw new UserNotExistException();

        return optional.get();
    }
}
