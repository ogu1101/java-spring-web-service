package com.example.repository;

import com.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserのRepositoryインターフェース.
 */
public interface UserRepository extends JpaRepository<User, Integer> {
}
