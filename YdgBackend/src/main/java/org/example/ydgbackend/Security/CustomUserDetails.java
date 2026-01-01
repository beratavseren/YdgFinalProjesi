package org.example.ydgbackend.Security;

import org.example.ydgbackend.Entity.Admin;
import org.example.ydgbackend.Entity.WerehouseWorker;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class CustomUserDetails implements UserDetails {

    private final String email; // using email as username
    private final String password;
    private final String role; // ADMIN or WORKER
    private final boolean enabled;

    public CustomUserDetails(String email, String password, String role, boolean enabled) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
    }

    public static CustomUserDetails fromAdmin(Admin admin) {
        return new CustomUserDetails(
                admin.getEmail(),
                admin.getPassword(),
                "ADMIN",
                true
        );
    }

    public static CustomUserDetails fromWorker(WerehouseWorker worker) {
        return new CustomUserDetails(
                worker.getEmail(),
                worker.getPassword(),
                "WORKER",
                true
        );
    }

    public String getRole() {
        return role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomUserDetails that = (CustomUserDetails) o;
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
