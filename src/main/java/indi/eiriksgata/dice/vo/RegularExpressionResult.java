package indi.eiriksgata.dice.vo;

import lombok.Data;

@Data
public class RegularExpressionResult {
    private String matcherText;
    private int startIndex;
    private int endIndex;

}
