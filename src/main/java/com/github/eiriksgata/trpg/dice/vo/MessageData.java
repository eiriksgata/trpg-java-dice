package com.github.eiriksgata.trpg.dice.vo;

import lombok.Data;

@Data
public class MessageData<T> {

    private String message;
    private Long qqID;
    private T event;

}
