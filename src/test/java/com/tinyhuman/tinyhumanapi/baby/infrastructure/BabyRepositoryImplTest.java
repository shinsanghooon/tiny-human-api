package com.tinyhuman.tinyhumanapi.baby.infrastructure;

import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.baby.enums.Gender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional(readOnly = true)
@Sql("classpath:sql/user-repository-test-data.sql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BabyRepositoryImplTest {

    @Autowired
    private BabyRepositoryImpl babyRepository;

    @Test
    @DisplayName("사용자는 아기를 등록할 수 있다.")
    void registerBaby() {
        Baby baby = Baby.builder()
                .name("코리")
                .gender(Gender.MALE)
                .dayOfBirth(LocalDate.of(2022, 9, 23))
                .timeOfBirth(23)
                .nickName("코리코리")
                .build();
        Baby savedBaby = babyRepository.save(baby);

        assertThat(savedBaby.name()).isEqualTo("코리");
        assertThat(savedBaby.gender()).isEqualTo(Gender.MALE);
        assertThat(savedBaby.timeOfBirth()).isEqualTo(23);
        assertThat(savedBaby.dayOfBirth()).isEqualTo(LocalDate.of(2022, 9, 23));
        assertThat(savedBaby.nickName()).isEqualTo("코리코리");
    }

    @Test
    @DisplayName("id로 아기를 조회할 수 있다.")
    void getBaby() {
        Long babyId = 1L;

        Optional<Baby> baby = babyRepository.findById(babyId);

        assertThat(baby.isPresent()).isTrue();
        assertThat(baby.get().id()).isEqualTo(babyId);
        assertThat(baby.get().name()).isEqualTo("신지");
    }

    @Test
    @DisplayName("존재하지 않는 id로 조회하면 Optional.isEmpty()가 반환된다.")
    void noBaby() {
        Long babyId = 999L;

        Optional<Baby> baby = babyRepository.findById(babyId);

        assertThat(baby.isEmpty()).isTrue();
    }
}