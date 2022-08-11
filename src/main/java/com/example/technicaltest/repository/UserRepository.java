package com.example.technicaltest.repository;

import com.example.technicaltest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{
    User findByUsername(String username);
}
