package indi.eiriksgata.dice.reply;


import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author: create by Keith
 * @version: v1.0
 * @description: indi.eiriksgata.dice.reply
 * @date:2020/10/16
 **/

public class CustomText {

    public static String getText(String key, Object... value) {
        ResourceBundle customText;
        try {
            customText = ResourceBundle.getBundle("custom-text");
            return MessageFormat.format(new String(
                    customText.getString(key)
                            .getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8), value);
        } catch (MissingResourceException e) {
            customText = ResourceBundle.getBundle("default-text");
            return MessageFormat.format(new String(
                    customText.getString(key)
                            .getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8), value);
        }
    }


}
