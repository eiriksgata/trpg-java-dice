package indi.eiriksgata.dice.operation;

import indi.eiriksgata.dice.callback.SanCheckCallback;
import indi.eiriksgata.dice.config.DiceConfig;
import indi.eiriksgata.dice.exception.DiceInstructException;
import indi.eiriksgata.dice.exception.ExceptionEnum;
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
public class RollBasicsImpl {

    static ConcurrentMap<Long, Integer> defaultDiceFace = new ConcurrentHashMap<>();

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


    public String attributeCheck(String text, String attribute) throws DiceInstructException {

        //检测两种不同的输入方式
        //输入中携带有数值
        if (text.matches("[\\u4E00-\\u9FA5A-z]+[0-9]+")) {
            String attributeName = RegularExpressionUtils.getMatcher("[\\u4E00-\\u9FA5A-z]+", text);
            String attributeValue = RegularExpressionUtils.getMatcher("[0-9]+", text);
            return attributeDegree(attributeName, Integer.valueOf(attributeValue));
        }

        //输入中不携带有数值
        if (text.matches("[\\u4E00-\\u9FA5A-z]+")) {
            //从attribute中获取
            String regex = text + "[0-9]+";
            String selectData = RegularExpressionUtils.getMatcher(regex, attribute);
            if (selectData == null) {
                throw new DiceInstructException(ExceptionEnum.DICE_INSTRUCT_PARAMETER_ERR);
            }
            return attributeDegree(text, Integer.valueOf(selectData.substring(text.length())));
        }

        //指令参数不符合要求
        throw new DiceInstructException(ExceptionEnum.DICE_INSTRUCT_PARAMETER_ERR);

    }


    private String attributeDegree(String attributeName, int attributeValue) {

        //生成随机数 属性确认默认生成百面骰
        int faceNumber = 100;
        int randomValue = createRandom(1, faceNumber)[0];


        //rule value 暂时按骰子全局配置文件负责 以后可能会以个人用户 或者房间数据来进行
        int roomRuleValue = Integer.valueOf(DiceConfig.diceSet.getString("coc7.rules"));

        //程度判断
        if (randomValue <= roomRuleValue) {
            return CustomText.getText("coc7.attribute.big-success", "D" + faceNumber, randomValue, attributeValue, attributeName);
        }

        if (randomValue <= attributeValue / 5) {
            return CustomText.getText("coc7.attribute.ex-success", "D" + faceNumber, randomValue, attributeValue, attributeName);
        }

        if (randomValue <= attributeValue / 2) {
            return CustomText.getText("coc7.attribute.dif-success", "D" + faceNumber, randomValue, attributeValue, attributeName);
        }

        if (randomValue <= attributeValue) {
            return CustomText.getText("coc7.attribute.success", "D" + faceNumber, randomValue, attributeValue, attributeName);
        }

        //判断是否为大失败
        if (randomValue > 100 - roomRuleValue) {
            return CustomText.getText("coc7.attribute.big-fail", "D" + faceNumber, randomValue, attributeValue, attributeName);

        }

        //剩下的只有失败
        return CustomText.getText("coc7.attribute.fail", "D" + faceNumber, randomValue, attributeValue, attributeName);

    }


    public String sanCheck(String text, String attribute, SanCheckCallback callback) {


        // RegularExpressionUtils.getMatchers("")


        callback.getResultData(attribute);
        return coc7SanCheck();
    }


    private String coc7SanCheck() {


        return null;

    }

    private int[] createRandom(int diceNumber, int faceNumber) {
        int[] result = new int[diceNumber];
        for (int i = 0; i < diceNumber; i++) {
            result[i] = RandomUtils.nextInt(1, faceNumber + 1);
        }
        return result;
    }


}
