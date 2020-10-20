package indi.eiriksgata.dice.utlis;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpressionUtils {

    /**
     * 正则表达式返回内容
     *
     * @param regex
     * @param source
     * @return
     */
    public static List<String> getMatchers(String regex, String source) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        List<String> list = new ArrayList<String>();
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }


    public static String getMatcherString(String regex, String source) {
        if (source == null) return null;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        if (!matcher.find()) return null;
        return matcher.group();
    }
}
