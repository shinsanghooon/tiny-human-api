package com.tinyhuman.tinyhumanapi.common.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public abstract class BaseEntity extends BaseTimeEntity {

    @CreatedBy
    @Column(updatable = false, name = "created_by")
    private String createdBy;

    @LastModifiedBy
    @Column(name="updated_by")
    private String updatedBy;

    @Column(name="is_deleted")
    private Boolean isDeleted = false;

    public void delete() {
        this.isDeleted = true;
    }
}

