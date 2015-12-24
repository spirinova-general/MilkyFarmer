package com.milky.service.serverapi;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

public class HttpAsycTask extends AsyncTask<String, Void, JSONArray> {

    private String _url;

    private String _type;

    private List<NameValuePair> _nameValuePair = null;

    OnTaskCompleteListner _listner;
    HttpResponse response;
    private boolean methodPost = false;
    private  HashMap<String,String>requestedList;

    public void runRequest(String url, List<NameValuePair> nameValuePair, OnTaskCompleteListner listner, boolean isPost, HashMap<String,String> list) {
        _url = ServerApis.API_ROOT + url;
        _type = url;
        _nameValuePair = nameValuePair;
        _listner = listner;
        methodPost = isPost;
        requestedList =list;
        execute("");
    }

    @Override
    protected JSONArray doInBackground(String... params) {
        Log.i("HttpAsycTask_PreExec", "type: " + _type + " __ request: " + _url);
        try {
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 8000);
            HttpConnectionParams.setSoTimeout(httpParameters, 8000);

            HttpClient httpClient = new DefaultHttpClient(httpParameters);

//            String s = _url+URLEncoder.encode(appendedString, "UTF-8");
            if (methodPost) {
                HttpPost httpPost = new HttpPost(_url);
                httpPost.setEntity(new UrlEncodedFormEntity(_nameValuePair));
                response = httpClient.execute(httpPost);
            } else {

                HttpGet httpGet = new HttpGet(_url);
                response = httpClient.execute(httpGet);
            }


            if (response != null) {
                HttpEntity e = response.getEntity();
                String data = EntityUtils.toString(e);
                JSONArray last = new JSONArray(data);
                return last;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(JSONArray result) {
        JSONArray _result = result;


        if (_result == null || _result.length() == 0) {
            ServerApis.STATUS = 0;
        } else {
            ServerApis.STATUS = 1;
        }
        _listner.onTaskCompleted(_result, _type,requestedList);

    }

}