package dev.bayun.id.controller;

import org.springframework.security.core.Authentication;

//@RestController
public class TestController {

//    @GetMapping(path = "/test")
    public String test(Authentication authentication) {
        return authentication.toString();
    }

}
