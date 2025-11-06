package com.devops.projeto_ac2.service;

import java.util.List;
import java.util.stream.Collectors;

import com.devops.projeto_ac2.entity.User_RA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devops.projeto_ac2.dto.UserDTO;
import com.devops.projeto_ac2.entity.User;
import com.devops.projeto_ac2.repository.User_Repository;

@Service
public class UserService {

    @Autowired
    private User_Repository user_repository;

    public UserDTO save(UserDTO userDTO)
    {
        User user = User.builder().nome(userDTO.getNome()).User_RA(new User_RA(userDTO.getRa())).build();
        User savedUser = user_repository.save(user);
        return mapToDTO(savedUser);
    }

    public User returnUser(Long id)
    {
        return user_repository.findById(id).orElse(null);
    }


    public List<UserDTO> findAll()
    {
        return user_repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private UserDTO mapToDTO(User user)
    {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setNome(user.getNome());
        userDTO.setRa(user.getUser_RA().getRegistroMatricula());
        return userDTO;
    }



}
