package com.tinyhuman.tinyhumanapi.checklist.infrastructure;

import com.tinyhuman.tinyhumanapi.checklist.domain.Checklist;
import com.tinyhuman.tinyhumanapi.checklist.domain.ChecklistDetail;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Table(name = "checklists_details")
@Where(clause = "is_deleted=false")
@NoArgsConstructor
public class ChecklistDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contents")
    private String contents;

    @Column(name = "reason")
    private String reason;

    @Column(name="is_checked")
    private boolean isChecked = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checklist_id")
    private ChecklistEntity checklist;

    @Column(name="is_deleted")
    private boolean isDeleted = false;

    @Builder
    public ChecklistDetailEntity(Long id, String contents, String reason, ChecklistEntity checklist, boolean isChecked, boolean isDeleted) {
        this.id = id;
        this.contents = contents;
        this.reason = reason;
        this.checklist = setChecklist(checklist);
        this.isChecked = isChecked;
        this.isDeleted = isDeleted;
    }

    private ChecklistEntity setChecklist(ChecklistEntity checklist) {
        if (this.checklist != null) {
            this.checklist.getChecklistDetails().remove(this);
        }
        this.checklist = checklist;
        checklist.addChecklistDetail(this);

        return checklist;
    }

    public static ChecklistDetailEntity fromModel(ChecklistDetail checklistDetail, Checklist checklist) {
        return ChecklistDetailEntity.builder()
                .id(checklistDetail.id())
                .contents(checklistDetail.contents())
                .reason(checklistDetail.reason())
                .isChecked(checklistDetail.isChecked())
                .checklist(ChecklistEntity.fromModel(checklist))
                .build();
    }

    public ChecklistDetail toModel() {
        return ChecklistDetail.builder()
                .id(this.id)
                .contents(this.contents)
                .reason(this.reason)
                .isChecked(this.isChecked)
                .build();
    }
}
