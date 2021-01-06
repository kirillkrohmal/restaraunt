package com.restarauntvote.restarauntservice.repository;


import com.restarauntvote.restarauntservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl {
    @Autowired
    UserRepository userRepository;

    void save(User user) {
        userRepository.save(user);
    }

    void findOne(long id) {
        userRepository.findById(id);
    }

    public void findAll() {
        userRepository.findAll();
    }

    public void remove(User user) {
        userRepository.delete(user);
    }

    void update(User user) {
        userRepository.save(user);
    }
}
