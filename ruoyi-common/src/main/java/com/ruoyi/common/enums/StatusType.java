package com.ruoyi.common.enums;

import lombok.Getter;

/**
 * @author ccc
 */
@Getter
public enum StatusType {
    CAOG("0", "草稿"),
    ING("1", "申请中"),
    SUCCESS("2", "成功"),
    FAIL("3", "失败");

    private final String code;
    private final String info;

    StatusType(String code, String info) {
        this.code = code;
        this.info = info;
    }
}
