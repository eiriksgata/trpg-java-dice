package com.github.eiriksgata.trpg.dice.operation.impl;

import com.github.eiriksgata.trpg.dice.reply.CustomText;
import com.github.eiriksgata.trpg.dice.config.DiceConfig;
import com.github.eiriksgata.trpg.dice.exception.DiceInstructException;
import com.github.eiriksgata.trpg.dice.exception.ExceptionEnum;
import com.github.eiriksgata.trpg.dice.utlis.RegularExpressionUtils;

public class AttributeCheckImpl {


    public String attributeCheck(String text, String attribute) throws DiceInstructException {

        //检测两种不同的输入方式
        //输入中携带有数值
        if (text.matches("[\\u4E00-\\u9FA5A-z]+[0-9]+")) {
            String attributeName = RegularExpressionUtils.getMatcher("[\\u4E00-\\u9FA5A-z]+", text);
            String attributeValue = RegularExpressionUtils.getMatcher("[0-9]+", text);
            return attributeDegree(attributeName, Integer.parseInt(attributeValue));
        }

        //输入中不携带有数值
        if (text.matches("[\\u4E00-\\u9FA5A-z]+")) {
            //从attribute中获取
            String regex = text + "[0-9]+";
            String selectData = RegularExpressionUtils.getMatcher(regex, attribute);
            if (selectData == null) {
                throw new DiceInstructException(ExceptionEnum.DICE_INSTRUCT_PARAMETER_ERR);
            }
            return attributeDegree(text, Integer.parseInt(selectData.substring(text.length())));
        }

        //指令参数不符合要求
        throw new DiceInstructException(ExceptionEnum.DICE_INSTRUCT_PARAMETER_ERR);

    }


    private String attributeDegree(String attributeName, int attributeValue) {

        //生成随机数 属性确认默认生成百面骰
        int faceNumber = 100;
        int randomValue = RollBasicsImpl.createRandom(1, faceNumber)[0];


        //rule value 暂时按骰子全局配置文件负责 以后可能会以个人用户 或者房间数据来进行
        int roomRuleValue = Integer.parseInt(DiceConfig.diceSet.getString("coc7.rules"));

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


}
