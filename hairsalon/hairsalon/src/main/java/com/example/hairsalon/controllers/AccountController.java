package com.example.hairsalon.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${app.api.version.v1}/account")
@RequiredArgsConstructor
public class AccountController {

}
