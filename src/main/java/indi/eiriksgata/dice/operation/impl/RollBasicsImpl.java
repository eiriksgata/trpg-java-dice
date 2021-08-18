package indi.eiriksgata.dice.operation.impl;

import indi.eiriksgata.calci.Expression;
import indi.eiriksgata.dice.callback.RollRandomCallback;
import indi.eiriksgata.dice.config.DiceConfig;
import indi.eiriksgata.dice.exception.DiceInstructException;
import indi.eiriksgata.dice.exception.ExceptionEnum;
import indi.eiriksgata.dice.operation.RollBasics;
import indi.eiriksgata.dice.reply.CustomText;
import indi.eiriksgata.dice.utlis.RegularExpressionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomUtils;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RollBasicsImpl implements RollBasics {

    public static ConcurrentMap<Long, Integer> defaultDiceFace = new ConcurrentHashMap<>();

    @Override
    public int dicePoolCount(int number, StringBuilder stringBuilder, int count, int checkValue, int startNumber) {
        if (number > 0) {
            stringBuilder.append("{");
            int tempCount = 0;
            SecureRandom secureRandom = new SecureRandom();
            for (int i = 0; i < number; i++) {
                int randomNumber = secureRandom.nextInt(10) + 1;
                stringBuilder.append(randomNumber);
                if (i != number - 1) {
                    stringBuilder.append(",");
                }
                if (randomNumber >= 8) {
                    count++;
                }
                if (randomNumber >= checkValue) {
                    tempCount++;
                }
            }
            stringBuilder.append("}");
            number = tempCount;
            if (number != 0) {
                stringBuilder.append("+");
            }
            return dicePoolCount(number, stringBuilder, count, checkValue, startNumber);
        }
        if (startNumber != 0) {
            stringBuilder.append("+").append(startNumber);
        }

        stringBuilder.append("=").append(count);
        return count;
    }


    @Override
    public String todayRandom(long id, int zone) {
        long timestamp = (System.currentTimeMillis() + (1000L * 60 * 60 * zone)) / (1000 * 60 * 60 * 24);
        int result = new Random(timestamp + id).nextInt(100);
        return CustomText.getText("dice.jrrp.success", result);
    }

    @Override
    public String rollRandom(String text, Long id) {
        return rollRandom(text, id, (value, calProcess) -> {
        });
    }

    @Override
    public String rollRandom(String text, Long id, RollRandomCallback callback) {
        String inputFormula = text;
        List<String> list = RegularExpressionUtils.getMatchers("[0-9]?[Dd][0-9]+|[0-9]+[Dd]|[Dd]", text);
        for (String temp : list) {
            if (temp.charAt(0) == 'd' ||
                    temp.charAt(0) == 'D') {
                if (temp.length() == 1) {
                    if (defaultDiceFace.get(id) == null) {
                        String diceType = DiceConfig.diceSet.getString("dice.type");
                        int diceFace = Integer.parseInt(DiceConfig.diceSet.getString(diceType + ".face"));
                        text = text.replaceFirst(temp, String.valueOf(createRandom(1, diceFace)[0]));
                        inputFormula = inputFormula.replaceFirst(temp, "D" + diceFace);

                    } else {
                        text = text.replaceFirst(temp, String.valueOf(createRandom(1, defaultDiceFace.get(id))[0]));
                        inputFormula = inputFormula.replaceFirst(temp, "D" + defaultDiceFace.get(id));

                    }
                } else {
                    int[] diceRandom = createRandom(1, Integer.parseInt(temp.substring(1)));
                    text = text.replaceFirst(temp, String.valueOf(diceRandom[0]));
                }
            } else {
                String[] dataSplitArr = temp.split("[dD]");
                int diceNumber;
                int diceFace;
                if (dataSplitArr.length == 1) {
                    diceNumber = Integer.parseInt(dataSplitArr[0]);
                    if (defaultDiceFace.get(id) == null) {
                        String diceType = DiceConfig.diceSet.getString("dice.type");
                        diceFace = Integer.parseInt(DiceConfig.diceSet.getString(diceType + ".face"));

                        //diceFace = 100;
                    } else {
                        diceFace = defaultDiceFace.get(id);
                    }
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
                    text = text.replaceFirst(temp, String.valueOf(formula));
                } else {
                    text = text.replaceFirst(temp, String.valueOf(randomData[0]));
                }
            }
        }

        String result = new Expression(text).value().val.toString();
        callback.getFormulaResult(result, text);
        return CustomText.getText("coc7.roll", inputFormula, text, result);

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
            randomArr[i] = RandomUtils.nextInt(0, 10);
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
        Random random = new Random();
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
