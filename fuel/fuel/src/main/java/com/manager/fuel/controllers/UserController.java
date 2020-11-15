package com.manager.fuel.controllers;

import com.manager.fuel.entities.User;
import com.manager.fuel.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public Iterable<User> list() {
        return userRepository.findAll();
    }

    @RequestMapping(value = "/new", method =  RequestMethod.POST)
    public User save(@RequestBody User user)
    {
        return userRepository.save(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<User> getById(@PathVariable(value = "id") long id)
    {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent())
            return new ResponseEntity<User>(user.get(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/{id}", method =  RequestMethod.PUT)
    public ResponseEntity<User> update(@PathVariable(value = "id") long id, @RequestBody User newUser)
    {
        Optional<User> user = userRepository.findById(id);

        if(user != null){
            User lUser = user.get();

            if (newUser.getName() != null) {
               lUser.setName(newUser.getName());
            }

            if (newUser.getLogin() != null) {
                lUser.setLogin(newUser.getLogin());
            }

            if (newUser.getPassword() != null) {
                lUser.setPassword(newUser.getPassword());
            }

            userRepository.save(lUser);

            return new ResponseEntity<User>(lUser, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> delete(@PathVariable(value = "id") long id)
    {
        Optional<User> user = userRepository.findById(id);

        if(user.isPresent()){
            userRepository.delete(user.get());

            return new ResponseEntity<>(HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
