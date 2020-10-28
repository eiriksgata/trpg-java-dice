package indi.eiriksgata.dice.operation.impl;

import indi.eiriksgata.dice.callback.SanCheckCallback;

/**
 * @author: create by Keith
 * @version: v1.0
 * @description: indi.eiriksgata.dice.operation.impl
 * @date:2020/10/28
 **/
public class SanCheckImpl {


    public String sanCheck(String text, String attribute, SanCheckCallback callback) {


        // RegularExpressionUtils.getMatchers("")

        callback.getResultData(attribute);
        return coc7SanCheck();
    }


    private String coc7SanCheck() {


        return null;

    }


}
