package com.projetoa3.medsync.services;

import com.projetoa3.medsync.domain.user.User;
import com.projetoa3.medsync.dtos.UpdateUserDTO;
import com.projetoa3.medsync.exceptions.EmailAlreadyExistsException;
import com.projetoa3.medsync.repositories.UserRepository;
import org.hibernate.sql.Update;
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

    public void update(long id, UpdateUserDTO dto) {
        userRepository.findById(id).map(user -> {
            user.setAlturaCm(dto.getAlturaCm());
            user.setPesoKg(dto.getPesoKg());
            user.setDoencasCronicas(dto.getDoencasCronicas());
            user.setAlergias(dto.getAlergias());
            user.setMedicamentos(dto.getMedicamentos());
            user.setContatoEmergenciaNome(dto.getContatoEmergenciaNome());
            user.setContatoEmergenciaTelefone(dto.getContatoEmergenciaTelefone());
            return userRepository.save(user);
        });
    }


}
