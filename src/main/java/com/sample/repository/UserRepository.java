package com.sample.repository;

import com.sample.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserのRepositoryクラス.
 */
public interface UserRepository extends JpaRepository<User, Integer> {
}
