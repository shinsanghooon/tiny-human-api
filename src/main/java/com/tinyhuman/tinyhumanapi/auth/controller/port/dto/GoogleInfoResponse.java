package com.tinyhuman.tinyhumanapi.auth.controller.port.dto;

public record GoogleInfoResponse(String issued_to, String audience, String user_id, String scope, int expires_in,
                                 String email, boolean verified_email, String access_type) {
}
