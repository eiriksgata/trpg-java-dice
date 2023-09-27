package com.github.eiriksgata.trpg.dice.vo;

import lombok.Data;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.dice
 * date:2020/10/20
 **/
@Data
public class MessageData<T> {

    private String message;
    private Long qqID;
    private T event;

}
