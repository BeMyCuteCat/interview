package interview.spring.controller;

import interview.spring.CustomException.UserNotExistException;
import interview.spring.controller.DTO.Model;
import interview.spring.controller.DTO.UserResponse;
import interview.spring.model.User;
import interview.spring.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;


@Controller
@Slf4j
public class UserController {

    private static final String USER_NOT_EXIST = "User name not exist!";

    @Autowired
    UserService userService;

    @GetMapping(path = "/user/{name}", headers = "content-type=application/json")
    @ResponseBody
    public ResponseEntity<UserResponse> getUserInfo(@PathVariable @NotEmpty String name) {
        try {
            List<User> users = userService.getUserInfo(name);

            UserResponse userResponse = new UserResponse();
            List<Model> models = new LinkedList<>();
            userResponse.setModels(models);
            userResponse.setName(name);
            for (User user : users) {
                Model model = new Model(user.getModel(), user.getNum(), user.getCreateTime());
                userResponse.getModels().add(model);
            }
            return new ResponseEntity(userResponse, HttpStatus.OK);

        } catch (UserNotExistException e) {
            return new ResponseEntity(USER_NOT_EXIST, HttpStatus.BAD_REQUEST);
        }
    }


}
