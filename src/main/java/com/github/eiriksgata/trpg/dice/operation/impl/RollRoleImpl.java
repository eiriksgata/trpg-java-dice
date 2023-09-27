package com.github.eiriksgata.trpg.dice.operation.impl;

import com.github.eiriksgata.trpg.dice.operation.RollRole;
import org.apache.commons.lang3.RandomUtils;

import java.util.Arrays;

public class RollRoleImpl implements RollRole {


    @Override
    public String createCocRole(int number) {
        StringBuilder result = new StringBuilder();
        String[] attributeText = {"力量", "体质", "体型", "敏捷", "外貌", "智力", "意志", "教育", "幸运"};
        for (int i = 0; i < number; i++) {
            StringBuilder role = new StringBuilder();
            int attributeCount = 0;
            int luckyValue = 0;
            for (String attributeName : attributeText) {
                int random = RandomUtils.nextInt(5, 18 + 1);
                role.append(attributeName).append(":").append(random * 5).append(" ");
                if (attributeName.equals(attributeText[attributeText.length - 1])) {
                    luckyValue = random * 5;
                } else {
                    attributeCount = attributeCount + (random * 5);
                }
            }
            result.append("\n").append(role).append("总计:")
                    .append(attributeCount).append("/")
                    .append(attributeCount + luckyValue);
        }
        return result.toString();
    }

    @Override
    public String createDndRole(int number) {
        String[] attributeText = {"力量", "体质", "敏捷", "智力", "感知", "魅力"};
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < number; i++) {
            StringBuilder role = new StringBuilder();
            int attributeCount = 0;
            for (String attributeName : attributeText) {
                int[] tempDiceValue = new int[4];
                for (int j = 0; j < 4; j++) {
                    tempDiceValue[j] = RandomUtils.nextInt(1, 6 + 1);
                }
                Arrays.sort(tempDiceValue);
                int attributeMax = tempDiceValue[1] + tempDiceValue[2] + tempDiceValue[3];
                role.append(attributeName).append(":").append(attributeMax).append(" ");
                attributeCount = attributeCount + attributeMax;
            }
            result.append("\n").append(role).append("总计:").append(attributeCount);
        }
        return result.toString();
    }
    @Override
    public String createDnd5eRole() {
        int[] attributeValue = new int[6];
        int count = 0;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int[] tempDiceValue = new int[4];
            for (int j = 0; j < 4; j++) {
                tempDiceValue[j] = RandomUtils.nextInt(1, 6 + 1);
            }
            result.append(Arrays.toString(tempDiceValue)).append("=>");
            Arrays.sort(tempDiceValue);
            int attributeMax = tempDiceValue[1] + tempDiceValue[2] + tempDiceValue[3];
            count += attributeMax;
            attributeValue[i] = attributeMax;
            result.append(tempDiceValue[1]).append("+")
                    .append(tempDiceValue[2]).append("+")
                    .append(tempDiceValue[3]).append("+")
                    .append("=").append(attributeMax).append("\n");
        }
        result.append("\n").append("最终数值为:").append(Arrays.toString(attributeValue))
                .append(",").append("合计:").append(count);
        return result.toString();
    }


}
