package com.example.manuel.wingittest2;


//Main view for the app, lots left to do
//location grabbing is broken at the moment, defaulting to hunter as location
//Handling repeated fragments is not working, only food tab updates
//need to connect to azure to get preferences CSV

//Location service implementation adjusted from https://github.com/googlesamples/android-play-location/blob/master/BasicLocationSample/app/src/main/java/com/google/android/gms/location/sample/basiclocationsample/MainActivity.java


import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    protected static final String TAG = "MainActivity";
    //entry point into Google Play Services
    protected GoogleApiClient mGoogleApiClient;
    //geolocation holders
    protected Location mLastLocation;
    protected String mLatitudeLabel;
    protected String mLongitudeLabel;
    protected TextView mLatitudeText;
    protected TextView mLongitudeText;


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
       // mLatitudeLabel = getResources().getString(R.string.latitude_label);
       // mLongitudeLabel = getResources().getString(R.string.longitude_label);
        mLatitudeText = (TextView) findViewById((R.id.location1));
        mLongitudeText = (TextView) findViewById((R.id.location2));

        buildGoogleApiClient();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Drinks";
                case 1:
                    return "Food";
                case 2:
                    return "Cafes";
            }
            return null;
        }
    }


    /**Lets make our wingit catergories here
     * first food
     * bar
     * cafe
     *
     * maybe movies?
     * maybe events?


     */

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private GoogleMap gmap;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            int section = getArguments().getInt(ARG_SECTION_NUMBER);
            if (section == 1){
                textView.setText("There's always time for drinks");
            }
            else if (section == 2) {
                textView.setText("Let's get some food");
            }
            else {
                textView.setText("How about a cup o' joe?");
            }


            return rootView;
        }
    }



    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    //wingit button is clicked
    public void WingitClicked (View view){
        InternetTester it = new InternetTester(getApplicationContext());
        Boolean isConnected = it.isConnectingToInternet();
        // check for Internet status
        if (isConnected) {
            new PlacesQuery("American").execute();
            // Internet Connection is Present
            // make HTTP requests

        }
        else{
            Snackbar.make(view, "Please check your network connection", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        }
    }

    //card result clicked, open google map
    public void CardClicked(View view){
        TextView cardaddress = (TextView)findViewById(R.id.info_address);
        String placeaddress = cardaddress.getText().toString();
        if (placeaddress != null) {
            String uri = "http://maps.google.com/maps?daddr=" + placeaddress;
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            startActivity(intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitudeText.setText(String.format("%s: %f", mLatitudeLabel,
                    mLastLocation.getLatitude()));
            mLongitudeText.setText(String.format("%s: %f", mLongitudeLabel,
                    mLastLocation.getLongitude()));
        } else {
            Toast.makeText(this, R.string.no_location_detected, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");


    }






        //Async downloader adapted from https://developer.android.com/training/basics/network-ops/connecting.html


        // Uses AsyncTask to create a task away from the main UI thread. This task takes a
        // URL string and uses it to create an HttpUrlConnection. Once the connection
        // has been established, the AsyncTask downloads the contents of the webpage as
        // an InputStream. Finally, the InputStream is converted into a string, which is
        // displayed in the UI as a card by the AsyncTask's onPostExecute method.
        private class PlacesQuery extends AsyncTask<String, String, String> {
            //sample query
            //https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyBy8Rd_xJ8jOnLeBsvIt8sw4fybCiM5YEI&rankby=distance&keyword=American&location=40.7686793,-73.9647192&opennow&type=restaurant

            //this is a silly thing to do but...
            final private String APIKEY = "AIzaSyBy8Rd_xJ8jOnLeBsvIt8sw4fybCiM5YEI";
            String URLstring = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
            URL placeurl;
            //preset location to hunter for now
            final private String latlng = "40.7686793,-73.9647192";
            public PlacesQuery(String foodtype){
                URLstring = URLstring + "key=" + APIKEY + "&rankby=distance" + "&keyword=" + foodtype + "&location=" + latlng + "&opennow&type=restaurant";
                try {
                    placeurl = new URL(URLstring);
                }
                catch (MalformedURLException e){
                    e.printStackTrace();
                }
            }


            @Override
            protected String doInBackground(String... urls) {
                try{
                    //try connecting to the url
                    HttpURLConnection connection = (HttpURLConnection)placeurl.openConnection();
                    int responseCode = connection.getResponseCode();
                    System.out.println(responseCode);
                    //place results in a buffered reader
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    StringBuilder responseOutput = new StringBuilder();
                    //go from buffered reader to string
                    while((line = br.readLine()) != null ) {
                        responseOutput.append(line);
                    }
                    //finished with the buffer
                    br.close();
                    System.out.println(responseOutput);

                    return responseOutput.toString();
                }
                catch(MalformedURLException e){
                    e.printStackTrace();
                }
                catch(IOException e){
                    e.printStackTrace();
                }
                return "failed";
            }
            // onPostExecute displays the results of the AsyncTask.
            @Override
            protected void onPostExecute(String result) {
                JSONObject object;
                JSONArray array = null;
                try{
                    object = new JSONObject(result);
                    array = object.getJSONArray("results");

                }
                catch (Throwable t){
                    Log.e("My App", "Could not parse malformed JSON: \"" + result + "\"");
                }
                TextView testy = (TextView)findViewById(R.id.info_name);
                TextView testy2 = (TextView)findViewById(R.id.info_address);
                try {
                    testy.setText(array.getJSONObject(0).getString("name"));
                    testy2.setText(array.getJSONObject(0).getString("vicinity"));
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
}
