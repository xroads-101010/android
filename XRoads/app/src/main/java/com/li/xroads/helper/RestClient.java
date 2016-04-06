package com.li.xroads.helper;

import android.util.Log;

import com.li.xroads.util.Constant;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;


/**
 * Created by sony on 28-02-2016.
 */
public class RestClient {
    private static final char PARAMETER_DELIMITER = '&';
    private static final char PARAMETER_EQUALS_CHAR = '=';
    private static final String TAG = "RestClient";
    private static final String SERVER_ADDRS = "https://d4d55a60.ngrok.io/xroads-app/";

    public HTTPResponse doPost(String requestURL,
                         JSONObject postData) {
        URL url;
        HTTPResponse response = new HTTPResponse();
        HttpURLConnection conn = null;
        try {

            url = new URL(SERVER_ADDRS+requestURL);
            Log.i(TAG, url.getPath()+":: "+url.getHost());
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type",
                    "application/json");
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(postData.toString());

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                response.setStatus(responseCode);
                response.setResponse(readStream(conn.getInputStream()));
            } else {
                Log.e(TAG, "response::"+response+":::::"+readStream(conn.getInputStream()));
                response.setStatus(responseCode);
                response.setResponse("Problem while registering user");
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            response.setResponse("Problem while registering user");
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
        }

        return response;
    }


    public HTTPResponse doGet(String requestURL, HashMap<String, String> queryParam) {

        HTTPResponse response = new HTTPResponse();
        HttpURLConnection conn = null;
        try {
            URL url;
            String params = convertDataToQueryParam(queryParam);
            url = new URL(SERVER_ADDRS+requestURL+"?"+params);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            /*conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod(HttpURLConnection.CON);

            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type",
                    "application/json");*/
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                response.setStatus(responseCode);
                response.setResponse(readStream(conn.getInputStream()));
            } else {
            response.setStatus(responseCode);
            response.setResponse("Problem while fetching user");
        }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (conn != null)
                conn.disconnect();
        }
        return response;
    }

    private String convertDataToQueryParam(HashMap<String, String> parameters) {
        StringBuilder parametersAsQueryString = new StringBuilder();
        if (parameters != null) {
            boolean firstParameter = true;

            for (String parameterName : parameters.keySet()) {
                if (!firstParameter) {
                    parametersAsQueryString.append(PARAMETER_DELIMITER);
                }
                try {
                    parametersAsQueryString.append(parameterName)
                            .append(PARAMETER_EQUALS_CHAR)
                            .append(URLEncoder.encode(
                                    parameters.get(parameterName), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                firstParameter = false;
            }
        }
        return parametersAsQueryString.toString();

    }


    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }


}