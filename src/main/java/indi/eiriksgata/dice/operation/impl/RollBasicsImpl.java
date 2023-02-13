package indi.eiriksgata.dice.operation.impl;

import indi.eiriksgata.calci.Expression;
import indi.eiriksgata.dice.callback.RollRandomCallback;
import indi.eiriksgata.dice.config.DiceConfig;
import indi.eiriksgata.dice.exception.DiceInstructException;
import indi.eiriksgata.dice.exception.ExceptionEnum;
import indi.eiriksgata.dice.operation.RollBasics;
import indi.eiriksgata.dice.reply.CustomText;
import indi.eiriksgata.dice.utlis.RegularExpressionUtils;
import indi.eiriksgata.dice.vo.RegularExpressionResult;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomUtils;

import java.security.SecureRandom;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RollBasicsImpl implements RollBasics {

    public static ConcurrentMap<Long, Integer> defaultDiceFace = new ConcurrentHashMap<>();

    @Override
    public int dicePoolCount(int number, StringBuilder stringBuilder, int count, int addDiceCheck, int startNumber, int diceFace, int successDiceCheck) {
        if (number > 0) {
            stringBuilder.append("{");
            int tempCount = 0;
            SecureRandom secureRandom = new SecureRandom();
            for (int i = 0; i < number; i++) {
                int randomNumber = secureRandom.nextInt(diceFace) + 1;
                stringBuilder.append(randomNumber);
                if (i != number - 1) {
                    stringBuilder.append(",");
                }
                if (randomNumber >= successDiceCheck) {
                    count++;
                }
                if (randomNumber >= addDiceCheck) {
                    tempCount++;
                }
            }
            stringBuilder.append("}");
            number = tempCount;
            if (number != 0) {
                stringBuilder.append("+");
            }
            return dicePoolCount(number, stringBuilder, count, addDiceCheck, startNumber, diceFace, successDiceCheck);
        }
        if (startNumber != 0) {
            stringBuilder.append("+").append(startNumber);
        }
        stringBuilder.append("=").append(count - startNumber).append("+").append(startNumber);
        stringBuilder.append("=").append(count);
        return count;
    }


    @Override
    public String todayRandom(long id, int zone) {
        long timestamp = (System.currentTimeMillis() + (1000L * 60 * 60 * zone)) / (1000 * 60 * 60 * 24);
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(timestamp + id);
        int result = secureRandom.nextInt(101);
        return CustomText.getText("dice.jrrp.success", result);
    }

    @Override
    public String rollRandom(String text, Long id) {
        return rollRandom(text, id, (value, calProcess) -> {
        });
    }

    @Override
    public String rollRandom(String text, Long id, RollRandomCallback callback) {
        text = text.toUpperCase().trim();
        String inputFormula = text;
        int offsetValue = 0;
        List<RegularExpressionResult> list = RegularExpressionUtils.getMatchersResult("[0-9]*[Dd][0-9]+|[0-9]+[Dd]|[Dd]", text);
        for (RegularExpressionResult temp : list) {
            if (temp.getMatcherText().charAt(0) == 'D') {
                if (temp.getMatcherText().length() == 1) {
                    if (defaultDiceFace.get(id) == null) {
                        String diceType = DiceConfig.diceSet.getString("dice.type");
                        int diceFace = Integer.parseInt(DiceConfig.diceSet.getString(diceType + ".face"));
                        text = text.replaceFirst(temp.getMatcherText(), String.valueOf(createRandom(1, diceFace)[0]));
                        inputFormula = inputFormula.substring(0, temp.getStartIndex() + offsetValue) + "D" + diceFace + inputFormula.substring(temp.getEndIndex() + offsetValue);
                        offsetValue += ("" + diceFace).length();

                    } else {
                        text = text.replaceFirst(temp.getMatcherText(), String.valueOf(createRandom(1, defaultDiceFace.get(id))[0]));
                        inputFormula = inputFormula.substring(0, temp.getStartIndex() + offsetValue) + "D" + defaultDiceFace.get(id) + inputFormula.substring(temp.getEndIndex() + offsetValue);
                        offsetValue += ("" + defaultDiceFace.get(id)).length();
                    }
                } else {
                    int[] diceRandom = createRandom(1, Integer.parseInt(temp.getMatcherText().substring(1)));
                    text = text.replaceFirst(temp.getMatcherText(), String.valueOf(diceRandom[0]));
                }
            } else {
                String[] dataSplitArr = temp.getMatcherText().split("[dD]");
                int diceNumber;
                int diceFace;
                if (dataSplitArr.length == 1) {
                    diceNumber = Integer.parseInt(dataSplitArr[0]);
                    if (defaultDiceFace.get(id) == null) {
                        String diceType = DiceConfig.diceSet.getString("dice.type");
                        diceFace = Integer.parseInt(DiceConfig.diceSet.getString(diceType + ".face"));
                    } else {
                        diceFace = defaultDiceFace.get(id);
                    }
                    String replaceText = diceNumber + "D" + diceFace;
                    inputFormula = inputFormula.substring(0, temp.getStartIndex() + offsetValue) + replaceText + inputFormula.substring(temp.getEndIndex() + offsetValue);
                    offsetValue += ("" + diceFace).length();

                } else {
                    diceNumber = Integer.parseInt(dataSplitArr[0]);
                    diceFace = Integer.parseInt(dataSplitArr[1]);
                }
                int[] randomData = createRandom(diceNumber, diceFace);
                if (randomData.length > 1) {
                    StringBuilder formula = new StringBuilder();
                    formula.append("(").append(randomData[0]);
                    for (int j = 1; j < randomData.length; j++) {
                        formula.append("+").append(randomData[j]);
                    }
                    formula.append(")");
                    text = text.replaceFirst(temp.getMatcherText(), String.valueOf(formula));
                } else {
                    text = text.replaceFirst(temp.getMatcherText(), String.valueOf(randomData[0]));
                }
            }
        }
        String result = new Expression(text).value().val.toString();
        callback.getFormulaResult(result, text);

        if (!text.contains("+")) {
            return CustomText.getText("coc7.roll1", inputFormula, result);
        }
        return CustomText.getText("coc7.roll1", inputFormula + "=" + text, result);

    }

    static int[] createRandom(int diceNumber, int faceNumber) {
        int[] result = new int[diceNumber];
        for (int i = 0; i < diceNumber; i++) {
            result[i] = RandomUtils.nextInt(1, faceNumber + 1);
        }
        return result;
    }

    static String checkText(int randomValue, int attributeValue) {
        int roomRuleValue = Integer.parseInt(DiceConfig.diceSet.getString("coc7.rules"));

        //程度判断
        if (randomValue <= roomRuleValue) {
            return CustomText.getText("text.big-success");
        }

        if (randomValue <= attributeValue / 5) {
            return CustomText.getText("text.ex-success");
        }

        if (randomValue <= attributeValue / 2) {
            return CustomText.getText("text.dif-success");
        }

        if (randomValue <= attributeValue) {
            return CustomText.getText("text.success");
        }

        //判断是否为大失败
        if (randomValue > 100 - roomRuleValue) {
            return CustomText.getText("text.big-fail");
        }

        return CustomText.getText("text.fail");
    }

    static void createRandomArray(int bonusNumber, RollArrayCallback callback) throws DiceInstructException {
        if (bonusNumber > Integer.parseInt(DiceConfig.diceSet.getString("dice.number.max"))
                || bonusNumber < Integer.parseInt(DiceConfig.diceSet.getString("dice.number.min"))) {
            throw new DiceInstructException(ExceptionEnum.DICE_NUMBER_OUT_BOUNDS_ERR);
        }
        int randomCheckNumber = RandomUtils.nextInt(1, 101);
        int[] randomArr = new int[bonusNumber];
        int[] sortArr = new int[bonusNumber];
        for (int i = 0; i < bonusNumber; i++) {
            randomArr[i] = RandomUtils.nextInt(1, 11);
            sortArr[i] = randomArr[i];
        }
        for (int i = 0; i < bonusNumber; i++) {
            for (int j = 0; j < bonusNumber; j++) {
                if (sortArr[i] < sortArr[j]) {
                    int temp = sortArr[i];
                    sortArr[i] = sortArr[j];
                    sortArr[j] = temp;
                }
            }
        }
        callback.getResultData(randomCheckNumber, sortArr, randomArr);
    }

    public static String integerShuffle(int maxValue) {
        if (maxValue > 100 || maxValue < 2) {
            return "超出生成的数值范围，请输入2-100范围内";
        }
        int[] array = new int[maxValue];
        for (int i = 0; i < maxValue; i++) {
            array[i] = i + 1;
        }
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < array.length; i++) {
            int randomNumberA = random.nextInt(array.length);
            int randomNumberB = random.nextInt(array.length);
            int tempNumber = array[randomNumberB];
            array[randomNumberB] = array[randomNumberA];
            array[randomNumberA] = tempNumber;
        }
        return ArrayUtils.toString(array);
    }


}
