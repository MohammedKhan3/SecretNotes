package com.FK.Notes.Secrets.Service;

import com.FK.Notes.Secrets.DTOs.UserDTO;
import com.FK.Notes.Secrets.Models.AppRole;
import com.FK.Notes.Secrets.Models.Role;
import com.FK.Notes.Secrets.Models.User;
import com.FK.Notes.Secrets.Repo.RoleRepository;
import com.FK.Notes.Secrets.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserServiceInterface{

    @Autowired
    UserRepo userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public List<User> getUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        return convertToDTO(user);

    }

    @Override
    public void updateUserRole(Long userId, String roleName) {
     User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));
     AppRole appRole = AppRole.valueOf(roleName);
     Role role = roleRepository.findByRoleName(appRole).orElseThrow(()-> new RuntimeException("Role not found"));
     user.setRole(role);
     userRepository.save(user);
    }

    private UserDTO convertToDTO(User user){
        return new UserDTO(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.isAccountNonLocked(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isEnabled(),
                user.getCredentialsExpiryDate(),
                user.getAccountExpiryDate(),
                user.getTwoFactorSecret(),
                user.isTwoFactorEnabled(),
                user.getSignUpMethod(),
                user.getRole(),
                user.getCreatedDate(),
                user.getUpdatedDate()
        );
    }

    public User findByUsername(String username) {
        Optional<User> user = userRepository.findByUserName(username);
        return user.orElseThrow(()->new RuntimeException("User not found with that username."));
    }
}
