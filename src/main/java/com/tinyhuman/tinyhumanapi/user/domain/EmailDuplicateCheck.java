package com.tinyhuman.tinyhumanapi.user.domain;

import jakarta.validation.constraints.Email;

public record EmailDuplicateCheck(@Email String email) {

}
