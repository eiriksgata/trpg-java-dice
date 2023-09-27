package com.github.eiriksgata.trpg.dice.config;

import java.util.ResourceBundle;

public class DiceConfig {
    public static ResourceBundle diceSet = ResourceBundle.getBundle("dice-config");
    public static String language = diceSet.getString("dice.language");

}
