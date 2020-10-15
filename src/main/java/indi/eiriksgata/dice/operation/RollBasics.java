package indi.eiriksgata.dice.operation;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.util.concurrent.ConcurrentMap;


/**
 * @author: create by Keith
 * @version: v1.0
 * @description: indi.eiriksgata.dice.operation
 * @date:2020/10/13
 **/
public class RollBasics {

    public static String diceGameType = "coc7";

    public String rollRandom() {


        return null;
    }


    private int[] createRandom(int diceNumber, int faceNumber) {
        int[] result = new int[diceNumber];
        //ConcurrentMap<String, Object> diceConfig = new Yaml().load("dice-config.yaml");
        //int diceValueMax = (int) diceConfig.get("face-number");
        for (int i = 0; i < diceNumber; i++) {
            result[i] = RandomUtils.nextInt(1, faceNumber + 1);
        }
        return result;
    }


}
