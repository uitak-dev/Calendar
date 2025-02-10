package com.demo.calendar.domain.entity;

import java.util.EnumSet;
import java.util.Set;

public enum Permission {
    READ, WRITE, EDIT;

    // 상위 권한이 있을 경우 자동으로 하위 권한을 포함하여 반환.
    public Set<Permission> getInheritedPermissions() {
        return switch (this) {
            case EDIT -> EnumSet.of(EDIT, WRITE, READ);
            case WRITE -> EnumSet.of(WRITE, READ);
            case READ -> EnumSet.of(READ);
        };
    }
}
