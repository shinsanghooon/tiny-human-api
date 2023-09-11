package com.tinyhuman.tinyhumanapi.user.infrastructure;

import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.domain.UserBabyRelation;
import com.tinyhuman.tinyhumanapi.user.enums.FamilyRelation;
import com.tinyhuman.tinyhumanapi.user.enums.UserBabyRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional(readOnly = true)
@Sql("classpath:sql/user-repository-test-data.sql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserBabyRelationRelationRepositoryImplTest {

    @Autowired
    private UserBabyRelationRelationRepositoryImpl userBabyRelationRelationRepository;

    @Test
    @DisplayName("사용자와 아기의 관계를 생성할 수 있다.")
    void setUserBabyRelationRelation() {

        UserBabyRelation relation = userBabyRelationRelationRepository.save(UserBabyRelation.builder()
                .user(User.builder().id(1L).build())
                .baby(Baby.builder().id(1L).build())
                .userBabyRole(UserBabyRole.ADMIN)
                .relation(FamilyRelation.FATHER)
                .build());

        assertThat(relation.user().id()).isEqualTo(1L);
        assertThat(relation.baby().id()).isEqualTo(1L);
        assertThat(relation.relation()).isEqualTo(FamilyRelation.FATHER);
    }

    @Test
    @DisplayName("사용자를 통해 소속된 아기와의 관계를 조회할 수 있다.")
    void getRelationByUser() {
        List<UserBabyRelation> userBabyRelations = userBabyRelationRelationRepository.findByUser(User.builder().id(1L).build());

        assertThat(userBabyRelations.size()).isEqualTo(1);
        assertThat(userBabyRelations.get(0).user().id()).isEqualTo(1L);
        assertThat(userBabyRelations.get(0).relation()).isEqualTo(FamilyRelation.FATHER);
    }

    @Test
    @DisplayName("사용자, 아기와 관계는 최대 1개 존재한다.")
    void getRelationById() {

        UserBabyMappingId userBabyMappingId = new UserBabyMappingId(1L, 1L);
        Optional<UserBabyRelation> relation = userBabyRelationRelationRepository.findById(userBabyMappingId);

        assertThat(relation.isPresent()).isTrue();
        assertThat(relation.get().user().id()).isEqualTo(1L);
        assertThat(relation.get().baby().id()).isEqualTo(1L);
        assertThat(relation.get().relation()).isEqualTo(FamilyRelation.FATHER);
    }

    @Test
    @DisplayName("사용자, 아기와 관계가 없을 경우 Optional.isEmpty()를 반환한다.")
    void noRelationById() {
        UserBabyMappingId userBabyMappingId = new UserBabyMappingId(2L, 1L);
        Optional<UserBabyRelation> relation = userBabyRelationRelationRepository.findById(userBabyMappingId);

        assertThat(relation.isEmpty()).isTrue();
    }
}