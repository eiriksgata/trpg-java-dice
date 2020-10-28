package indi.eiriksgata.dice.operation.impl;

import indi.eiriksgata.dice.config.DiceConfig;
import indi.eiriksgata.dice.operation.RollBasics;
import indi.eiriksgata.dice.reply.CustomText;
import indi.eiriksgata.dice.utlis.CalcUtil;
import indi.eiriksgata.dice.utlis.RegularExpressionUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * @author: create by Keith
 * @version: v1.0
 * @description: indi.eiriksgata.dice.operation
 * @date:2020/10/13
 **/
public class RollBasicsImpl implements RollBasics {

    public static ConcurrentMap<Long, Integer> defaultDiceFace = new ConcurrentHashMap<>();

    @Override
    public String rollRandom(String text, Long id) {
        String inputFormula = text;
        List<String> list = RegularExpressionUtils.getMatchers("[0-9]?[Dd][0-9]?", text);
        for (String temp : list) {
            if (temp.substring(0, 1).equals("d") ||
                    temp.substring(0, 1).equals("D")) {
                if (temp.length() == 1) {
                    if (defaultDiceFace.get(id) == null) {
                        String diceType = DiceConfig.diceSet.getString("dice.type");
                        int diceFace = Integer.valueOf(DiceConfig.diceSet.getString(diceType + ".face"));
                        text = text.replaceFirst(temp, String.valueOf(createRandom(1, diceFace)[0]));
                        inputFormula = inputFormula.replaceFirst(temp, "D" + diceFace);
                    } else {
                        text = text.replaceFirst(temp, String.valueOf(createRandom(1, defaultDiceFace.get(id))[0]));
                    }
                } else {
                    int[] diceRandom = createRandom(1, Integer.valueOf(temp.substring(1)));
                    text = text.replaceFirst(temp, String.valueOf(diceRandom[0]));
                }
            } else {
                String[] dataSplitArr = temp.split("[dD]");
                int diceNumber = Integer.valueOf(dataSplitArr[0]);
                int diceFace = Integer.valueOf(dataSplitArr[1]);
                int[] randomData = createRandom(diceNumber, diceFace);
                if (randomData.length > 1) {
                    StringBuilder formula = new StringBuilder();
                    formula.append("(").append(randomData[0]);
                    for (int j = 1; j < randomData.length; j++) {
                        formula.append("+").append(randomData[j]);
                    }
                    formula.append(")");
                    text = text.replaceFirst(temp, String.valueOf(formula));
                } else {
                    text = text.replaceFirst(temp, String.valueOf(randomData[0]));
                }
            }
        }
        return CustomText.getText("coc7.roll", inputFormula, text, new CalcUtil(text).getResult().toString());
    }

    



    static int[] createRandom(int diceNumber, int faceNumber) {
        int[] result = new int[diceNumber];
        for (int i = 0; i < diceNumber; i++) {
            result[i] = RandomUtils.nextInt(1, faceNumber + 1);
        }
        return result;
    }


}
