package com.milky.service.serverapi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public interface OnTaskCompleteListner {
    void onTaskCompleted(String type, HashMap<String,String> listType);
}
