package com.mengxinya.ys.checker;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.util.Arrays;

public class CheckerUtils {
    public static Object getJsonValue(JSONObject jsonObject, String field) {
        return getJsonValue(jsonObject, field.split("\\."));
    }

    public static Object getJsonValue(JSONArray jsonObject, String field) {
        return getJsonValue(jsonObject, field.split("\\."));
    }

    public static Object getJsonValue(JSONObject jsonObject, String[] fields) {
        if (fields == null || fields.length == 0) {
            return jsonObject;
        }
        String first = fields[0];
        Object obj = jsonObject.get(first);
        String[] leftFields = Arrays.copyOfRange(fields, 1, fields.length);

        if (leftFields.length == 0) {
            return obj;
        }

        if (obj instanceof JSONObject) {
            return getJsonValue((JSONObject)obj, leftFields);
        } else if (obj instanceof JSONArray) {
            return getJsonValue((JSONArray)obj, leftFields);
        } else {
            throw new JSONException("The field is not exists: " + Arrays.toString(fields));
        }
    }

    public static Object getJsonValue(JSONArray jsonArray, String[] fields) {
        if (fields == null || fields.length == 0) {
            return jsonArray;
        }
        String first = fields[0];
        Object obj = jsonArray.get(Integer.parseInt(first));
        String[] leftFields = Arrays.copyOfRange(fields, 1, fields.length);

        if (leftFields.length == 0) {
            return obj;
        }

        if (obj instanceof JSONObject) {
            return getJsonValue((JSONObject)obj, leftFields);
        } else if (obj instanceof JSONArray) {
            return getJsonValue((JSONArray)obj, leftFields);
        } else {
            throw new JSONException("The field is not exists: " + Arrays.toString(fields));
        }
    }
}
