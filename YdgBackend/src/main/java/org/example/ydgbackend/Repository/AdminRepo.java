package org.example.ydgbackend.Repository;

import org.example.ydgbackend.Entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepo extends JpaRepository<Admin,Long> {
    Admin findByEmail(String email);
}
