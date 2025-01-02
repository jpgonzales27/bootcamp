package com.juan_pablo.adopcion_mascotas.service.Impl;

import com.juan_pablo.adopcion_mascotas.exception.ObjectNotFoundException;
import com.juan_pablo.adopcion_mascotas.persistence.entity.Pet;
import com.juan_pablo.adopcion_mascotas.persistence.entity.Role;
import com.juan_pablo.adopcion_mascotas.persistence.entity.User;
import com.juan_pablo.adopcion_mascotas.persistence.repository.RoleCrudRepository;
import com.juan_pablo.adopcion_mascotas.persistence.repository.UserCrudRepository;
import com.juan_pablo.adopcion_mascotas.persistence.specifications.PetSpecifications;
import com.juan_pablo.adopcion_mascotas.persistence.specifications.UserSpecifications;
import com.juan_pablo.adopcion_mascotas.persistence.specifications.searchCriteria.UserSearchCriteria;
import com.juan_pablo.adopcion_mascotas.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserCrudRepository userCrudRepository;
    private final RoleCrudRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<User> findAllUsers(UserSearchCriteria userSearchCriteria, Pageable pageable) {
        UserSpecifications petSpecifications = new UserSpecifications(userSearchCriteria);
        return userCrudRepository.findAll(petSpecifications,pageable);
    }

    @Override
    public User findUserById(Long id) {
        return userCrudRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("[User: " + Long.toString(id) + "]"));
    }


    @Override
    public User saveUser(User user) {
        // Check if user roles are set
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            throw new IllegalArgumentException("User must have at least one role");
        }

        Boolean roleExist = roleRepository.findByName(user.getRoles().iterator().next().getName());
        if (roleExist) {
            userCrudRepository.save(user);
        } else {
            throw new ObjectNotFoundException("[Role: " + user.getRoles().iterator().next().getName() + "]");
        }

        for (Role userRole : user.getRoles()) {
            userRole.getUsers().add(user);
        }

        return user;
    }

    @Override
    public User updateUserById(Long id, User user) {
        User oldUser = this.findUserById(id);
        oldUser.setName(user.getName());
        oldUser.setEmail(user.getEmail());
        oldUser.setPassword(user.getPassword());
        oldUser.setPhone(user.getPhone());
        oldUser.setAdoptions(user.getAdoptions());

        // Update user roles
        oldUser.getRoles().clear();
        for (Role role : user.getRoles()) {
            oldUser.getRoles().add(role);
            role.getUsers().add(oldUser);
        }

        return userCrudRepository.save(oldUser);
    }

    @Override
    public void deleteUserById(Long id) {
        User exist = this.findUserById(id);;
        userCrudRepository.delete(exist);
    }
}
