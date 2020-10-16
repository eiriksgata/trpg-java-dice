package indi.eiriksgata.dice.message.handle;

import indi.eiriksgata.dice.exception.DiceInstructException;
import indi.eiriksgata.dice.exception.ExceptionEnum;
import indi.eiriksgata.dice.injection.InstructService;
import indi.eiriksgata.dice.injection.InstructReflex;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author: create by Keith
 * @version: v1.0
 * @description: indi.eiriksgata.dice.message.handle
 * @date:2020/10/15
 **/

public class InstructHandle {

    //唯一公开的调用方法
    public String instructCheck(String message) throws DiceInstructException {
        //先行判断是否符合指令样式
        if (message.substring(0, 1).equals(".")) {
            return trackInstructCases(message);
        }
        throw new DiceInstructException(ExceptionEnum.DICE_INSTRUCT_NOT_FOUND);
    }

    private String trackInstructCases(String message) throws DiceInstructException {
        Reflections reflections = new Reflections("indi.eiriksgata.dice");
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(InstructService.class);
        for (Class clazz : typesAnnotatedWith) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(InstructReflex.class)) {
                    InstructReflex instructReflex = method.getAnnotation(InstructReflex.class);
                    String[] instructArr = instructReflex.value();
                    for (String temp : instructArr) {
                        //方法增强检测命令
                        if (instructMessageCheck(message, temp)) {
                            try {
                                return (String) method.invoke(clazz.newInstance(), message);
                            } catch (Exception e) {
                                e.printStackTrace();
                                throw new DiceInstructException(ExceptionEnum.DICE_INSTRUCT_HANDLE_ERR);
                            }
                        }
                    }
                }
            }
        }
        throw new DiceInstructException(ExceptionEnum.DICE_INSTRUCT_NOT_FOUND);
    }


    //仔细的指令检测方法
    private boolean instructMessageCheck(String message, String instructStr) {
        //具体的逻辑判定 该检测主要作为 统一的指令检测 若单条指令需要作检测，请在@InstrctionReflex的方法中实现

        return message.contains(instructStr);
    }


}
