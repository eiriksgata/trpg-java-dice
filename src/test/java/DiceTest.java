
import indi.eiriksgata.dice.exception.DiceInstructException;
import indi.eiriksgata.dice.operation.impl.AttributeCheckImpl;
import indi.eiriksgata.dice.operation.impl.RollBasicsImpl;
import indi.eiriksgata.dice.operation.impl.RollBonusImpl;
import indi.eiriksgata.dice.operation.impl.RollRoleImpl;
import indi.eiriksgata.dice.utlis.VersionUtils;
import org.junit.jupiter.api.Test;

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
        System.out.println(resultArr[4]);

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
        System.out.println(new RollBasicsImpl().todayRandom(986426162, 8));
    }

    @Test
    void roll() {
        String result = new RollBasicsImpl().rollRandom("d20+20d50+2d3+d5", 233686862L);
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

}
