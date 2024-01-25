package com.tinyhuman.tinyhumanapi.checklist.controller.port;

import com.tinyhuman.tinyhumanapi.checklist.controller.port.dto.ChecklistCreate;
import com.tinyhuman.tinyhumanapi.checklist.controller.port.dto.ChecklistResponse;

import java.util.List;

public interface ChecklistService {

    ChecklistResponse register(ChecklistCreate checklistCreate);

    List<ChecklistResponse> getChecklist();
}
