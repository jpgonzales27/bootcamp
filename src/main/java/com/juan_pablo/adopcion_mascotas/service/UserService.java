package com.juan_pablo.adopcion_mascotas.service;

import com.juan_pablo.adopcion_mascotas.persistence.entity.User;

import java.util.List;

public interface UserService {
    List<User> findAllUsers();
    User findUserById(Long id );
    User saveUser(User user );
    User updateUserById( Long id, User user );
    void deleteUserById( Long id );
}
