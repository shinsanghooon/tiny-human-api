package com.tinyhuman.tinyhumanapi.helpchat.infrastructure;


import com.tinyhuman.tinyhumanapi.common.infrastructure.BaseEntity;
import com.tinyhuman.tinyhumanapi.helpchat.domain.HelpChat;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

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

    @Column(name = "latest_message")
    @ColumnDefault("대화 중인 채팅이 없습니다.")
    private String latestMessage;

    @Column(name = "latest_message_time")
    private LocalDateTime latestMessageTime;

    @Column(name="is_deleted")
    private boolean isDeleted = false;

    @Builder
    public HelpChatEntity(Long id, Long helpRequestId, Long helpRequestUserId, Long helpAnswerUserId, String latestMessage, LocalDateTime latestMessageTime, boolean isDeleted) {
        this.id = id;
        this.helpRequestId = helpRequestId;
        this.helpRequestUserId = helpRequestUserId;
        this.helpAnswerUserId = helpAnswerUserId;
        this.latestMessage = latestMessage;
        this.latestMessageTime = latestMessageTime;
        this.isDeleted = isDeleted;
    }

    public static HelpChatEntity fromModel(HelpChat helpChat) {
        return HelpChatEntity.builder()
                .id(helpChat.id())
                .helpRequestId(helpChat.helpRequestId())
                .helpRequestUserId(helpChat.helpRequestUserId())
                .helpAnswerUserId(helpChat.helpAnswerUserId())
                .latestMessage(helpChat.latestMessage())
                .latestMessageTime(helpChat.latestMessageTime())
                .build();
    }

    public HelpChat toModel() {
        return HelpChat.builder()
                .id(this.id)
                .helpRequestId(this.helpRequestId)
                .helpRequestUserId(this.helpRequestUserId)
                .helpAnswerUserId(this.helpAnswerUserId)
                .latestMessage(this.latestMessage)
                .latestMessageTime(this.latestMessageTime)
                .createdAt(this.getCreatedAt())
                .build();
    }

}
