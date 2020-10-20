package indi.eiriksgata.dice.reply;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentMap;

/**
 * @author: create by Keith
 * @version: v1.0
 * @description: indi.eiriksgata.dice.reply
 * @date:2020/10/16
 **/

public class CustomText {

    private static ResourceBundle outTextConfig = ResourceBundle.getBundle("custom-text");
    private static String defaultGameType = "coc7";

    public static String getText(String key, Object... value) {
        return MessageFormat.format(new String(
                outTextConfig
                        .getString(key)
                        .getBytes(StandardCharsets.ISO_8859_1),
                StandardCharsets.UTF_8), value);
    }




}
