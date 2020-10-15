package indi.eiriksgata.dice.instuction;

import indi.eiriksgata.dice.injection.InstructService;
import indi.eiriksgata.dice.injection.InstructReflex;

/**
 * @author: create by Keith
 * @version: v1.0
 * @description: indi.eiriksgata.dice
 * @date:2020/9/24
 **/
@InstructService
public class DiceInstructions {

    @InstructReflex(value = {".ra", ".rc"})
    public String attributeCheck(String message) {
        return "you input .ra";
    }


    @InstructReflex(value = {".rd"})
    public String rdCheck(String message) {
        return "you input .rd";
    }

}
