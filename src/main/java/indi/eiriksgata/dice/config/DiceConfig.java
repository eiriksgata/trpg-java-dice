package indi.eiriksgata.dice.config;

import java.util.ResourceBundle;

/**
 * @author: create by Keith
 * @version: v1.0
 * @description: indi.eiriksgata.dice.config
 * @date:2020/10/19
 **/
public class DiceConfig {
    public static ResourceBundle diceSet = ResourceBundle.getBundle("dice-config");
    public static String language = diceSet.getString("dice.language");


}
