package com.example.zee.searchvenues.utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zee on 8/17/2017.
 * JSON parser to parse message received from server
 */

public class VenueListJson {

    //this method will receive json object and return a transfer object(VenueModel)
    public ArrayList<VenueModel> parseResp(String content) throws JSONException
    {
        int code;
        String name;
        String address;
        int distance;

        List venueList=new ArrayList<VenueModel>();
        //response from json object
        JSONObject jsonObject=new JSONObject(content);


        code=(((JSONObject)jsonObject.get("meta")).getInt("code"));

        if(code==200)
        {

            JSONArray items = ((JSONObject) jsonObject.get("response")).getJSONArray("venues");
            //check if response from server contains name of the venue
            for (int i = 0; i < items.length(); i++) {
                if(items.getJSONObject(i).has("name"))
                {
                    name=items.getJSONObject(i).getString("name");
                    System.out.println("The name is" + name);

                }
                else
                {
                    name="name not received";

                }
                //check if response from server contains address of the venue

                if(items.getJSONObject(i).getJSONObject("location").has("formattedAddress"))
                {
                    address=items.getJSONObject(i).getJSONObject("location").getString("formattedAddress");

                }
                else
                {
                    address="address not received";

                }

                //check if response from server contains distance of the venue

                if(items.getJSONObject(i).getJSONObject("location").has("distance"))
                {
                    distance=(int)(items.getJSONObject(i).getJSONObject("location").getInt("distance"));

                }
                else
                {
                    distance=0;

                }

                venueList.add(new VenueModel(name,distance,address));


            }

    }
        else
        {
            String errorType = (((JSONObject) jsonObject.get("meta")).getString("errorType")); // error type if response from server is not a success
            String errorDescription = (((JSONObject) jsonObject.get("meta")).getString("errorDetail"));// error details if response from server is negative
            venueList.add(new VenueModel(true, code, errorType, errorDescription));
        }

        return (ArrayList<VenueModel>) venueList;
    }
}
