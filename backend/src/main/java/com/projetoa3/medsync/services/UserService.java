package com.projetoa3.medsync.services;

import com.projetoa3.medsync.domain.user.User;
import com.projetoa3.medsync.exceptions.EmailAlreadyExistsException;
import com.projetoa3.medsync.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void save(User user) {
        if(userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email já cadastrado");
        }
        String hashedPassword = passwordEncoder.encode(user.getSenhaHash());
        user.setSenhaHash(hashedPassword);

        this.userRepository.save(user);
    }

    public List<User> findAll() {
        return this.userRepository.findAll();
    }


}
