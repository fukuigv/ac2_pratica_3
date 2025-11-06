package com.devops.projeto_ac2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.devops.projeto_ac2.entity.User;


public interface User_Repository extends JpaRepository<User, Long> {
}