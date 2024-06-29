package com.tinyhuman.tinyhumanapi.user.infrastructure;

import com.tinyhuman.tinyhumanapi.auth.eum.SocialMedia;
import com.tinyhuman.tinyhumanapi.checklist.infrastructure.ChecklistEntity;
import com.tinyhuman.tinyhumanapi.common.infrastructure.BaseEntity;
import com.tinyhuman.tinyhumanapi.diary.infrastructure.DiaryEntity;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "users")
@Where(clause = "is_deleted=false")
@NoArgsConstructor
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "social_media")
    @Enumerated(EnumType.STRING)
    private SocialMedia socialMedia;

    @OneToMany(mappedBy = "user")
    private final List<UserBabyRelationEntity> userBabyRelations = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private final List<DiaryEntity> diaries = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private final List<ChecklistEntity> checklists = new ArrayList<>();

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @Column(name = "allow_diary_notifications", nullable = false)
    private boolean allowDiaryNotifications = false;

    @Column(name = "allow_chat_notifications", nullable = false)
    private boolean allowChatNotifications = false;

    @Builder
    public UserEntity(Long id, String name, String email, String password, UserStatus status, SocialMedia socialMedia, LocalDateTime lastLoginAt, boolean isDeleted,
                      boolean isAllowDiaryNotifications,
                      boolean isAllowChatNotifications) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.status = status;
        this.socialMedia = socialMedia;
        this.lastLoginAt = lastLoginAt;
        this.isDeleted = isDeleted;
        this.allowDiaryNotifications = isAllowDiaryNotifications;
        this.allowChatNotifications = isAllowChatNotifications;
    }

    public static UserEntity fromModel(User user) {
        return UserEntity.builder()
                .id(user.id())
                .name(user.name())
                .email(user.email())
                .password(user.password())
                .status(user.status())
                .socialMedia(user.socialMedia())
                .lastLoginAt(user.lastLoginAt())
                .isDeleted(user.isDeleted())
                .isAllowChatNotifications(user.isAllowChatNotifications())
                .isAllowDiaryNotifications(user.isAllowDiaryNotifications())
                .build();
    }

    public User toModel() {
        return User.builder()
                .id(this.id)
                .name(this.name)
                .email(this.email)
                .password(this.password)
                .status(this.status)
                .socialMedia(this.socialMedia)
                .lastLoginAt(this.lastLoginAt)
                .isDeleted(this.isDeleted)
                .isAllowChatNotifications(this.allowChatNotifications)
                .isAllowDiaryNotifications(this.allowDiaryNotifications)
                .build();
    }

    public void addRelation(UserBabyRelationEntity userBabyRelation) {
        userBabyRelations.add(userBabyRelation);
    }

    public void addDiary(DiaryEntity diary) {
        diaries.add(diary);
    }

    public void addChecklist(ChecklistEntity checklist) {
        checklists.add(checklist);
    }
}
