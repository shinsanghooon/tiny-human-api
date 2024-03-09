package com.tinyhuman.tinyhumanapi.helpchat.infrastructure;


import com.tinyhuman.tinyhumanapi.common.infrastructure.BaseEntity;
import com.tinyhuman.tinyhumanapi.helpchat.domain.HelpChat;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Table(name = "help_chats")
@Where(clause = "is_deleted=false")
@NoArgsConstructor
public class HelpChatEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "help_request_id")
    private Long helpRequestId;

    @Column(name = "help_request_user_id")
    private Long helpRequestUserId;

    @Column(name = "help_answer_user_id")
    private Long helpAnswerUserId;

    @Column(name="is_deleted")
    private boolean isDeleted = false;

    @Builder
    public HelpChatEntity(Long id, Long helpRequestId, Long helpRequestUserId, Long helpAnswerUserId, boolean isDeleted) {
        this.id = id;
        this.helpRequestId = helpRequestId;
        this.helpRequestUserId = helpRequestUserId;
        this.helpAnswerUserId = helpAnswerUserId;
        this.isDeleted = isDeleted;
    }

    public static HelpChatEntity fromModel(HelpChat helpChat) {
        return HelpChatEntity.builder()
                .helpRequestId(helpChat.helpRequestId())
                .helpRequestUserId(helpChat.helpRequestUserId())
                .helpAnswerUserId(helpChat.helpAnswerUserId())
                .build();
    }

    public HelpChat toModel() {
        return HelpChat.builder()
                .id(this.id)
                .helpRequestId(this.helpRequestId)
                .helpRequestUserId(this.helpRequestUserId)
                .helpAnswerUserId(this.helpAnswerUserId)
                .createdAt(this.getCreatedAt())
                .build();
    }

}
