package com.FK.Notes.Secrets;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
@GetMapping("/Hello")
    public String hello(){
    return "Hello!";
    }
}
