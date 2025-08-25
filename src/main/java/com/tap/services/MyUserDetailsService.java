package com.tap.services;

import com.tap.entities.User;
import com.tap.entities.UserPrincipal;
import com.tap.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//            Optional<User> user=userRepository.findByUsername(username);
//
//            if(user==null){
//                System.out.println("User not found");
//                throw new UsernameNotFoundException("user not found");
//            }
//
//            return new UserPrincipal(user);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserPrincipal(user);

    }
}


