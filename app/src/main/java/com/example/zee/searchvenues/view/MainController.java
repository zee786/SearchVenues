package com.example.zee.searchvenues.view;

/**
 * Created by zee on 8/12/2017.
 */

import android.content.Context;

import com.example.zee.searchvenues.utility.VenueModel;

import java.util.ArrayList;
import java.util.List;


public interface MainController
{


    void stopLocationManager();

    void showSettingsAlert(String title,String message);

    Context getSearchAppContext();

    void clearListView();

    void showResult(String result);

    void populateListView(ArrayList<VenueModel> venueModels);

}
