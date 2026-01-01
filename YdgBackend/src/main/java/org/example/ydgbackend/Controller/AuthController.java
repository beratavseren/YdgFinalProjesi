package org.example.ydgbackend.Controller;

import org.example.ydgbackend.Entity.Admin;
import org.example.ydgbackend.Entity.WerehouseWorker;
import org.example.ydgbackend.Repository.AdminRepo;
import org.example.ydgbackend.Repository.WerehouseWorkerRepo;
import org.example.ydgbackend.Security.BlackListService;
import org.example.ydgbackend.Security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AdminRepo adminRepo;
    private final WerehouseWorkerRepo workerRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final BlackListService blackListService;

    public AuthController(AdminRepo adminRepo, WerehouseWorkerRepo workerRepo, JwtService jwtService,
                          PasswordEncoder passwordEncoder, BlackListService blackListService) {
        this.adminRepo = adminRepo;
        this.workerRepo = workerRepo;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.blackListService = blackListService;
    }

    public static class LoginRequest {
        public String email;
        public String password;
        public String getEmail() { return email; }
        public String getPassword() { return password; }
        public void setEmail(String email) { this.email = email; }
        public void setPassword(String password) { this.password = password; }
    }
    public static class LoginResponse {
        public String token;
        public String role;
        public LoginResponse(String token, String role) { this.token = token; this.role = role; }
        public String getToken() { return token; }
        public String getRole() { return role; }
        public void setToken(String token) { this.token = token; }
        public void setRole(String role) { this.role = role; }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Admin admin = adminRepo.findByEmail(request.getEmail());

        if (admin != null && passwordMatches(request.getPassword(), admin.getPassword())) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", "ADMIN");
            claims.put("email", admin.getEmail());
            String token = jwtService.generateToken(admin.getEmail(), claims);
            return ResponseEntity.ok(new LoginResponse(token, "ADMIN"));
        }

        WerehouseWorker worker = workerRepo.findByEmail(request.getEmail());

        if (worker != null && passwordMatches(request.getPassword(), worker.getPassword())) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", "WORKER");
            claims.put("email", worker.getEmail());
            if (worker.getWerehouse() != null) {
                claims.put("werehouseId", worker.getWerehouse().getWerehouseId());
            }
            String token = jwtService.generateToken(worker.getEmail(), claims);
            return ResponseEntity.ok(new LoginResponse(token, "WORKER"));
        }

        return ResponseEntity.status(401).body("Invalid credentials");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = getCurrentToken(request);

        if (token != null) {
            blackListService.blackListToken(token);
            return ResponseEntity.ok("Logged out successfully");
        }
        return ResponseEntity.badRequest().body("No token found");
    }

    @GetMapping("/hello")
    public ResponseEntity<?> hello() {
        return ResponseEntity.ok("Hello");
    }

    private String getCurrentToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private boolean passwordMatches(String raw, String stored) {

        if (stored == null) return false;

        if (stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$")) {
            return passwordEncoder.matches(raw, stored);
        }

        return stored.equals(raw);
    }
}
