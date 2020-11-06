package com.zhch.paysvc.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lumos
 */
@Data
public class PrivilegeDto implements Serializable {

    private Integer type;
    private String path;

}
