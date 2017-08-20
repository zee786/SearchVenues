package com.example.zee.searchvenues.view;

import com.example.zee.searchvenues.model.LocationTracker;
import com.example.zee.searchvenues.model.MyRestService;
import com.example.zee.searchvenues.model.VenueSearch;
import com.example.zee.searchvenues.utility.VenueListJson;
import com.example.zee.searchvenues.utility.VenueModel;

import org.junit.Before;


import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;

/**
 * Created by zee on 8/19/2017.
 */

public class BackendTest
{

    private String gpsSettingTitle="GPS Settings";
    private String gpsSettingMessage="GPS is not enabled. Please enable it using settings menu";
    private final String apiUrl="";
    private final String clientID="YFRT0F0EKMLCUYYQO04YWQMUN15MXZH2KEXU5ZGN2D1QV0CF"; //Client id
    private final String clientSecret="I2NQKHQPPL3DCCBTWU4PP4ZF2WJMGYZEOXIVXSHF0K330RJA"; // Client secret
    private final String baseQuery = "https://api.foursquare.com/v2/venues/search?"; //base query string from foursquare
    private final int apiVersion=20130815; // apiversion
    private double latitude=0.0; //latitude
    private double longitude=0.0; //longitude
    private MyRestService restService;
    private String searchQuery;

    @Before
    public void setUp() throws Exception
    {
        restService=new MyRestService();

    }

    // the response from server should be parsed and store into the venueModel List
    @Test
    public void testReceievedJSONObjectParser() throws Exception
    {
        String searchQuery="oulu";
        double testLatitude=65.0581571;
        double testLongitude=25.4744109;
        String prepQuery = baseQuery + "client_id=" + clientID
                + "&client_secret=" + clientSecret + "&v=" + apiVersion
                + "&ll=" + testLatitude + "," + testLongitude + "&query=" + searchQuery;

        String respFromServer=MyRestService.getData(prepQuery);
        VenueListJson js=new VenueListJson();
        // parse json object into Arraylist
        ArrayList<VenueModel> venueModels=js.parseResp(respFromServer);

        //verify name
        assertEquals(venueModels.get(0).getName(), "Oulun yliopisto / University of Oulu");

        //verify address
        assertEquals(venueModels.get(0).getAddress(), "[\"Pentti Kaiteran Katu 1\",\"90570 Oulu\",\"Suomi\"]");

        //verify distance
        assertEquals(venueModels.get(0).getDistance(), "399Meters");

        assertEquals(venueModels.size(), 30);
    }

    //Error response from server in case GPS service failed to set latitude and longitude
    @Test
    public void testLocationParametermissing () throws Exception
    {
        String searchQuery="oulu";
        String prepQuery = baseQuery + "client_id=" + clientID
                + "&client_secret=" + clientSecret + "&v=" + apiVersion
                + "&ll=" + this.latitude + "," + this.longitude + "&query=" + searchQuery;

        String respFromServer=MyRestService.getData(prepQuery);
        VenueListJson js=new VenueListJson();

        // parse json object into Arraylist
        ArrayList<VenueModel> venueModels=js.parseResp(respFromServer);

        //verify error code
        assertEquals(venueModels.get(0).getErrorCode(), 400);

        //verify error type
        assertEquals(venueModels.get(0).getErrorType(), "param_error");

        // verify error description
        assertEquals(venueModels.get(0).getErrorDescription(), "Must provide parameter ll");



    }

    //Handling error if client ID is wrong or missing
    @Test
    public void testMissingClientIdFourSquare() throws Exception
    {

        String searchQuery="oulu";
        String prepQuery = baseQuery + "client_id="
                + "&client_secret=" + clientSecret + "&v=" + apiVersion
                + "&ll=" + this.latitude + "," + this.longitude + "&query=" + searchQuery;

        String respFromServer=MyRestService.getData(prepQuery);
        VenueListJson js=new VenueListJson();
        // parse json object into Arraylist
        ArrayList<VenueModel> venueModels=js.parseResp(respFromServer);

        // verify error code
        assertEquals(venueModels.get(0).getErrorCode(), 400);
        //verify error type
        assertEquals(venueModels.get(0).getErrorType(), "invalid_auth");
    }

    //Handling error if client ID is wrong or missing
    @Test
    public void testMissingSecretIdFourSquare() throws Exception
    {

        String searchQuery="oulu";
        String prepQuery = baseQuery + "client_id=" + clientID
                + "&client_secret="  + "&v=" + apiVersion
                + "&ll=" + this.latitude + "," + this.longitude + "&query=" + searchQuery;

        String respFromServer=MyRestService.getData(prepQuery);
        VenueListJson js=new VenueListJson();
        // parse json object into Arraylist
        ArrayList<VenueModel> venueModels=js.parseResp(respFromServer);
        //verify error code
        assertEquals(venueModels.get(0).getErrorCode(), 400);
        //verify error type
        assertEquals(venueModels.get(0).getErrorType(), "invalid_auth");
    }

    //Handling error if Four square version is missing or wrong
    @Test
    public void testMissingVersionFourSquare() throws Exception
    {

        String searchQuery="oulu";
        String prepQuery = baseQuery + "client_id=" + clientID
                + "&client_secret=" + clientSecret + "&v="
                + "&ll=" + this.latitude + "," + this.longitude + "&query=" + searchQuery;

        String respFromServer=MyRestService.getData(prepQuery);
        VenueListJson js=new VenueListJson();
        // parse json object into Arraylist
        ArrayList<VenueModel> venueModels=js.parseResp(respFromServer);


        assertEquals(venueModels.get(0).getErrorCode(), 400);
        assertEquals(venueModels.get(0).getErrorType(), "param_error");
    }

    //if JSONParser successfully parse the output from server
    @Test
    public void testJsonParserSuccessfulContent() throws Exception
    {
        String searchQuery="oulu";
        String prepQuery = baseQuery + "client_id=" + clientID
                + "&client_secret=" + clientSecret + "&v=" + apiVersion
                + "&ll=" + this.latitude + "," + this.longitude + "&query=" + searchQuery;

        String respFromServer=MyRestService.getData(prepQuery);
        VenueListJson js=new VenueListJson();
        // parse json object into Arraylist
        ArrayList<VenueModel> venueModels=js.parseResp(respFromServer);
        assertTrue(!venueModels.isEmpty());

    }

    //if service is not able to make connection with foursquare server
    @Test
    public void testHttpServerRequestIsReturn()throws Exception
    {
        String searchQuery="oulu";
        String prepQuery = baseQuery + "client_id=" + clientID
                + "&client_secret=" + clientSecret + "&v=" + apiVersion
                + "&ll=" + this.latitude + "," + this.longitude + "&query=" + searchQuery;

        String respFromServer=MyRestService.getData(prepQuery);
        VenueListJson js=new VenueListJson();

        assertTrue(!respFromServer.isEmpty());


    }
}
