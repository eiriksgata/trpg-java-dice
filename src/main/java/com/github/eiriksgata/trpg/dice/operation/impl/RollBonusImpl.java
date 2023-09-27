package com.github.eiriksgata.trpg.dice.operation.impl;

import com.github.eiriksgata.trpg.dice.exception.DiceInstructException;
import com.github.eiriksgata.trpg.dice.reply.CustomText;
import com.github.eiriksgata.trpg.dice.utlis.RegularExpressionUtils;

import java.util.Arrays;
import java.util.List;

import static com.github.eiriksgata.trpg.dice.operation.impl.RollBasicsImpl.checkText;
import static com.github.eiriksgata.trpg.dice.operation.impl.RollBasicsImpl.createRandomArray;

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
                    resultText[0] = CustomText.getText("coc7.bonus", attributeName, diceNumber, checkValue, Arrays.toString(randomArr), resultValue,
                            attributeValue, checkText(resultValue, attributeValue));
                } else {
                    resultValue = getPunishmentData(checkValue, sortArr);
                    resultText[0] = CustomText.getText("coc7.punishment", attributeName, diceNumber, checkValue, Arrays.toString(randomArr), resultValue,
                            attributeValue, checkText(resultValue, attributeValue));
                }

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
            int diceNumber = Integer.parseInt(matchers.get(0));
            int attributeValue = Integer.parseInt(matchers.get(1));
            String attributeName = text.substring(matchers.get(0).length(), text.length() - matchers.get(1).length());
            getResultText(diceNumber, resultText, attributeName, attributeValue, isBonus);
            return resultText[0];
        }

        //默认1个奖励骰 前面没有数据 后面有
        if (text.matches("[\\u4E00-\\u9FA5A-z]+[0-9]+")) {
            int diceNumber = 1;
            int attributeValue = Integer.parseInt(matchers.get(0));
            String attributeName = text.substring(0, text.length() - matchers.get(0).length());
            getResultText(diceNumber, resultText, attributeName, attributeValue, isBonus);
            return resultText[0];
        }

        int diceNumber;
        if (text.matches("[0-9]+[\\u4E00-\\u9FA5A-z]+")) {
            diceNumber = Integer.parseInt(matchers.get(0));
            String attributeName = text.substring(matchers.get(0).length());
            int attributeValue;
            try {
                attributeValue = Integer.parseInt(RegularExpressionUtils.getMatcher(attributeName + "[0-9]+", attribute).substring(attributeName.length()));
            } catch (Exception e) {
                return CustomText.getText("coc7.bonus.error");
            }
            getResultText(diceNumber, resultText, attributeName, attributeValue, isBonus);
        } else {
            try {
                diceNumber = Integer.parseInt(text);
            } catch (Exception e) {
                diceNumber = 1;
            }
            if (attribute == null || !attribute.contains(text)) {
                try {
                    int finalDiceNumber = diceNumber;
                    createRandomArray(diceNumber, (checkValue, sortArr, randomArr) -> {
                        int resultValue;
                        if (isBonus) {
                            resultValue = getBonusData(checkValue, sortArr);
                            resultText[0] = CustomText.getText("coc7.bonus.easy",
                                    finalDiceNumber, checkValue, Arrays.toString(randomArr), resultValue);
                        } else {
                            resultValue = getPunishmentData(checkValue, sortArr);
                            resultText[0] = CustomText.getText("coc7.punishment.easy",
                                    finalDiceNumber, checkValue, Arrays.toString(randomArr), resultValue);
                        }

                    });
                } catch (DiceInstructException e) {
                    return e.getErrMsg();
                }
                return resultText[0];
            }
            int attributeValue = Integer.parseInt(RegularExpressionUtils.getMatcher(text + "[0-9]+", attribute).substring(text.length()));
            getResultText(diceNumber, resultText, text, attributeValue, isBonus);
        }
        return resultText[0];
    }


}
