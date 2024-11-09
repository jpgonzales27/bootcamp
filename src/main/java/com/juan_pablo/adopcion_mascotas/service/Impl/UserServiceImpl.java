package com.juan_pablo.adopcion_mascotas.service.Impl;

import com.juan_pablo.adopcion_mascotas.persistence.entity.Pet;
import com.juan_pablo.adopcion_mascotas.persistence.entity.User;
import com.juan_pablo.adopcion_mascotas.persistence.repository.UserCrudRepository;
import com.juan_pablo.adopcion_mascotas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserCrudRepository userCrudRepository;

    @Override
    public List<User> findAllUsers() {
        return userCrudRepository.findAll();
    }

    @Override
    public User findUserById(Long id) {
        return userCrudRepository.findById(id).orElseThrow();
    }

    @Override
    public User saveUser(User user) {
        return userCrudRepository.save(user);
    }

    @Override
    public User updateUserById(Long id, User user) {
        User oldUser = userCrudRepository.findById(id).orElseThrow();;
        oldUser.setName(user.getName());
        oldUser.setEmail(user.getEmail());
        oldUser.setPhone(user.getPhone());
        oldUser.setAdoptions(user.getAdoptions());
        return userCrudRepository.save(oldUser);
    }

    @Override
    public void deleteUserById(Long id) {
        User exist = userCrudRepository.findById(id).orElseThrow();
        userCrudRepository.delete(exist);
    }
}
