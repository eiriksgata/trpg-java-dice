package indi.eiriksgata.dice.operation;

import indi.eiriksgata.dice.callback.RollRandomCallback;
import indi.eiriksgata.dice.callback.SanCheckCallback;
import indi.eiriksgata.dice.exception.DiceInstructException;
import indi.eiriksgata.dice.operation.impl.AttributeCheckImpl;
import indi.eiriksgata.dice.operation.impl.SanCheckImpl;

/**
 * @author: create by Keith
 * @version: v1.0
 * @description: indi.eiriksgata.dice.operation
 * @date:2020/10/28
 **/
public interface RollBasics {

    String rollRandom(String text, Long id);

    default String attributeCheck(String text, String attribute) throws DiceInstructException {
        return new AttributeCheckImpl().attributeCheck(text, attribute);
    }

    default void sanCheck(String text, String attribute, SanCheckCallback callback) {
        new SanCheckImpl().sanCheck(text, attribute, callback);
    }

    String rollRandom(String text, Long id, RollRandomCallback callback);
}
