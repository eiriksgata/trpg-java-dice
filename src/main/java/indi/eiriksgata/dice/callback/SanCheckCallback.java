package indi.eiriksgata.dice.callback;

/**
 *  author: create by Keith
 *  version: v1.0
 *  description: indi.eiriksgata.dice.callback
 *  date:2020/10/26
 **/
public interface SanCheckCallback {

    void getResultData(String attribute, int random, int sanValue, String calculationProcess, int surplus);

}
