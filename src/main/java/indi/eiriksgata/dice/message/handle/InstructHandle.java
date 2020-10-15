package indi.eiriksgata.dice.message.handle;

import indi.eiriksgata.dice.injection.InstructService;
import indi.eiriksgata.dice.injection.InstructReflex;
import indi.eiriksgata.dice.instuction.DiceInstructions;
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

    public String instructCheck(String message) {
        return trackInstructCases(message);
    }

    private String trackInstructCases(String message) {
        Reflections reflections = new Reflections("indi.eiriksgata.dice");
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(InstructService.class);
        for (Class clazz : typesAnnotatedWith) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(InstructReflex.class)) {
                    InstructReflex instructReflex = method.getAnnotation(InstructReflex.class);
                    String[] instructArr = instructReflex.value();
                    for (String temp : instructArr) {
                        if (message.contains(temp)) {
                            try {
                                return (String) method.invoke(clazz.newInstance(), message);
                            } catch (Exception e) {
                                e.printStackTrace();
                                return "识别指令中出现了错误";
                            }
                        }
                    }
                }
            }
        }
        return null;
    }


}
