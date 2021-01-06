package com.restarauntvote.restarauntservice.service;


import com.restarauntvote.restarauntservice.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User save(User book);

    User update(User book);

    void delete(User book);

    void get(long id);

    List<User> getAll();
}
