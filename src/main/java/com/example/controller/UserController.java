package com.example.controller;

import com.example.entity.User;
import com.example.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    @RequestMapping(
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public User postUser(@RequestBody @Validated final User user)
            throws JsonProcessingException {
        startLog(null, user);
        User savedUser = userService.saveUser(user);
        endLog(savedUser);
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
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", String.valueOf(id));
        startLog(pathParams, null);
        Optional<User> foundUser = userService.findUser(id);
        endLog(foundUser.orElse(null));
        return foundUser;
    }

    /**
     * すべてのユーザー情報をDBから取得する.
     *
     * @return DBから取得したすべてのユーザー情報
     * @throws JsonProcessingException JSON文字列への変換時に発生する例外
     */
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getUsers() throws JsonProcessingException {
        startLog(null, null);
        List<User> foundUsers = userService.findAllUsers();
        endLog(foundUsers);
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
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", String.valueOf(id));
        startLog(pathParams, user);
        user.setId(id);
        User updatedUser = userService.updateUser(user);
        endLog(updatedUser);
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
            method = RequestMethod.DELETE)
    void deleteUser(@PathVariable("id") final int id)
            throws JsonProcessingException {
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", String.valueOf(id));
        startLog(pathParams, null);
        userService.deleteUser(id);
        endLog(null);
    }

    // TODO: メソッドをライブラリモジュールに移動させる。

    /**
     * スタートログを出力する.
     *
     * @param pathParams  パスパラメーター
     * @param requestBody リクエストボディ
     * @throws JsonProcessingException JSON文字列への変換時に発生する例外
     */
    private void startLog(final Map<String, String> pathParams,
                          final Object requestBody)
            throws JsonProcessingException {
        final String methodName =
                Thread.currentThread().getStackTrace()[2].getMethodName();
        LOGGER.info(methodName + " was started.");
        String pathParamsStr = objectMapper.writeValueAsString(pathParams);
        LOGGER.info("pathParams:" + pathParamsStr);
        String requestBodyStr = objectMapper.writeValueAsString(requestBody);
        LOGGER.info("requestBody:" + requestBodyStr);
    }

    // TODO: メソッドをライブラリモジュールに移動させる。

    /**
     * エンドログを出力する.
     *
     * @param responseBody レスポンスボディ
     * @throws JsonProcessingException JSON文字列への変換時に発生する例外
     */
    private void endLog(final Object responseBody)
            throws JsonProcessingException {
        final String methodName =
                Thread.currentThread().getStackTrace()[2].getMethodName();
        String responseBodyStr = objectMapper.writeValueAsString(responseBody);
        LOGGER.info("responseBody:" + responseBodyStr);
        LOGGER.info(methodName + " was completed.");
    }
}
