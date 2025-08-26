package com.FK.Notes.Secrets.Service;

import com.FK.Notes.Secrets.DTOs.UserDTO;
import com.FK.Notes.Secrets.Models.User;

import java.util.*;
public interface UserServiceInterface {
    List<User> getUsers();
    UserDTO getUserById(Long id);
    void updateUserRole(Long userId,String roleName);
}
