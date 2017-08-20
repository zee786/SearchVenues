package com.example.zee.searchvenues.model;

import com.example.zee.searchvenues.Presenter.Presenter;
import com.example.zee.searchvenues.model.MyRestService;
import com.example.zee.searchvenues.utility.VenueListJson;
import com.example.zee.searchvenues.utility.VenueModel;

import android.os.AsyncTask;
import android.os.AsyncTask.Status;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by zee on 8/17/2017.
 */

public class VenueSearch {
    private String result;
    private Presenter presenter;

    public void search(Presenter presenter, String query) {


        this.presenter = presenter;
        // create AysncTask for HTTP request
        MyAsync myAsync = new MyAsync();
        //if any backgroud task is already running then interrupt it and start a new background task for the new query.
        if (myAsync.getStatus() == Status.RUNNING) {
            myAsync.cancel(true);
        }
        // http request to background thread which will return response after completing the request.
        myAsync.execute(query);
    }

    private class MyAsync extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            //send http get request to foursquare and convert input stream from there as string.
            String respFromServer = MyRestService.getData(params[0]);

            System.out.println("Response from Server" + respFromServer);
            return respFromServer;
        }

        @Override
        protected void onPostExecute(String result) {
            ArrayList<VenueModel> venueListDTOs = null;
            // if failed to make connection with server, send error message to presenter
            if (result.equalsIgnoreCase("Error in network connection")) {
                showResult(result);
                System.out.println("The result is " + result);
            }
            //on getting response from server, pass it to presenter for further processing
            else {

                VenueListJson jp = new VenueListJson();
                     //parse json object into ArrayList and sent to presenter for further processing.

                try {
                        venueListDTOs = jp.parseResp(result);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("The result is " + result);

                showRes(venueListDTOs);

            }

        }
    }

    //on getting response from server, pass it to presenter for further processing
    private void showRes(ArrayList<VenueModel> venueListDTOs) {

        presenter.showResponse(venueListDTOs);
    }

    // if failed to make connection with server, send error message to presenter
    private void showResult(String result) {
        presenter.showResult(result);
    }

}
