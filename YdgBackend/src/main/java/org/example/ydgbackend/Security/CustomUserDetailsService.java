package org.example.ydgbackend.Security;

import org.example.ydgbackend.Entity.Admin;
import org.example.ydgbackend.Entity.WerehouseWorker;
import org.example.ydgbackend.Repository.AdminRepo;
import org.example.ydgbackend.Repository.WerehouseWorkerRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepo adminRepo;
    private final WerehouseWorkerRepo workerRepo;

    public CustomUserDetailsService(AdminRepo adminRepo, WerehouseWorkerRepo workerRepo) {
        this.adminRepo = adminRepo;
        this.workerRepo = workerRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Admin admin = adminRepo.findByEmail(email);
        if (admin != null) {
            return CustomUserDetails.fromAdmin(admin);
        }
        WerehouseWorker worker = workerRepo.findByEmail(email);
        if (worker != null) {
            return CustomUserDetails.fromWorker(worker);
        }
        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}
