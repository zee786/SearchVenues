package com.example.zee.searchvenues.utility;

/**
 * Created by zee on 8/12/2017.
 */
import org.json.JSONArray;
import org.json.JSONException;


public class VenueModel {
    private String name; // string to store venue name
    private String address;// string to store venue address
    private String distance;//string to store venue distance
    private String errorDescription;//string to store error description
    private String errorType;// string to store error type
    private int errorCode; //string to store error code
    private boolean requestFailed;//boolean to tell status of http call

    public VenueModel(String name,int distance, String address)
    {
        //if name is empty
        if(name.isEmpty())
        {
            name="no name received";
        }
        this.name=name;
        //if address is empty
        if(address==null)
        {
            address="no address received";

        }
        this.address=address;

        //convert ditance to kilometers if it is greater than 1000 meters
        if(distance < 1000)

            this.distance=distance + "Meters";

        else
            this.distance=distance * 0.001 + "Km";
    }

    private final static  String venueIconSize="64";


    public VenueModel(boolean failure,int errorCode,String errorType,String errorDescription)
    {
        this.requestFailed=failure;
        this.errorCode=errorCode;
        this.errorType=errorType;
        this.errorDescription=errorDescription;

    }




    public String getErrorDescription()
    {
        return errorDescription;
    }

    public String getErrorType()
    {
        return errorType;

    }
    public int getErrorCode()
    {
        return errorCode;

    }
    public boolean isRequestFailed()
    {

        return requestFailed;
    }
    public String getName()
    {
        return name;
    }
    public String getAddress()
    {
        return address;

    }
    public String getDistance()
    {

        return distance;
    }
}
