package com.neoflex.creditconveyor.com.neoflex.creditconveyor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerTest {

    @GetMapping("/h1")
    public String s() {
        return "str";
    }
}
