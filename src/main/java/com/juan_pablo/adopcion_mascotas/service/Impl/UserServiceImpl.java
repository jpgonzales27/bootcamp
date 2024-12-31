package com.juan_pablo.adopcion_mascotas.service.Impl;

import com.juan_pablo.adopcion_mascotas.exception.ObjectNotFoundException;
import com.juan_pablo.adopcion_mascotas.persistence.entity.Pet;
import com.juan_pablo.adopcion_mascotas.persistence.entity.User;
import com.juan_pablo.adopcion_mascotas.persistence.repository.UserCrudRepository;
import com.juan_pablo.adopcion_mascotas.persistence.specifications.PetSpecifications;
import com.juan_pablo.adopcion_mascotas.persistence.specifications.UserSpecifications;
import com.juan_pablo.adopcion_mascotas.persistence.specifications.searchCriteria.UserSearchCriteria;
import com.juan_pablo.adopcion_mascotas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserCrudRepository userCrudRepository;

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
        return userCrudRepository.save(user);
    }

    @Override
    public User updateUserById(Long id, User user) {
        User oldUser = this.findUserById(id);
        oldUser.setName(user.getName());
        oldUser.setEmail(user.getEmail());
        oldUser.setPhone(user.getPhone());
        oldUser.setAdoptions(user.getAdoptions());
        return userCrudRepository.save(oldUser);
    }

    @Override
    public void deleteUserById(Long id) {
        User exist = this.findUserById(id);;
        userCrudRepository.delete(exist);
    }
}
