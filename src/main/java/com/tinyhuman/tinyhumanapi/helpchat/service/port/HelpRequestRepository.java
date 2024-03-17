package com.tinyhuman.tinyhumanapi.helpchat.service.port;

import com.tinyhuman.tinyhumanapi.helpchat.domain.HelpRequest;

import java.util.List;
import java.util.Optional;

public interface HelpRequestRepository {
    HelpRequest save(HelpRequest helpRequest);

    List<HelpRequest> findByUserId(Long userId);

//    List<HelpRequest> findByUserIdNot(Long userId);

    List<HelpRequest> findAll();

    Optional<HelpRequest> findById(Long id);

    Optional<HelpRequest> findByIdAndUserId(Long id, Long userId);
}
