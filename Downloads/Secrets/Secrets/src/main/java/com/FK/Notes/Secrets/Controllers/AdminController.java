package com.FK.Notes.Secrets.Controllers;

import com.FK.Notes.Secrets.DTOs.UserDTO;
import com.FK.Notes.Secrets.Models.User;
import com.FK.Notes.Secrets.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    UserService userService;


    @GetMapping("/getusers")
    public ResponseEntity<List<User>> getAllUser(){
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }
    @PutMapping("/updateRole")
    public ResponseEntity<String> updateUserRole(@RequestParam Long userID, @RequestParam String roleName){
        userService.updateUserRole(userID,roleName);
        return new ResponseEntity<>("User role updated",HttpStatus.OK);

    }
    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("id")Long userId){
        return new ResponseEntity<>(userService.getUserById(userId),HttpStatus.OK);
    }






}
