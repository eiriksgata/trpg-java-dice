package indi.eiriksgata.dice.utlis;

import java.math.BigDecimal;
import java.math.MathContext;

public class CalcUtil {


    private char[] val;

    private int len;

    private int inx;


    // 构造器，把公式传进去，比如： 100 + 20 * 5 + (1 + 2)
    public CalcUtil(String val) {
        val = val.replaceAll("[＋➕]", "+");
        val = val.replaceAll("[－➖]", "-");
        val = val.replaceAll("[×xX✖]", "*");
        val = val.replaceAll("[÷➗]", "/");
        this.val = val.toCharArray();
        len = this.val.length;
        inx = 0;
    }


    public BigDecimal getResult() {
        return nextValue(BigDecimal.ZERO, '+');
    }

    private BigDecimal nextValue(BigDecimal param1, char operator) {
        if (inx < len) {
            if (operator == ')' || operator == '）') {
                return param1;
            }

            if (operator == '+') {
                return param1.add(nextValue(nextParam(), inx < len ? val[inx++] : ')'));
            } else if (operator == '*') {
                return nextValue(param1.multiply(nextParam(), MathContext.DECIMAL128), inx < len ? val[inx++] : ')');
            } else if (operator == '/') {
                return nextValue(param1.divide(nextParam(), MathContext.DECIMAL128), inx < len ? val[inx++] : ')');
            }
        }
        return param1;
    }

    // 获取下一个参数
    private BigDecimal nextParam() {
        char[] param = new char[len - inx + 1];
        int paramInx = 0;
        while (inx < len) {
            if (val[inx] == '-' || val[inx] == '➖' || val[inx] == '－') {
                if (paramInx == 0) {
                    param[paramInx++] = val[inx];
                    param[paramInx++] = '0';
                } else {
                    val[--inx] = '+';
                    break;
                }
            } else if (val[inx] == '.' || ((int) val[inx] >= 48 && (int) val[inx] <= 57)) {// 如果是 . 或 0 ~ 9
                param[paramInx++] = val[inx];
            } else if (val[inx] == '(') {
                inx++;
                return nextValue(BigDecimal.ZERO, '+');
            } else if (((int) val[inx] >= 41 && (int) val[inx] <= 43) || (int) val[inx] == 47) {
                break;
            }
            inx++;
        }
        return paramInx > 0 ? new BigDecimal(param, 0, paramInx) : BigDecimal.ZERO;
    }


}