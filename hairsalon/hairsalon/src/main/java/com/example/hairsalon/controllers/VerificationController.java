package com.example.hairsalon.controllers;

import com.example.hairsalon.services.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class VerificationController {

    @Autowired
    IAccountService accountService;


    // Serve the verify page
    @GetMapping("/api/v1/verify/verify-page")
    public String showVerifyPage(
            @RequestParam("userId") Long userId,
            @RequestParam("token") String token,
            Model model
    ) {
        model.addAttribute("userId", userId);
        model.addAttribute("token", token);
        return "verify"; // This refers to verify.html (Thymeleaf template)
    }

    // Handle verification logic and redirect to success
    @PostMapping("/api/v1/verify")
    public String verifyAccount(
            @RequestParam Long userId,
            @RequestParam String token,
            Model model
    ) {
        try {
            accountService.verify(userId, token);
            return "redirect:/api/v1/verify/success"; // Redirect to success page
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Verification failed. Please try again.");
            return "verify";
        }
    }

    // Success page endpoint
    @GetMapping("/api/v1/verify/success")
    public String showSuccessPage() {
        return "success"; // Render the success.html page
    }
}
