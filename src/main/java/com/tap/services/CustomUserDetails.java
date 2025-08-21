package com.tap.services;

import com.tap.entities.User;
import com.tap.entities.Student;
import com.tap.entities.Instructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public boolean isStudent() {
        return user instanceof Student;
    }

    public boolean isInstructor() {
        return user instanceof Instructor;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = isStudent() ? "ROLE_STUDENT"
                : isInstructor() ? "ROLE_INSTRUCTOR"
                : "ROLE_USER";
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override public String getPassword() { return user.getPassword(); }
    @Override public String getUsername() { return user.getUsername(); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}