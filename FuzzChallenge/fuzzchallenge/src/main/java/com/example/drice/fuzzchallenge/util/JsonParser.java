package com.example.drice.fuzzchallenge.util;

import android.util.Log;

import com.example.drice.fuzzchallenge.model.ListviewContent;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Json Parser for asynctask in BaseFragment. Not needed because of volley, just including this in
 * here to show how to do it without volley.
 * Created by DrIce on 4/29/15.
 */
@Deprecated
public class JsonParser {


    public String getFromHttp(String url) {
        String responseString = "";
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpResponse response = null; // some response object
            HttpGet get = new HttpGet(url);
            HttpEntity httpEntity;

            response = client.execute(get);

            httpEntity = response.getEntity();
            responseString = EntityUtils.toString(httpEntity);
        }
        catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("jsonparser", responseString);
        return responseString;
    }

    public ArrayList<ListviewContent> retrieveContent(String url) {
        ArrayList<ListviewContent> items = new ArrayList<>();
        parseJson(getFromHttp(url), items);
        Log.i("parseJSON", "items length is " + items.size());
        return items;
    }

    public void parseJson(String jsonStr, ArrayList<ListviewContent> items) {
        if(jsonStr != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonStr);
                Gson gson = new Gson();
                for(int ind = 0; ind < jsonArray.length(); ind++) {
                    ListviewContent item;
                    item = gson.fromJson(jsonArray.get(ind).toString(), ListviewContent.class);
                    items.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Called by GetUserPhotosTask to fetch from api, returns an arrayList of Photo objects
     * @return
     */

    public ArrayList<ListviewContent> fetchItems() {

        String url = Constants.ENDPOINT_URL;
        Log.i("TAG", "URL is " + url);
        return retrieveContent(url);

    }
}
