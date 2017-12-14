package com.quick.core.util.common;

import android.text.TextUtils;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by dailichun on 2017/12/6.
 * json数据解析类
 */
public class JsonUtil {

    /**
     * Json通用解析类 获取对象列表数据 例如：
     * [ {item:{"a":"1","b":"2"}},
     * {item:{"a":"1","b":"2"}},
     * {item:{"a":"1","b":"2"}},
     * {item:{"a":"1","b":"2"}},
     * {item:{"a":"1","b":"2"}} ]
     *
     * @param objectArray
     * @param c           类
     * @param itemTitle   二级列表名称 例如:item
     * @return List对象
     */
    public static List parseJsonArray(JsonArray objectArray, Class<?> c, String itemTitle) {
        ArrayList list = new ArrayList();
        Gson gson = new Gson();
        JsonObject object;
        String tempStr;
        for (JsonElement je : objectArray) {
            object = je.getAsJsonObject();
            object = object.get(itemTitle).getAsJsonObject();
            tempStr = object.toString();
            Object nc = gson.fromJson(tempStr, c);
            list.add(nc);
        }
        return list;
    }

    /**
     * Json通用解析类 获取对象列表数据 例如：
     * [{"a":"1","b":"2"},
     * {"a":"1","b":"2"},
     * {"a":"1","b":"2"},
     * {"a":"1","b":"2"},
     * {"a":"1","b":"2"}]
     *
     * @param objectArray
     * @param c           类
     * @return List对象
     */
    public static List parseJsonArray(JsonArray objectArray, Class<?> c) {
        List list = new ArrayList();
        Gson gson = new Gson();
        JsonObject object;
        String tempStr;
        for (JsonElement je : objectArray) {
            object = je.getAsJsonObject();
            tempStr = object.toString();
            Object nc = gson.fromJson(tempStr, c);
            list.add(nc);
        }
        return list;
    }

    /**
     * Json通用解析类 获取某一属性值 例如：{"a":1,"b":"2","c":3,"d":"4"}
     *
     * @param jsonObject
     * @param key
     * @return
     */
    public static String parseJsonObject(JsonObject jsonObject, String key) {
        String value = null;
        try {
            if (jsonObject != null && jsonObject.get(key) != null) {
                value = jsonObject.get(key).getAsString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 解析JSONArray 获取数组 例如：["1",2,true,"4"]
     *
     * @param jsonArray 数据源
     * @param array     返回值
     * @return
     */
    public static int[] parseJSONArray(JSONArray jsonArray, int[] array) {
        if (jsonArray != null) {
            try {
                array = new int[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    array[i] = jsonArray.optInt(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return array;
    }

    /**
     * 解析JSONArray 获取数组 例如：["1","2","3","4"]
     *
     * @param jsonArray 数据源
     * @param array     返回值
     * @return
     */
    public static String[] parseJSONArray(JSONArray jsonArray, String[] array) {
        if (jsonArray != null) {
            try {
                array = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    array[i] = jsonArray.optString(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return array;
    }

    /**
     * 解析JSONArray 获取数组 例如：["1",2,true,"4"]
     *
     * @param jsonArray 数据源
     * @param array     返回值
     * @return
     */
    public static Object[] parseJSONArray(JSONArray jsonArray, Object[] array) {
        if (jsonArray != null) {
            try {
                array = new Object[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    array[i] = jsonArray.opt(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return array;
    }

    /**
     * 从V7标准接口数据获取code
     * 示例： {"status":{"code":400,"text":"父部门不存在存在"},"custom":{}}
     *
     * @param jsonObject
     * @return
     */
    public static Object[] getStatus(JsonObject jsonObject) {
        Object[] code = new Object[2];
        if (jsonObject.has("status")) {
            JsonObject status = jsonObject.get("status").getAsJsonObject();
            if (status.has("code")) {
                code[0] = status.get("code").getAsInt();
            }
            if (status.has("text")) {
                code[1] = status.get("text").getAsString();
            } else {
                code[1] = "";
            }
        }
        return code;
    }

    /**
     * 从V7标准接口错误响应数据中获取错误信息
     *
     * @param jsonObject
     * @return
     */
    public static String getTextFromErrorBody(JsonObject jsonObject) {
        if (jsonObject.has("error")) {
            String error = jsonObject.get("error").getAsString();
            if (!TextUtils.isEmpty(error)) {
                return error;
            }
        }
        return "";
    }

    public static JSONObject json2JSON(JsonObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        String json = jsonObject.toString();
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JsonObject JSON2json(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        String json = jsonObject.toString();
        return new JsonParser().parse(json).getAsJsonObject();
    }
}
