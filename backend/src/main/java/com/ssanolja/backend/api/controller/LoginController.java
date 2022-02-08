package com.ssanolja.backend.api.controller;

import com.ssanolja.backend.db.entity.User;
import com.ssanolja.backend.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/postTest")
    public String  inputKey3(@RequestBody User user) {

        User inputUser = userRepository.findByUserEmail(user.getUserEmail());
        if(inputUser == null){
            inputUser = User.builder()
                    .userEmail(user.getUserEmail())
                    .userNickname(user.getUserNickname())
                    .build();
            userRepository.save(user);
            System.out.println("포스트 테스트 저장됨");
        }else {
            System.out.println("이미 회원가입 되어 있습니다.");
        }
        return "ok";
    }

    @PostMapping("/sendUser")
    public User senduser(@RequestBody String userEmail){
        User user = userRepository.findByUserEmail(userEmail);
        return user;
    }
}
