package com.milky.service.serverapi;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;

public interface OnTaskCompleteListner {
    void onTaskCompleted(JSONArray result, String type, HashMap<String,String> listType);
}
