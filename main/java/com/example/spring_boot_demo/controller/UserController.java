package com.example.spring_boot_demo.controller;


import com.example.spring_boot_demo.exception.ResourceNotFundException;
import com.example.spring_boot_demo.model.User;
import com.example.spring_boot_demo.repository.UserRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger= LoggerFactory.getLogger(UserController.class);
    //    @GetMapping
//    public String getUsers(){
//        return "Hello API";
//    }
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<User> getAllUsers() {
//        return Arrays.asList(new User(1L,"John","john@gmail.com"),
//                new User(2L,"Joe","joe@gmail.com"));
        return userRepository.findAll();
    }


    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        logger.info("Getting userdetails for id: "+id);
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFundException("user Not Found With Id " + id));
    }
//    @PutMapping("/{id}")
//    public User updateUser(@PathVariable Long id, @RequestBody User userDetails){
//        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFundException("User not Found with Id "+id));
//        user.setName(userDetails.getName());
//        user.setEmail(userDetails.getEmail());
//        return userRepository
    
    @PostMapping("/createUser")
    public User createUser(@Valid @RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

}
