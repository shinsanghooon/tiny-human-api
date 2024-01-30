package com.tinyhuman.tinyhumanapi.checklist.controller.port;

import com.tinyhuman.tinyhumanapi.checklist.domain.ChecklistDetail;

public interface ChecklistDetailService {

    ChecklistDetail toggleCheckDetail(Long checklistId, Long id);

    void toggleAllCheckDetail(Long checklistId, boolean targetChecked);
}
