package com.tinyhuman.tinyhumanapi.checklist.service.port;

import com.tinyhuman.tinyhumanapi.checklist.domain.Checklist;

import java.util.List;

public interface ChecklistRepository {

    Checklist save(Checklist checklist);

    List<Checklist> findByUserId(Long userId);

}
