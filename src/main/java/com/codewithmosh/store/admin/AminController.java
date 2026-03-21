package com.codewithmosh.store.admin;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AminController {

    @GetMapping("/hello")
    public  String sayHEllo(){
        return "Hii";
    }
}
