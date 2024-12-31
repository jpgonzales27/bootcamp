package com.juan_pablo.adopcion_mascotas.service;

import com.juan_pablo.adopcion_mascotas.persistence.entity.User;
import com.juan_pablo.adopcion_mascotas.persistence.specifications.searchCriteria.UserSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    Page<User> findAllUsers(UserSearchCriteria userSearchCriteria, Pageable pageable);
    User findUserById(Long id );
    User saveUser(User user );
    User updateUserById( Long id, User user );
    void deleteUserById( Long id );
}
