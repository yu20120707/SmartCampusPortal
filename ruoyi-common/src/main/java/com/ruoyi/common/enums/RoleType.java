package com.ruoyi.common.enums;

import lombok.Getter;

/**
 * @author ccc
 */
@Getter
public enum RoleType {

    ADMIN("1", "管理员"),
    SIMPLE("2", "普通用户"),
    STUDENT("110", "学生"),
    TEACHER("111", "教师"),
    LEADER("112", "领导");

    private final String code;
    private final String info;

    RoleType(String code, String info) {
        this.code = code;
        this.info = info;
    }

}
