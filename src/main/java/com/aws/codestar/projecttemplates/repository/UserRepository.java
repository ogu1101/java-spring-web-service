package com.aws.codestar.projecttemplates.repository;

import com.aws.codestar.projecttemplates.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserのRepositoryクラス.
 */
public interface UserRepository extends JpaRepository<User, Integer> {
}
