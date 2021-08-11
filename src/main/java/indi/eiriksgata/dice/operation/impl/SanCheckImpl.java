package indi.eiriksgata.dice.operation.impl;

import indi.eiriksgata.calci.Expression;
import indi.eiriksgata.dice.callback.SanCheckCallback;
import indi.eiriksgata.dice.reply.CustomText;
import indi.eiriksgata.dice.utlis.RegularExpressionUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.dice.operation.impl
 * date:2020/10/28
 **/
public class SanCheckImpl {


    //本方法需要采取回调的形式使用
    public String sanCheck(String text, String attribute, SanCheckCallback callback) {

        //筛选出检测属性 san
        String sanAttribute = RegularExpressionUtils.getMatcher("san[0-9]+", attribute);
        int sanNumber = Integer.parseInt(sanAttribute.substring("san".length()));

        //分割两个不同的计算方案
        String[] formula = text.split("/");

        //coc 的理智判定 默认百面骰子
        int random = RollBasicsImpl.createRandom(1, 100)[0];
        //检测成功
        if (random <= sanNumber) {
            //理智判定默认百面骰
            AtomicInteger surplus = new AtomicInteger();
            String resultText = new RollBasicsImpl().rollRandom(formula[0], 0L, (value, calculationProcess) -> {
                try {
                    surplus.set(Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    surplus.set(-1);
                    return;
                }
                String resultAttribute = attribute.replaceFirst(sanAttribute, "san" + surplus);
                callback.getResultData(resultAttribute, random, sanNumber, calculationProcess, surplus.get());
            });
            if (surplus.get() == -1) {
                return "不能处理除整数以外的数值";
            }

            return CustomText.getText("coc7.sc.success", text, random, sanNumber, resultText, surplus.get());
        }

        //检测大失败
        if (random == 100) {
            //计算大失败的数值
            List<String> regexResult = RegularExpressionUtils.getMatchers("[0-9]?[Dd][0-9]+|[Dd]", formula[1]);
            for (String item : regexResult) {
                if (item.charAt(0) == 'D' || item.charAt(0) == 'd') {
                    if (item.length() == 1) {
                        formula[1] = formula[1].replaceFirst(item, "100");
                    } else {
                        formula[1] = formula[1].replaceFirst(item, item.substring(1));
                    }
                } else {
                    //String itemValue = RegularExpressionUtils.getMatcher("[Dd][0-9]+", item);
                    formula[1] = formula[1].replaceFirst("[dD]", "*");
                }
            }
            String formulaResult = new Expression(formula[1]).value().val.toString();
            String calProcess = text + "=" + formulaResult;
            int surplus = sanNumber - Integer.parseInt(formulaResult);
            String resultAttribute = attribute.replaceFirst(sanAttribute, "san" + surplus);
            callback.getResultData(resultAttribute, random, sanNumber, calProcess, surplus);
            return CustomText.getText("coc7.sc.big-fail", text, random, sanNumber, calProcess, surplus);


        } else {
            //理智判定默认百面骰
            AtomicInteger surplus = new AtomicInteger();
            String resultText = new RollBasicsImpl().rollRandom(formula[1], 0L, (value, calculationProcess) -> {
                try {
                    surplus.set(Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    surplus.set(-1);
                    return;
                }
                String resultAttribute = attribute.replaceFirst(sanAttribute, "san" + surplus);
                callback.getResultData(resultAttribute, random, sanNumber, calculationProcess, surplus.get());

            });
            if (surplus.get() == -1) {
                return "不能处理除整数以外的数值";
            }

            return CustomText.getText("coc7.sc.fail", text, random, sanNumber, resultText, surplus.get());
        }


    }

}

