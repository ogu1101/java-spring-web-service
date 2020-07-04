package com.aws.codestar.projecttemplates.controller;

import com.aws.codestar.projecttemplates.entity.User;
import com.aws.codestar.projecttemplates.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * UserのControllerクラス.
 */
@RestController
@RequestMapping("user")
public class UserController {
    // TODO ResponseEntityを使用してHTTPステータスコードを応答するように修正
    // TODO APIレスポンスのHTTPヘッダに"Content-Type:application/json"を付与するように修正
    // TODO エラー発生時にエラー内容を応答しないように修正
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
     */
    @RequestMapping(method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public User postUser(@RequestBody @Validated final User user) {
        return userService.saveUser(user);
    }

    /**
     * 単一のユーザー情報をDBから取得する.
     *
     * @param id ユーザーID
     * @return DBから取得した単一のユーザー情報
     */
    @RequestMapping(
            value = "{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<User> getUser(@PathVariable("id") final int id) {
        return userService.findUser(id);
    }

    /**
     * すべてのユーザー情報をDBから取得する.
     *
     * @return DBから取得したすべてのユーザー情報
     */
    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getUsers() {
        return userService.findAllUsers();
    }

    /**
     * DBのユーザー情報を更新する.
     *
     * @param id   ユーザーID
     * @param user ユーザー情報
     * @return 更新後のユーザー情報
     */
    @RequestMapping(
            value = "{id}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    User putUser(@PathVariable("id") final int id,
                 @RequestBody @Validated final User user) {
        user.setId(id);
        return userService.updateUser(user);
    }

    /**
     * ユーザー情報をDBから削除する.
     *
     * @param id ユーザーID
     */
    @RequestMapping(
            value = "{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    void deleteUser(@PathVariable("id") final int id) {
        userService.deleteUser(id);
    }
}
