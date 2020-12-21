import indi.eiriksgata.dice.callback.SanCheckCallback;
import indi.eiriksgata.dice.operation.impl.RollBasicsImpl;
import indi.eiriksgata.dice.operation.impl.RollBonusImpl;
import indi.eiriksgata.dice.operation.impl.RollRoleImpl;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @author: create by Keith
 * @version: v1.0
 * @description: PACKAGE_NAME
 * @date:2020/10/23
 **/
public class DiceTest {

    @Test
    void randomTest() {
        String text = "3d";
        String[] resultArr = text.split("d");
        System.out.println(resultArr.length);
        System.out.println(resultArr[0]);

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


}
