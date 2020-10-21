package com.example.controller;

import com.example.Application;
import com.example.entity.User;
import com.example.test.util.CsvDataSetLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// TODO: 条件分岐を網羅するようにテストケースを作成する。
// TODO: 閾値を網羅するようにテストケースを作成する。
/*
 TODO: テストデータに以下のバリエーションを設ける。
    ・最大値
    ・最小値
    ・中間値
    ・最大値 + α
    ・最小値 - α
 */

/**
 * {@link UserController}のテスト.
 */
@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = Application.class)
@DisplayName("UserControllerのテスト")
public class UserControllerTest {
    /**
     * ObjectMapperオブジェクト.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();
    /**
     * MockMvcオブジェクト.
     */
    @Autowired
    MockMvc mockMvc;

    /**
     * {@link UserController#postUser}の正常系テスト.
     */
    @Test
    @DisplayName("POSTリクエストの正常系テスト")
    // NOTE: DBテーブルのAUTO_INCREMENTを初期化するためにTRUNCATEを実行する。
    /*
        WARNING:
            POSTリクエストをテストする場合は、
            DBテーブルのAUTO_INCREMENTを初期化しないと、
            AUTO_INCREMENTされるDBカラムの値を正しく検証できない。
     */
    @Sql(statements = "TRUNCATE TABLE users")
    @DatabaseSetup(
            value = "/com/example/controller/user-controller-test"
                    + "/test-post-user/test-data/")
    @ExpectedDatabase(
            value = "/com/example/controller/user-controller-test"
                    + "/test-post-user/expected-data/",
            table = "users",
            assertionMode = DatabaseAssertionMode.NON_STRICT
    )
    public void testPostUser() throws Exception {
        User user = new User();
        user.setName("Shuhei Ogura");
        user.setEmailAddress("shuhei.ogura@example.com");
        user.setCellPhoneNumber("09001111111");
        String requestBody = objectMapper.writeValueAsString(user);
        user.setId(1);
        String expected = objectMapper.writeValueAsString(user);

        this.mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(
                        "Content-Type",
                        is(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(content().json(expected));
    }

    /**
     * {@link UserController#postUser}の異常系テスト.
     */
    @Test
    @DisplayName("POSTリクエストの異常系テスト")
    @Sql(statements = "TRUNCATE TABLE users")
    @DatabaseSetup(
            value = "/com/example/controller/user-controller-test"
                    + "/test-post-user-abnormal/test-data/")
    @ExpectedDatabase(
            value = "/com/example/controller/user-controller-test"
                    + "/test-post-user-abnormal/expected-data/",
            table = "users",
            assertionMode = DatabaseAssertionMode.NON_STRICT
    )
    public void testPostUserAbnormal() throws Exception {
        User user = new User();
        user.setCellPhoneNumber("abcdeABCDE");
        String requestBody = objectMapper.writeValueAsString(user);

        this.mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().string(
                        "Content-Type",
                        is(nullValue())))
                .andExpect(content().string(is(emptyString())));
    }

    /**
     * {@link UserController#getUser}の正常系テスト.
     */
    @Test
    @DisplayName("GETリクエストの正常系テスト")
    @DatabaseSetup(
            value = "/com/example/controller/user-controller-test"
                    + "/test-get-user/test-data/")
    public void testGetUser() throws Exception {
        User user = new User();
        user.setId(2);
        user.setName("Mari Ogura");
        user.setEmailAddress("mari.ogura@example.com");
        user.setCellPhoneNumber("09002222222");
        String expected = objectMapper.writeValueAsString(user);

        this.mockMvc.perform(get("/user/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(
                        "Content-Type",
                        is(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(content().json(expected));
    }

    /**
     * {@link UserController#getUsers}の正常系テスト.
     */
    @Test
    @DisplayName("GETリクエストの正常系テスト")
    @DatabaseSetup(
            value = "/com/example/controller/user-controller-test"
                    + "/test-get-users/test-data/")
    public void testGetUsers() throws Exception {
        User user1 = new User();
        user1.setId(1);
        user1.setName("Shuhei Ogura");
        user1.setEmailAddress("shuhei.ogura@example.com");
        user1.setCellPhoneNumber("09001111111");

        User user2 = new User();
        user2.setId(2);
        user2.setName("Mari Ogura");
        user2.setEmailAddress("mari.ogura@example.com");
        user2.setCellPhoneNumber("09002222222");

        User user3 = new User();
        user3.setId(3);
        user3.setName("Kagetaka Ogura");
        user3.setEmailAddress("kagetaka.ogura@example.com");
        user3.setCellPhoneNumber("09003333333");

        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        String expected = objectMapper.writeValueAsString(userList);

        this.mockMvc.perform(get("/user"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(
                        "Content-Type",
                        is(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(content().json(expected));
    }

    /**
     * {@link UserController#putUser}の正常系テスト.
     */
    @Test
    @DisplayName("PUTリクエストの正常系テスト")
    @DatabaseSetup(
            value = "/com/example/controller/user-controller-test"
                    + "/test-put-user/test-data/")
    @ExpectedDatabase(
            value = "/com/example/controller/user-controller-test"
                    + "/test-put-user/expected-data/",
            table = "users",
            assertionMode = DatabaseAssertionMode.NON_STRICT
    )
    public void testPutUser() throws Exception {
        User user = new User();
        user.setName("Mari Ogura");
        user.setEmailAddress("mari.ogura@example.com");
        user.setCellPhoneNumber("09002222222");
        String requestBody = objectMapper.writeValueAsString(user);
        user.setId(1);
        String expected = objectMapper.writeValueAsString(user);

        this.mockMvc.perform(put("/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(
                        "Content-Type",
                        is(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(content().json(expected));
    }

    /**
     * {@link UserController#deleteUser}の正常系テスト.
     */
    @Test
    @DisplayName("DELETEリクエストの正常系テスト")
    @DatabaseSetup(
            value = "/com/example/controller/user-controller-test"
                    + "/test-delete-user/test-data/")
    @ExpectedDatabase(
            value = "/com/example/controller/user-controller-test"
                    + "/test-delete-user/expected-data/",
            table = "users",
            assertionMode = DatabaseAssertionMode.NON_STRICT
    )
    public void testDeleteUser() throws Exception {
        this.mockMvc.perform(delete("/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(
                        "Content-Type",
                        is(nullValue())))
                .andExpect(content().string(is(emptyString())));
    }
}
