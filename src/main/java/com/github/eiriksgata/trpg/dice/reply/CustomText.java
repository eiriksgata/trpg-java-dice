package com.github.eiriksgata.trpg.dice.reply;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.eiriksgata.trpg.dice.utlis.VersionUtils;
import org.apache.commons.lang3.RandomUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.MessageFormat;

import static org.apache.ibatis.io.Resources.getResourceAsStream;


public class CustomText {
    public static JSONObject customText;
    public static String customTextFilePath = "config/indi.eiriksgata.rulateday-dice/custom-text.json";

    public static void merge(JSONObject defaultJSONObject) {
        defaultJSONObject.forEach((key, value) -> {
            if (customText.getString(key) == null) {
                customText.put(key, value);
            }
        });
        customText.put("custom-text.version", defaultJSONObject.getString("custom-text.version"));
        try {
            fileOut(new File(customTextFilePath), customText.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static {
        try {
            customText = JSON.parseObject(new String(
                    fileRead(new File(customTextFilePath)), StandardCharsets.UTF_8
            ));
            String loadFileVersion = customText.getString("custom-text.version");
            InputStream inputStream = getResourceAsStream("custom-text.json");
            JSONObject defaultJSONObject = JSON.parseObject(new String(
                    inputStreamRead(inputStream), StandardCharsets.UTF_8
            ));
            String defaultFileVersion = defaultJSONObject.getString("custom-text.version");
            if (loadFileVersion == null) {
                merge(defaultJSONObject);
            } else {
                int result = new VersionUtils().compareVersion(loadFileVersion, defaultFileVersion);
                if (result == -1) {
                    merge(defaultJSONObject);
                }
            }

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

    //2022-05-17 增加数个文本，随机回复内容。 json string 兼容为 array<string>
    public static String getText(String key, Object... value) {
        String jsonString;
        try {
            JSONArray jsonArray = customText.getJSONArray(key);
            jsonString = jsonArray.getString(RandomUtils.nextInt(0, jsonArray.size()));
        } catch (RuntimeException e) {
            jsonString = customText.getString(key);
        }
        return MessageFormat.format(jsonString, value);
    }

    public void setCustomTextFilePath(String path) {
        customTextFilePath = path;
    }

    public static byte[] fileRead(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        int readLength;
        int countLength = 0;
        byte[] bufferContent = new byte[10485760];
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
        byte[] bufferContent = new byte[10485760];
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

    public static void fileOut(File file, String text) throws IOException {
        OutputStream output = Files.newOutputStream(file.toPath());
        output.write(text.getBytes(StandardCharsets.UTF_8));
        output.flush();
        output.close();
    }

}

