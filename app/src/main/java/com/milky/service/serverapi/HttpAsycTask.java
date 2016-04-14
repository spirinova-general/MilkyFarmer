package com.milky.service.serverapi;

import android.os.AsyncTask;

import com.milky.utils.Constants;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

public class HttpAsycTask extends AsyncTask<String, Void, Void> {

    private String _url;
    private String _type;
    private JSONObject object = null;

    private OnTaskCompleteListner _listner;
    private HttpResponse response;
    private boolean methodPost = false;
    private HashMap<String, String> requestedList;

    public void runRequest(String url, JSONObject jsonObject, OnTaskCompleteListner listner, boolean isPost, HashMap<String, String> list) {
        _url = url;
        _type = url;
        object = jsonObject;
        _listner = listner;
        methodPost = isPost;
        requestedList = list;
        execute("");
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();

//            String s = _url+URLEncoder.encode(appendedString, "UTF-8");
            if (!methodPost) {

                HttpGet httpGet = new HttpGet(_url);
                response = (HttpResponse) httpclient.execute(httpGet);
            } else {
                HttpPost httpPostRequest = new HttpPost(_url);
//                httpPost.setEntity(new UrlEncodedFormEntity(_nameValuePair));
                StringEntity se = new StringEntity(object.toString());
                httpPostRequest.setEntity(se);
                httpPostRequest.setHeader("Accept", "application/json");
                httpPostRequest.setHeader("Content-type", "application/json");
                response = (HttpResponse) httpclient.execute(httpPostRequest);

            }
            HttpParams httpParameters = new BasicHttpParams();
            // Set the timeout in milliseconds until a connection is established.
            // The default value is zero, that means the timeout is not used.
            int timeoutConnection = 4000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            // Set the default socket timeout (SO_TIMEOUT)
            // in milliseconds which is the timeout for waiting for data.
            int timeoutSocket = 6000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                // Read the content stream
                InputStream instream = entity.getContent();
                Header contentEncoding = response.getFirstHeader("Content-Encoding");
                if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                    instream = new GZIPInputStream(instream);
                }

                // convert content stream to a String
                String resultString = convertStreamToString(instream);
                instream.close();
//                resultString = resultString.substring(1, resultString.length() - 1); // remove wrapping "[" and "]"
                if (resultString != null || resultString.equals("null")) {
                    // Transform the String into a JSONObject
                    if(resultString.contains("{"))
                     Constants.API_RESPONCE = new JSONObject(resultString);
//                            holder.getFarmerCode(jsonObjRecv.optJSONObject())
                }
            }

        }
        catch (ConnectTimeoutException toexp)
        {
            Constants.TIME_OUT = true;
        }
        catch(Exception e) {
            // More about HTTP exception handling in another tutorial.
            // For now we just print the stack trace.
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(Void s) {
        if(_listner!=null)
        _listner.onTaskCompleted(_type, requestedList);

    }

    private static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
		 * method. We iterate until the BufferedReader return null which means
		 * there's no more data to read. Each line will appended to a StringBuilder
		 * and returned as String.
		 *
		 * (c) public domain: http://senior.ceng.metu.edu.tr/2009/praeda/2009/01/11/a-simple-restful-client-at-android/
		 */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}