package com.tinyhuman.tinyhumanapi.user.infrastructure;

import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.enums.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional(readOnly = true)
@Sql("classpath:sql/user-repository-test-data.sql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryImplTest {

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Test
    @DisplayName("사용자를 등록하고 도메인 모델을 반환한다.")
    void registerUser() {
        User user = User.builder()
                .name("홈버그1")
                .email("homebug1@tinyhuman.com")
                .password("1234")
                .status(UserStatus.ACTIVE)
                .build();

        User savedUser = userRepositoryImpl.save(user);

        assertThat(user.name()).isEqualTo(savedUser.name());
        assertThat(user.email()).isEqualTo(savedUser.email());
        assertThat(user.status()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    @DisplayName("id로 사용자 정보를 조회할 수 있다.")
    void findUserById() {
        User user = userRepositoryImpl.findById(1L).get();

        assertThat(user.id()).isEqualTo(1L);
        assertThat(user.name()).isEqualTo("신");
        assertThat(user.email()).isEqualTo("test@gmail.com");
        assertThat(user.status()).isInstanceOf(UserStatus.class);
    }

    @Test
    @DisplayName("존재하지 않는 id로 사용자 정보를 조회하면 Optional.isEmpty()가 반환된다..")
    void cannotFindUserById() {
        Optional<User> user = userRepositoryImpl.findById(999L);

        assertThat(user.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("이메일로 사용자 정보를 조회할 수 있다.")
    void findUserByEmail() {
        User user = userRepositoryImpl.findByEmail("test@gmail.com").get();

        assertThat(user.id()).isEqualTo(1L);
        assertThat(user.name()).isEqualTo("신");
        assertThat(user.email()).isEqualTo("test@gmail.com");
        assertThat(user.status()).isInstanceOf(UserStatus.class);
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 사용자 정보를 조회하면 Optional.isEmpty()가 반환된다.")
    void cannotFindUserByEmail() {
        Optional<User> user = userRepositoryImpl.findByEmail("helkufheukab@daf.com");

        assertThat(user.isEmpty()).isTrue();
    }


    @Test
    @DisplayName("이메일을 사용하는 사용자가 있는지 확인할 수 있다.")
    void existsByEmail() {
        boolean isEmailDuplicated = userRepositoryImpl.existsByEmail("test@gmail.com");

        assertThat(isEmailDuplicated).isTrue();
    }



}