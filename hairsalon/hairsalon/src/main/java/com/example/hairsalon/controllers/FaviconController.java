package com.example.hairsalon.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FaviconController {

    @GetMapping("/favicon.ico")
    public void favicon() {
        // Nothing here, browser will not get a favicon
    }
}