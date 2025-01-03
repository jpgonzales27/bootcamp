package com.juan_pablo.adopcion_mascotas.service.Impl;

import com.juan_pablo.adopcion_mascotas.exception.ObjectNotFoundException;
import com.juan_pablo.adopcion_mascotas.persistence.entity.Role;
import com.juan_pablo.adopcion_mascotas.persistence.entity.User;
import com.juan_pablo.adopcion_mascotas.persistence.repository.RoleCrudRepository;
import com.juan_pablo.adopcion_mascotas.persistence.repository.UserCrudRepository;
import com.juan_pablo.adopcion_mascotas.persistence.specifications.UserSpecifications;
import com.juan_pablo.adopcion_mascotas.persistence.specifications.searchCriteria.UserSearchCriteria;
import com.juan_pablo.adopcion_mascotas.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
        List<Role> roles = user.getRoles();
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("At least one role must be provided");
        }

        List<Role> validatedRoles = roles.stream()
                .map(role -> roleRepository.findByName(role.getName())
                        .orElseThrow(() -> new IllegalArgumentException("Role not found: " + role.getName())))
                .toList();

        user.setRoles(validatedRoles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userCrudRepository.save(user);
    }

    @Override
    public User updateUserById(Long id, User user) {
        User oldUser = this.findUserById(id);
        oldUser.setName(user.getName());
        oldUser.setEmail(user.getEmail());
        oldUser.setPhone(user.getPhone());
        oldUser.setAdoptions(user.getAdoptions());

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            oldUser.getRoles().clear();
            List<Role> validatedRoles = user.getRoles().stream()
                    .map(role -> roleRepository.findByName(role.getName())
                            .orElseThrow(() -> new IllegalArgumentException("Role not found: " + role.getName())))
                    .toList();
            oldUser.getRoles().addAll(validatedRoles);
        }

        return userCrudRepository.save(oldUser);
    }

    @Override
    public void deleteUserById(Long id) {
        User exist = this.findUserById(id);;
        userCrudRepository.delete(exist);
    }
}
