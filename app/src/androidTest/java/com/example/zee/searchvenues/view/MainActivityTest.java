package com.example.zee.searchvenues.view;

import android.support.test.espresso.action.ViewActions;

import android.support.test.rule.ActivityTestRule;
import android.test.ActivityInstrumentationTestCase;

import com.example.zee.searchvenues.R;
import com.example.zee.searchvenues.utility.VenueModel;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;

/**
 * Created by zee on 8/19/2017.
 */

public class MainActivityTest extends ActivityTestRule<MainActivity> {
    public MainActivity mActivity;
    private String searchQuery;

    public MainActivityTest()
    {

        super(MainActivity.class);
    }

    //Test if connection to foursquare fails, then application should give proper error message
    public void testConnection()
    {
        onView(withId(R.id.item_searchVenue)).perform(ViewActions.typeText(searchQuery));
        onView(withId(R.id.item_tvErrorReport)).check(matches(isDisplayed()));

        onView(withId(R.id.item_tvErrorReport)).check(matches(withText(containsString("Connection with server failed"))));

    }

    //Test if network is disable then application should not get crashed and show proper error message
    public void testNetwork ()
    {
        onView(withId(R.id.item_searchVenue)).perform(ViewActions.typeText("Oulu"));
        onView(withId(R.id.item_tvErrorReport)).check(matches(isDisplayed()));

        onView(withId(R.id.item_tvErrorReport)).check(matches(withText(containsString("Error in Network"))));


    }

    //To verify that if user enter a search query in searchview
    public void testNoRelevantVenueFound()
    {
        onView(withId(R.id.item_searchVenue)).perform(ViewActions.typeText("abcdefgh"));
        onView(withId(R.id.item_tvErrorReport)).check(matches(isDisplayed()));

        onView(withId(R.id.item_tvErrorReport)).check(matches(withText(containsString("No relevant venue found"))));


    }

    // the internet should be connected, client_id and client_secret is placed correctly
    public void testViewResultVenueName()
    {
        onView(withId(R.id.item_searchVenue)).perform(ViewActions.typeText("abcdefgh"));

        onData(allOf(instanceOf(VenueModel.class))).inAdapterView(withId(R.id.item_listView)).atPosition(0)
                .onChildView(withId(R.id.item_TvName)).check(matches(withText(containsString(searchQuery))));
    }
}
