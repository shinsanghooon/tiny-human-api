package com.tinyhuman.tinyhumanapi.checklist.service.port;

import com.tinyhuman.tinyhumanapi.checklist.domain.Checklist;
import com.tinyhuman.tinyhumanapi.checklist.domain.ChecklistDetail;

public interface ChecklistDetailRepository {
    ChecklistDetail save(ChecklistDetail checklistDetail, Checklist checklist);

}
