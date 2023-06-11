package com.lawrence.expensetraker.services.impl;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.lawrence.expensetraker.dto.UserDto;
import com.lawrence.expensetraker.entity.User;
import com.lawrence.expensetraker.exceptions.ItemExistsException;
import com.lawrence.expensetraker.exceptions.ResourceNotFoundException;
import com.lawrence.expensetraker.repository.UserRepository;
import com.lawrence.expensetraker.services.UserService;
import org.springframework.beans.BeanUtils;

public class UserServiceImpl implements UserService {
    private UserRepository userRepo;

    public UserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public User createUser(UserDto user) {
        if(userRepo.existsByEmail(user.getEmail())){
            throw new ItemExistsException("User is already register with the email: "+ user.getEmail());
        }
        User newUser = new User();
        BeanUtils.copyProperties(user, newUser);
        return userRepo.save(newUser);
    }

    @Override
    public User readUser() {
        Long userId = getLoggedInUser().getId();
        return userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found for the id: "+userId));
    }

    @Override
    public User updateUser(UserDto user) {
        User existingUser = readUser();
        existingUser.setName(user.getName() != null ? user.getName() : existingUser.getName());
        existingUser.setEmail(user.getEmail() != null ? user.getEmail() : existingUser.getEmail());
        existingUser.setPassword(user.getPassword() != null ? user.getPassword() : existingUser.getPassword());
        existingUser.setAge(user.getAge() != null ? user.getAge() : existingUser.getAge());
        return userRepo.save(existingUser);
    }

    @Override
    public void deleteUser() {
        User existingUser = readUser();
        userRepo.delete(existingUser);
    }

    @Override
    public User getLoggedInUser() {
        return null;
    }
}
