package com.tinyhuman.tinyhumanapi.user.service.port;

import com.tinyhuman.tinyhumanapi.user.domain.User;

public interface UserRepository {

    User save(User user);

    User findById(Long id);
}
