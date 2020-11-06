package indi.eiriksgata.dice.operation.impl;

import indi.eiriksgata.dice.utlis.RegularExpressionUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.dice.operation.impl
 * date: 2020/11/5
 **/
public class RollBonusImpl {


    public String generate(String text, String attribute) {

        //满足又是多个奖励骰 并且又给定数值
        if (text.matches("[0-9]+[\\u4E00-\\u9FA5A-z]+[0-9]+")) {
            List<String> matchers = RegularExpressionUtils.getMatchers("[0-9]+", text);


        }

        //给定的奖励骰数量
        if (text.matches("[0-9]+[\\u4E00-\\u9FA5A-z]+")) {

        } else {


        }

        return null;

    }


    public void randomHandle(int bonusNumber, int checkValue) {

        int randomCheckNumber = RandomUtils.nextInt(1, 101);
        int resultValue = 0;
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
        resultValue = randomCheckNumber;
        if (randomCheckNumber >= 10) {
            if (sortArr[0] < randomCheckNumber / 10) {
                resultValue = (sortArr[0] * 10) + (randomCheckNumber % 10);
            }
        }

        
    }


}
