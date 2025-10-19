package com.shopme.common.entity;

import lombok.Data;

@Data
public class StateDTO
{
    /*TODO: Change DTO place*/
    private Integer id;
    private String name;

    public StateDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
