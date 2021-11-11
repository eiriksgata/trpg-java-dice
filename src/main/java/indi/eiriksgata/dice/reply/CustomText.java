package indi.eiriksgata.dice.reply;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

import static org.apache.ibatis.io.Resources.getResourceAsStream;


public class CustomText {
    private static JSONObject customText;
    private static String customTextFilePath = "config/indi.eiriksgata.rulateday-dice/custom-text.json";

    static {
        try {
            customText = JSON.parseObject(new String(
                    fileRead(new File(customTextFilePath)), StandardCharsets.UTF_8
            ));
        } catch (IOException e) {
            try {
                InputStream inputStream = getResourceAsStream("default-text.json");
                customText = JSON.parseObject(new String(
                        inputStreamRead(inputStream), StandardCharsets.UTF_8
                ));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static String getText(String key, Object... value) {
        return MessageFormat.format(customText.getString(key), value);
    }

    public void setCustomTextFilePath(String path) {
        customTextFilePath = path;
    }

    public static byte[] fileRead(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        int readLength;
        int countLength = 0;
        byte[] bufferContent = new byte[1048576];
        while (true) {
            readLength = fileInputStream.read(bufferContent, countLength, 1024);
            if (readLength == -1) {
                break;
            }
            countLength += readLength;
        }
        byte[] result = new byte[countLength];
        System.arraycopy(bufferContent, 0, result, 0, countLength);
        return result;
    }

    public static byte[] inputStreamRead(InputStream inputStream) throws IOException {
        int readLength;
        int countLength = 0;
        byte[] bufferContent = new byte[1048576];
        while (true) {
            readLength = inputStream.read(bufferContent, countLength, 1024);
            if (readLength == -1) {
                break;
            }
            countLength += readLength;
        }
        byte[] result = new byte[countLength];
        System.arraycopy(bufferContent, 0, result, 0, countLength);
        return result;
    }

}

