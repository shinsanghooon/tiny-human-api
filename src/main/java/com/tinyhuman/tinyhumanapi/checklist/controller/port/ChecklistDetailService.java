package com.tinyhuman.tinyhumanapi.checklist.controller.port;

import com.tinyhuman.tinyhumanapi.checklist.domain.ChecklistDetail;

public interface ChecklistDetailService {

    ChecklistDetail checkUpdate(Long checklistId, Long id);
}
