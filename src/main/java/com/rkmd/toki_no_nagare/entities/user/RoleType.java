package com.rkmd.toki_no_nagare.entities.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum RoleType {
    @JsonProperty("admin")
    ADMIN,
    @JsonProperty("viewer")
    VIEWER
}
