package com.santeh.rjhonsl.samplemap.Parsers;

import android.util.Log;

import com.santeh.rjhonsl.samplemap.Obj.CustInfoObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rjhonsl on 8/17/2015.
 */
public class PondInfoJsonParser {

    public static List<CustInfoObject> parseFeed(String content) {

        try {
            JSONArray ar = new JSONArray(content);
            List<CustInfoObject> custInfoObjectList = new ArrayList<>();

            for (int i = 0; i < ar.length(); i++) {

                JSONObject obj = ar.getJSONObject(i);
                CustInfoObject custInfoObject = new CustInfoObject();


                custInfoObject.setId(obj.getInt("id"));
                custInfoObject.setSizeofStock(obj.getInt("sizeofStock"));
                custInfoObject.setPondID(obj.getInt("pondid"));
                custInfoObject.setQuantity(obj.getInt("quantity"));
                custInfoObject.setArea(obj.getInt("area"));
                custInfoObject.setSpecie(obj.getString("specie"));
                custInfoObject.setDateStocked(obj.getString("dateStocked"));
                custInfoObject.setCulturesystem(obj.getString("culturesystem"));
                custInfoObject.setRemarks(obj.getString("remarks"));
                custInfoObject.setCustomerID(obj.getString("customerId"));

                Log.d("JSON PARSE", "ID: " + custInfoObject.getId()+" " + custInfoObject.getCustomerID() + " " + ar.length());
                custInfoObjectList.add(custInfoObject);

            }

            return custInfoObjectList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
