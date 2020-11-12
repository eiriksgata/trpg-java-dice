package indi.eiriksgata.dice.operation.impl;

import indi.eiriksgata.dice.operation.RollRole;
import org.apache.commons.lang3.RandomUtils;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.dice.operation.impl
 * date: 2020/11/12
 **/
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
                int random = RandomUtils.nextInt(7, 18 + 1);
                role.append(attributeName).append(":").append(random).append(" ");
                attributeCount = attributeCount + random;
            }
            result.append("\n").append(role).append("总计:").append(attributeCount);
        }
        return result.toString();
    }

}
