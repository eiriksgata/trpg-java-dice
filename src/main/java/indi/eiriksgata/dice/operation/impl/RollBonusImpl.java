package indi.eiriksgata.dice.operation.impl;

import indi.eiriksgata.dice.exception.DiceInstructException;
import indi.eiriksgata.dice.reply.CustomText;
import indi.eiriksgata.dice.utlis.RegularExpressionUtils;

import java.util.Arrays;
import java.util.List;

import static indi.eiriksgata.dice.operation.impl.RollBasicsImpl.checkText;
import static indi.eiriksgata.dice.operation.impl.RollBasicsImpl.createRandomArray;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.dice.operation.impl
 * date: 2020/11/5
 **/
public class RollBonusImpl {

    private int getBonusData(int checkValue, int[] sortArr) {
        int resultValue = checkValue;
        if (checkValue >= 10) {
            if (sortArr[0] < checkValue / 10) {
                resultValue = (sortArr[0] * 10) + (checkValue % 10);
            }
        }
        return resultValue;
    }

    private int getPunishmentData(int checkValue, int[] sortArr) {
        int resultValue = checkValue;
        if (checkValue < 10) {
            resultValue = (sortArr[sortArr.length - 1] * 10) + checkValue;
        } else {
            if (sortArr[sortArr.length - 1] > checkValue / 10) {
                resultValue = (sortArr[sortArr.length - 1] * 10) + (checkValue % 10);
            }
        }
        return resultValue;

    }

    private void getResultText(int diceNumber, String[] resultText, String attributeName, int attributeValue, boolean isBonus) {
        try {
            createRandomArray(diceNumber, (checkValue, sortArr, randomArr) -> {

                int resultValue;
                if (isBonus) {
                    resultValue = getBonusData(checkValue, sortArr);
                } else {
                    resultValue = getPunishmentData(checkValue, sortArr);
                }
                resultText[0] = CustomText.getText("coc7.bonus", attributeName, diceNumber, checkValue, Arrays.toString(randomArr), resultValue,
                        attributeValue, checkText(resultValue, attributeValue));
            });
        } catch (DiceInstructException e) {
            resultText[0] = e.getErrMsg();
        }
    }

    public String generate(String text, String attribute, boolean isBonus) {
        final String[] resultText = new String[1];
        List<String> matchers = RegularExpressionUtils.getMatchers("[0-9]+", text);
        //满足又是多个奖励骰又有属性值
        if (matchers.size() >= 2) {
            int diceNumber = Integer.valueOf(matchers.get(0));
            int attributeValue = Integer.valueOf(matchers.get(1));
            String attributeName = text.substring(matchers.get(0).length(), text.length() - matchers.get(1).length());
            getResultText(diceNumber, resultText, attributeName, attributeValue, isBonus);
            return resultText[0];
        }

        //默认1个奖励骰 前面没有数据 后面有
        if (text.matches("[\\u4E00-\\u9FA5A-z]+[0-9]+")) {
            int diceNumber = 1;
            int attributeValue = Integer.valueOf(matchers.get(0));
            String attributeName = text.substring(matchers.get(0).length());
            getResultText(diceNumber, resultText, attributeName, attributeValue, isBonus);
            return resultText[0];
        }

        //无给定数值系列
        //检测录入属性中是否有该属性的数值
        String attributeName = text.substring(matchers.get(0).length());
        if (attribute == null || !attribute.matches(attributeName)) {
            return CustomText.getText("dice.attribute.error");
        }

        int attributeValue = Integer.valueOf(RegularExpressionUtils.getMatcher(attributeName + "[0-9]+", attribute).substring(attribute.length()));
        int diceNumber;
        //给定奖励骰数
        if (text.matches("[0-9]+[\\u4E00-\\u9FA5A-z]+")) {
            diceNumber = Integer.valueOf(matchers.get(0));
        } else {
            diceNumber = 1;
        }
        getResultText(diceNumber, resultText, attributeName, attributeValue, isBonus);
        return resultText[0];

    }


}
