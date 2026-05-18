package com.buyani.buyaniserver.controller;

import com.buyani.buyaniserver.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    // Called automatically after /create-cx in your UserService (see note below)
    // Or you can call it manually: POST /api/user/send-verification
    @PostMapping("/send-verification")
    public ResponseEntity<?> sendVerification(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
        }
        try {
            emailVerificationService.sendVerificationCode(email);
            return ResponseEntity.ok(Map.of("message", "Verification code sent"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("message", "Failed to send email: " + e.getMessage()));
        }
    }

    // POST /api/user/resend-verification
    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
        }
        try {
            emailVerificationService.sendVerificationCode(email);
            return ResponseEntity.ok(Map.of("message", "Verification code resent"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("message", "Failed to resend email: " + e.getMessage()));
        }
    }

    // POST /api/user/verify-email
    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String code = body.get("code");

        if (email == null || code == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email and code are required"));
        }

        boolean valid = emailVerificationService.verifyCode(email, code);
        if (!valid) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid or expired code"));
        }

        // ✅ Optional: mark the user as verified in your DB here
        // userService.markEmailVerified(email);

        return ResponseEntity.ok(Map.of("message", "Email verified successfully"));
    }
}
