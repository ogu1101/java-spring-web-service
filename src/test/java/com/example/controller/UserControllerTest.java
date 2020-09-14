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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

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
    // NOTE: DBテーブルのauto_incrementを初期化するためにTRUNCATEを実行する。
    // WARNING: POSTリクエストをテストする場合、DBテーブルのauto_incrementを初期化しないと、auto_incrementされるDBカラムの値を正しく検証できない。
    @Sql(statements = "TRUNCATE TABLE users")
    @DatabaseSetup(value = "/test-data/user-controller/test-post-user/")
    @ExpectedDatabase(
            value = "/expected-data/user-controller/test-post-user/",
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

        this.mockMvc.perform(MockMvcRequestBuilders.post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", is(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(MockMvcResultMatchers.content().json(expected));
    }

    /**
     * {@link UserController#postUser}の異常系テスト.
     */
    @Test
    @DisplayName("POSTリクエストの異常系テスト")
    @Sql(statements = "TRUNCATE TABLE users")
    @DatabaseSetup(value = "/test-data/user-controller/test-post-user-abnormal/")
    @ExpectedDatabase(
            value = "/expected-data/user-controller/test-post-user-abnormal/",
            table = "users",
            assertionMode = DatabaseAssertionMode.NON_STRICT
    )
    public void testPostUserAbnormal() throws Exception {
        User user = new User();
        user.setCellPhoneNumber("abcdeABCDE");
        String requestBody = objectMapper.writeValueAsString(user);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", is(nullValue())))
                .andExpect(MockMvcResultMatchers.content().string(is(emptyString())));
    }

    /**
     * {@link UserController#getUser}の正常系テスト.
     */
    @Test
    @DisplayName("GETリクエストの正常系テスト")
    @DatabaseSetup(value = "/test-data/user-controller/test-get-user/")
    public void testGetUser() throws Exception {
        User user = new User();
        user.setId(2);
        user.setName("Mari Ogura");
        user.setEmailAddress("mari.ogura@example.com");
        user.setCellPhoneNumber("09002222222");
        String expected = objectMapper.writeValueAsString(user);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/user/2"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", is(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(MockMvcResultMatchers.content().json(expected));
    }

    /**
     * {@link UserController#getUsers}の正常系テスト.
     */
    @Test
    @DisplayName("GETリクエストの正常系テスト")
    @DatabaseSetup(value = "/test-data/user-controller/test-get-user/")
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

        this.mockMvc.perform(MockMvcRequestBuilders.get("/user"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", is(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(MockMvcResultMatchers.content().json(expected));
    }

    /**
     * {@link UserController#putUser}の正常系テスト.
     */
    @Test
    @DisplayName("PUTリクエストの正常系テスト")
    @DatabaseSetup(value = "/test-data/user-controller/test-put-user/")
    @ExpectedDatabase(
            value = "/expected-data/user-controller/test-put-user/",
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

        this.mockMvc.perform(MockMvcRequestBuilders.put("/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", is(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(MockMvcResultMatchers.content().json(expected));
    }

    /**
     * {@link UserController#deleteUser}の正常系テスト.
     */
    @Test
    @DisplayName("DELETEリクエストの正常系テスト")
    @DatabaseSetup(value = "/test-data/user-controller/test-delete-user/")
    @ExpectedDatabase(
            value = "/expected-data/user-controller/test-delete-user/",
            table = "users",
            assertionMode = DatabaseAssertionMode.NON_STRICT
    )
    public void testDeleteUser() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", is(nullValue())))
                .andExpect(MockMvcResultMatchers.content().string(is(emptyString())));
    }
}
