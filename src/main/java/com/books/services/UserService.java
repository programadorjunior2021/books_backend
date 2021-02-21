package com.books.services;

import com.books.entities.User;
import com.books.repositories.UserRepository;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService
{
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public User addUser(User user)
    {
        Optional<User> userFoundByEmail = userRepository.findUserByEmail(user.getEmail());
        if (userFoundByEmail.isPresent()) {
            throw new IllegalStateException("email_already_exists");
        }

        Argon2 argon2 = Argon2Factory.create();
        char[] password = user.getPassword().toCharArray();
        String hash = argon2.hash(10, 1024, 1, password);
        user.setPassword(hash);

        return userRepository.save(user);
    }
}