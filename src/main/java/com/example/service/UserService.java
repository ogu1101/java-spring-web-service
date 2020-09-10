package com.example.service;

import com.example.entity.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Userのserviceクラス.
 */
@Service
@Transactional
public class UserService {
    /**
     * UserRepositoryオブジェクト.
     */
    @Autowired
    private final UserRepository userRepository;

    /**
     * コンストラクタ.
     *
     * @param ur UserRepositoryオブジェクト
     */
    public UserService(final UserRepository ur) {
        this.userRepository = ur;
    }

    /**
     * ユーザー情報をDBに登録する.
     *
     * @param user ユーザー情報
     * @return DBに登録したユーザー情報
     */
    public User saveUser(final User user) {
        return userRepository.save(user);
    }

    /**
     * 単一のユーザー情報をDBから取得する.
     *
     * @param id ユーザーID
     * @return DBから取得した単一のユーザー情報
     */
    public Optional<User> findUser(final int id) {
        return userRepository.findById(id);
    }

    /**
     * すべてのユーザー情報をDBから取得する.
     *
     * @return DBから取得したすべてのユーザー情報
     */
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * DBのユーザー情報を更新する.
     *
     * @param user ユーザー情報
     * @return 更新後のユーザー情報
     */
    public User updateUser(final User user) {
        return userRepository.save(user);
    }

    /**
     * ユーザー情報をDBから削除する.
     *
     * @param id ユーザーID
     */
    public void deleteUser(final int id) {
        userRepository.deleteById(id);
    }
}
