package com.github.eiriksgata.trpg.dice.operation;

import com.github.eiriksgata.trpg.dice.operation.impl.RollBasicsImpl;

public class DiceSet {

    public void setDiceFace(Long id, int diceFace) {
        RollBasicsImpl.defaultDiceFace.put(id, diceFace);
    }

}
