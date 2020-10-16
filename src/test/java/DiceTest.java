import indi.eiriksgata.dice.exception.DiceInstructException;
import indi.eiriksgata.dice.exception.ExceptionEnum;
import indi.eiriksgata.dice.message.handle.InstructHandle;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.text.MessageFormat;
import java.util.Map;

/**
 * @author: create by Keith
 * @version: v1.0
 * @description: PACKAGE_NAME
 * @date:2020/10/12
 **/
public class DiceTest {

    @Test
    void test1() {


    }

    @Test
    void yamlReadTest() {
        Map<String, Object> testObject =
                new Yaml().load(
                        DiceTest.class.getClassLoader()
                                .getResourceAsStream("dice-config.yaml"));
        System.out.println(testObject);

    }


    @Test
    void stringFormatTest() {
        String result = MessageFormat.format("a:{0},b:{1}", 1, 2);
        System.out.println(result);
    }

    @Test
    void stringArrTest() {
        String[] test = {"123", "345"};
        System.out.println(test[1]);
    }


    @Test
    void instructTest() {
        String result;
        try {
            result = new InstructHandle().instructCheck(".raasdasdasd");
        } catch (DiceInstructException e) {
            if (e.getErrCode().equals(ExceptionEnum.DICE_INSTRUCT_HANDLE_ERR.getErrCode())) {
                result = e.getErrMsg();
            }
            //e.printStackTrace();
            return;
        }
        System.out.println(result);
    }

}
