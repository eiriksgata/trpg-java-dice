package indi.eiriksgata.dice.operation;

import indi.eiriksgata.dice.callback.RollRandomCallback;
import indi.eiriksgata.dice.callback.SanCheckCallback;
import indi.eiriksgata.dice.exception.DiceInstructException;
import indi.eiriksgata.dice.operation.impl.AttributeCheckImpl;
import indi.eiriksgata.dice.operation.impl.RollBonusImpl;
import indi.eiriksgata.dice.operation.impl.SanCheckImpl;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.dice.operation
 * date:2020/10/28
 **/
public interface RollBasics {

    String rollRandom(String text, Long id);

    default String attributeCheck(String text, String attribute) throws DiceInstructException {
        return new AttributeCheckImpl().attributeCheck(text, attribute);
    }

    default String sanCheck(String text, String attribute, SanCheckCallback callback) {
        return new SanCheckImpl().sanCheck(text, attribute, callback);
    }

    default String rollBonus(String text, String attribute, boolean isBonus) {
        return new RollBonusImpl().generate(text, attribute, isBonus);
    }

    String rollRandom(String text, Long id, RollRandomCallback callback);

    int dicePoolCount(int number, StringBuilder stringBuilder, int count, int checkValue, int startNumber);

    String todayRandom(long id, int zone);
}
