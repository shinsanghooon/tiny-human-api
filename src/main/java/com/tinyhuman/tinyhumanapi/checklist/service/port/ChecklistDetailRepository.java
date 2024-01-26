package com.tinyhuman.tinyhumanapi.checklist.service.port;

import com.tinyhuman.tinyhumanapi.checklist.domain.Checklist;
import com.tinyhuman.tinyhumanapi.checklist.domain.ChecklistDetail;

import java.util.List;
import java.util.Optional;

public interface ChecklistDetailRepository {

    ChecklistDetail save(ChecklistDetail checklistDetail);

    ChecklistDetail save(ChecklistDetail checklistDetail, Checklist checklist);

    List<ChecklistDetail> saveAll(List<ChecklistDetail> checklistDetails,  Checklist checklist);

    Optional<ChecklistDetail> findById(Long id);

}
