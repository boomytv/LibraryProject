package com.proj.libraryproject.utils;

import com.proj.libraryproject.web.models.User;
import com.proj.libraryproject.web.repository.UserRepository;
import com.proj.libraryproject.web.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository repository;

    public User getUserByEmail(String email){
        return repository.findByEmailEquals(email);
    }

    public String getPasswordById(String id){
        Optional<User> user = repository.findById(id);
        if(user.isEmpty()){
            return null;
        }
        return user.get().getPassword();
    }
    public Optional<User> getUserById(String id) {
        return repository.findById(id);
    }
    public User save(User user){
        return repository.saveAndFlush(user);
    }

    public Optional<User> getCurrentUser(){
        return repository.findById(JwtUtils.getId());
    }
}
