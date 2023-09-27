
import com.github.eiriksgata.trpg.dice.callback.SanCheckCallback;
import com.github.eiriksgata.trpg.dice.exception.DiceInstructException;
import com.github.eiriksgata.trpg.dice.operation.impl.AttributeCheckImpl;
import com.github.eiriksgata.trpg.dice.operation.impl.RollBasicsImpl;
import com.github.eiriksgata.trpg.dice.operation.impl.RollBonusImpl;
import com.github.eiriksgata.trpg.dice.operation.impl.RollRoleImpl;
import com.github.eiriksgata.trpg.dice.utlis.VersionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author: create by Keith
 * version: v1.0
 * description: PACKAGE_NAME
 * date:2020/10/23
 **/
public class DiceTest {

    @Test
    void randomTest() {
        String text = "3d";
        String[] resultArr = text.split("d");
        System.out.println(resultArr.length);
        System.out.println(Arrays.toString(resultArr));

    }

    @Test
    void randAttribute() {
        try {
            String result = new AttributeCheckImpl().attributeCheck("侦察50", "");

            System.out.println(result);
        } catch (DiceInstructException e) {
            e.printStackTrace();
        }
    }

    @Test
    void randomSort() {

    }


    @Test
    void bonus() {
        String result = new RollBonusImpl().generate("10000san30", "san50", true);
        System.out.println(result);
    }

    @Test
    void punishment() {

    }


    @Test
    void createRole() {
        System.out.println(new RollRoleImpl().createCocRole(5));
        System.out.println(new RollRoleImpl().createDndRole(5));

    }

    @Test
    void jrrpTest() {
        for (int i = 0; i < 1000; i++) {
            System.out.println(new RollBasicsImpl().todayRandom(986426162, 8));
        }
    }

    @Test
    void roll() {
        String result = new RollBasicsImpl().rollRandom("d+5d+3d2+1d3+d+5d6+d", 233686862L);
        System.out.println(result);
    }

    @Test
    void calcUtil() {
    }

    @Test
    void dicePool() {
        StringBuilder stringBuilder = new StringBuilder();
        System.out.println(new RollBasicsImpl().dicePoolCount(10, stringBuilder, 0, 10, 0, 10, 8));
        System.out.println(stringBuilder);
    }

    @Test
    void versionUtilTest() {
        int result = new VersionUtils().compareVersion("1.0.0", "1.0.1");
        System.out.println(result);
    }

    @Test
    void randomUtilsNextInt() {
        for (int i = 0; i < 2000; i++) {
            System.out.println(RandomUtils.nextInt(0, 2));
        }
    }

    @Test
    void sanCheckTest() {
        String result = new RollBasicsImpl().sanCheck("1d5+2d3+5/1d100", "san50", new SanCheckCallback() {
            @Override
            public void getResultData(String attribute, int random, int sanValue, String calculationProcess, int surplus) {
                System.out.println(attribute + "," + random + "," + sanValue + "," + calculationProcess + "," + surplus);
            }
        });
        System.out.println(result);
    }

    @Test
    void regex() {
        String regex = "[0-9]*[Dd][0-9]+|[0-9]+[Dd]|[Dd]";
        String source = "d+2d+5d6+d+2d3+d";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            System.out.println("start:" + matcher.group());
            System.out.println("start:" + matcher.start() + " end:" + matcher.end());
        }


    }
}
