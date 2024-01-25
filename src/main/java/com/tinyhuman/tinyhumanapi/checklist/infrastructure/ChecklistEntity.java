package com.tinyhuman.tinyhumanapi.checklist.infrastructure;


import com.tinyhuman.tinyhumanapi.checklist.domain.Checklist;
import com.tinyhuman.tinyhumanapi.common.infrastructure.BaseEntity;
import com.tinyhuman.tinyhumanapi.user.infrastructure.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "checklists")
@Where(clause = "is_deleted=false")
@NoArgsConstructor
public class ChecklistEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "checklist")
    private final List<ChecklistDetailEntity> checklistDetails = new ArrayList<>();

    @Column(name="is_deleted")
    private boolean isDeleted = false;

    @Builder
    public ChecklistEntity(Long id, String title, UserEntity user, boolean isDeleted) {
        this.id = id;
        this.title = title;
        this.user = setUser(user);
    }

    private UserEntity setUser(UserEntity user) {
        if (this.user != null) {
            this.user.getDiaries().remove(this);
        }
        this.user = user;
        user.addChecklist(this);

        return user;
    }

    public static ChecklistEntity fromModel(Checklist checklist) {
        return ChecklistEntity.builder()
                .id(checklist.id())
                .title(checklist.title())
                .user(UserEntity.fromModel(checklist.user()))
                .isDeleted(checklist.isDeleted())
                .build();
    }

    public Checklist toModel() {
        return Checklist.builder()
                .id(this.id)
                .title(this.title)
                .user(this.user.toModel())
                .isDeleted(this.isDeleted)
                .checklistDetails(this.checklistDetails.stream().map(ChecklistDetailEntity::toModel).toList())
                .build();
    }

    public void addChecklistDetail(ChecklistDetailEntity checklistDetail) {
        checklistDetails.add(checklistDetail);
    }

}
