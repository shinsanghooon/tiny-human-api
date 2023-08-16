package com.tinyhuman.tinyhumanapi.user.infrastructure;

import com.tinyhuman.tinyhumanapi.common.infrastructure.BaseEntity;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

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
    @Enumerated
    private UserStatus status;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Builder
    public UserEntity(Long id, String name, String email, String password, UserStatus status, LocalDateTime lastLoginAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.status = status;
        this.lastLoginAt = lastLoginAt;
    }

    public static UserEntity fromModel(User user) {
        return UserEntity.builder()
                .id(user.id())
                .name(user.name())
                .email(user.email())
                .password(user.password())
                .status(user.status())
                .lastLoginAt(user.lastLoginAt())
                .build();
    }

    public User toModel() {
        return User.builder()
                .id(this.id)
                .name(this.name)
                .email(this.email)
                .password(this.password)
                .status(this.status)
                .lastLoginAt(this.lastLoginAt)
                .build();
    }
}
