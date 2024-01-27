package com.tinyhuman.tinyhumanapi.checklist.service.port;

import com.tinyhuman.tinyhumanapi.checklist.domain.Checklist;

import java.util.List;
import java.util.Optional;

public interface ChecklistRepository {

    Checklist save(Checklist checklist);

    List<Checklist> findByUserId(Long userId);

    Optional<Checklist> findById(Long id);

    Optional<Checklist> findByIdAndUserId(Long id, Long userId);

}
