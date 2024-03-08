package com.tinyhuman.tinyhumanapi.helpchat.infrastructure;


import com.tinyhuman.tinyhumanapi.common.infrastructure.BaseEntity;
import com.tinyhuman.tinyhumanapi.helpchat.domain.HelpRequest;
import com.tinyhuman.tinyhumanapi.helpchat.enums.RequestType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Table(name = "help_requests")
@Where(clause = "is_deleted=false")
@NoArgsConstructor
public class HelpRequestEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "request_type")
    @Enumerated(EnumType.STRING)
    private RequestType requestType;

    @Column(name = "contents")
    private String contents;

    @Column(name="is_deleted")
    private boolean isDeleted = false;

    @Builder
    public HelpRequestEntity(Long id, Long userId, RequestType requestType, String contents, boolean isDeleted) {
        this.id = id;
        this.userId = userId;
        this.requestType = requestType;
        this.contents = contents;
        this.isDeleted = isDeleted;
    }

    public static HelpRequestEntity fromModel(HelpRequest helpRequest) {
        return HelpRequestEntity.builder()
                .userId(helpRequest.userId())
                .requestType(helpRequest.requestType())
                .contents(helpRequest.contents())
                .build();
    }

    public HelpRequest toModel() {
        return HelpRequest.builder()
                .id(this.id)
                .userId(this.userId)
                .requestType(this.requestType)
                .contents(this.contents)
                .createdAt(this.getCreatedAt())
                .build();
    }

}
