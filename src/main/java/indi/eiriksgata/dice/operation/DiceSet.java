package indi.eiriksgata.dice.operation;

import indi.eiriksgata.dice.operation.impl.RollBasicsImpl;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.dice.operation
 * date:2020/10/20
 **/
public class DiceSet {

    public void setDiceFace(Long id, int diceFace) {
        RollBasicsImpl.defaultDiceFace.put(id, diceFace);
    }

}
