package com.FK.Notes.Secrets.SecurityConfig;

import com.FK.Notes.Secrets.Models.User;
import com.FK.Notes.Secrets.Repo.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {

    @Autowired
    UserRepo userRepository;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username).orElseThrow(()-> new UsernameNotFoundException("User Not Found with given username"));
       return UserDetailImpl.build(user);
    }
}


/*
 * @Transactional marks a method or class as transactional.
 *
 * All database operations inside a @Transactional method are executed within a transaction.
 * If the method completes successfully, changes are committed to the database.
 * If a RuntimeException (unchecked exception) occurs, all changes are rolled back to maintain data consistency.
 *
 * Optional: You can configure rollback for checked exceptions using rollbackFor attribute.
 */