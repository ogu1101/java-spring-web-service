package com.sample.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.entity.User;
import com.sample.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * UserのControllerクラス.
 */
@RestController
@RequestMapping("user")
public class UserController {
    /**
     * Loggerオブジェクト.
     */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(UserController.class);
    /**
     * ObjectMapperオブジェクト.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();
    /**
     * UserServiceオブジェクト.
     */
    @Autowired
    private final UserService userService;

    /**
     * コンストラクタ.
     *
     * @param us UserServiceオブジェクト
     */
    public UserController(final UserService us) {
        this.userService = us;
    }

    /**
     * ユーザー情報をDBに登録する.
     *
     * @param user ユーザー情報
     * @return DBに登録したユーザー情報
     * @throws JsonProcessingException JSON文字列への変換時に発生する例外
     */
    @RequestMapping(method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public User postUser(
            @RequestBody @Validated final User user)
            throws JsonProcessingException {
        String methodName =
                Thread.currentThread().getStackTrace()[1].getMethodName();
        String request = objectMapper.writeValueAsString(user);
        startLog(methodName, request);

        User savedUser = userService.saveUser(user);

        String response = objectMapper.writeValueAsString(savedUser);
        endLog(methodName, response);
        return savedUser;
    }

    /**
     * 単一のユーザー情報をDBから取得する.
     *
     * @param id ユーザーID
     * @return DBから取得した単一のユーザー情報
     * @throws JsonProcessingException JSON文字列への変換時に発生する例外
     */
    @RequestMapping(
            value = "{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<User> getUser(@PathVariable("id") final int id)
            throws JsonProcessingException {
        String methodName =
                Thread.currentThread().getStackTrace()[1].getMethodName();
        Map<String, Integer> queryStrMap = new HashMap<>();
        queryStrMap.put("id", id);
        String request = objectMapper.writeValueAsString(queryStrMap);
        startLog(methodName, request);

        Optional<User> foundUser = userService.findUser(id);

        String response =
                objectMapper.writeValueAsString(foundUser.orElse(null));
        endLog(methodName, response);
        return foundUser;
    }

    /**
     * すべてのユーザー情報をDBから取得する.
     *
     * @return DBから取得したすべてのユーザー情報
     * @throws JsonProcessingException JSON文字列への変換時に発生する例外
     */
    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getUsers() throws JsonProcessingException {
        String methodName =
                Thread.currentThread().getStackTrace()[1].getMethodName();
        startLog(methodName, null);

        List<User> foundUsers = userService.findAllUsers();

        String response = objectMapper.writeValueAsString(foundUsers);
        endLog(methodName, response);
        return foundUsers;
    }

    /**
     * DBのユーザー情報を更新する.
     *
     * @param id   ユーザーID
     * @param user ユーザー情報
     * @return 更新後のユーザー情報
     * @throws JsonProcessingException JSON文字列への変換時に発生する例外
     */
    @RequestMapping(
            value = "{id}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    User putUser(@PathVariable("id") final int id,
                 @RequestBody @Validated final User user)
            throws JsonProcessingException {
        String methodName =
                Thread.currentThread().getStackTrace()[1].getMethodName();
        user.setId(id);
        String request = objectMapper.writeValueAsString(user);
        startLog(methodName, request);

        User updatedUser = userService.updateUser(user);

        String response = objectMapper.writeValueAsString(updatedUser);
        endLog(methodName, response);
        return updatedUser;
    }

    /**
     * ユーザー情報をDBから削除する.
     *
     * @param id ユーザーID
     * @throws JsonProcessingException JSON文字列への変換時に発生する例外
     */
    @RequestMapping(
            value = "{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    void deleteUser(@PathVariable("id") final int id)
            throws JsonProcessingException {
        String methodName =
                Thread.currentThread().getStackTrace()[1].getMethodName();
        Map<String, Integer> queryStrMap = new HashMap<>();
        queryStrMap.put("id", id);
        String request = objectMapper.writeValueAsString(queryStrMap);
        startLog(methodName, request);

        userService.deleteUser(id);

        endLog(methodName, null);
    }

    /**
     * スタートログを出力する.
     *
     * @param methodName メソッド名
     * @param request    リクエスト情報
     */
    private void startLog(final String methodName, final String request) {
        LOGGER.info(methodName + " was started. request:" + request);
    }

    /**
     * エンドログを出力する.
     *
     * @param methodName メソッド名
     * @param response   レスポンス情報
     */
    private void endLog(final String methodName, final String response) {
        LOGGER.info(methodName + " was completed. response:" + response);
    }
}
