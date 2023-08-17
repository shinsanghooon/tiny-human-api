package com.tinyhuman.tinyhumanapi.user.service;

import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.baby.enums.Gender;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.domain.UserBabyRelation;
import com.tinyhuman.tinyhumanapi.user.enums.FamilyRelation;
import com.tinyhuman.tinyhumanapi.user.enums.UserBabyRole;
import com.tinyhuman.tinyhumanapi.user.enums.UserStatus;
import com.tinyhuman.tinyhumanapi.user.mock.FakeUserBabyRelationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserBabyRelationServiceImplTest {

    private UserBabyRelationServiceImpl userBabyRelationServiceImpl;
    @BeforeEach
    void init() {
        FakeUserBabyRelationRepository fakeUserBabyRelationRepository = new FakeUserBabyRelationRepository();
        this.userBabyRelationServiceImpl = new UserBabyRelationServiceImpl(fakeUserBabyRelationRepository);

        User user = User.builder()
                .id(1L)
                .name("김부모")
                .email("test@gmail.com")
                .status(UserStatus.ACTIVE)
                .password("1234")
                .isDeleted(false)
                .build();

        Baby baby1 = Baby.builder()
                .id(1L)
                .name("김가나")
                .gender(Gender.MALE)
                .nickName("초코")
                .timeOfBirth(20)
                .dayOfBirth(LocalDate.of(2022, 9, 20))
                .profileImgUrl("test_url")
                .isDeleted(false)
                .build();

        Baby baby2 = Baby.builder()
                .id(2L)
                .name("김다라")
                .gender(Gender.MALE)
                .nickName("딸기")
                .timeOfBirth(10)
                .dayOfBirth(LocalDate.of(2023, 9, 20))
                .profileImgUrl("test_url")
                .isDeleted(false)
                .build();

        UserBabyRelation relation = UserBabyRelation.builder()
                .user(user)
                .baby(baby1)
                .relation(FamilyRelation.FATHER)
                .userBabyRole(UserBabyRole.ADMIN)
                .build();

        fakeUserBabyRelationRepository.save(relation);

        UserBabyRelation relation2 = UserBabyRelation.builder()
                .user(user)
                .baby(baby2)
                .relation(FamilyRelation.FATHER)
                .userBabyRole(UserBabyRole.ADMIN)
                .build();

        fakeUserBabyRelationRepository.save(relation2);
    }

    @Test
    @DisplayName("나의 아기를 조회할 수 있다.")
    void getMyBabies() {

        Long userId = 1L;
        List<UserBabyRelation> userBabyRelations = userBabyRelationServiceImpl.findByUser(User.builder().id(userId).build());

        assertThat(userBabyRelations.size()).isEqualTo(2);
        assertThat(userBabyRelations).allSatisfy(u -> u.user().id());

    }

}