package com.example.chungmin.helpu.serverrequest.common;


import com.example.chungmin.helpu.enumeration.ProjectStatus;
import com.example.chungmin.helpu.models.CustomerRequest;
import com.example.chungmin.helpu.models.ServiceProvider;
import com.example.chungmin.helpu.models.User;

import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Chung Min on 9/23/2015.
 */
public class ServerUtils {
    public static final int CONNECTION_TIMEOUT = 1000 * 15;

    public static HttpParams getHttpRequestParams() {
        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams,
                CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpRequestParams,
                CONNECTION_TIMEOUT);
        return httpRequestParams;
    }


    public static Object buildRecord(Class<?> type, String result) throws JSONException {

        JSONObject jObject = new JSONObject(result);
        if (jObject.length() != 0) {
            switch (type.getSimpleName()) {
                case "Boolean":
                    Boolean isTrueOrFalse = jObject.getBoolean("result");
                    return (Object) isTrueOrFalse;
                default:
                    break;
            }

        }

        return null;
    }


}
