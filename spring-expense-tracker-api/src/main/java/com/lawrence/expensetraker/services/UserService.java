package com.lawrence.expensetraker.services;

import com.lawrence.expensetraker.dto.UserDto;
import com.lawrence.expensetraker.entity.User;

public interface UserService {
    User createUser(UserDto user);
    User readUser();
    User updateUser();
    void deleteUser();
    User getLoggedInUser();
}

