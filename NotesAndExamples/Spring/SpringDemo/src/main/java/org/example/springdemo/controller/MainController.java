package org.example.springdemo.controller;

import org.example.springdemo.pojos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/demo") // URL's start
public class MainController {
    @Autowired // Get User repository
    private UserRepository userRepository;

    @PostMapping(path = "/add")
    public @ResponseBody String addUser(@RequestParam("name") String name, @RequestParam("email") String email) {
        User user = new User(name, email);
        user.setName(name);
        user.setEmail(email);
        return Use

    }
}
