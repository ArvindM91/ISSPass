package com.own.isspasses.activities;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.own.isspasses.R;
import com.own.isspasses.adapters.PassesRecyclerAdapter;
import com.own.isspasses.apis.ApiGetHandler;
import com.own.isspasses.interfaces.ApiResponseListener;
import com.own.isspasses.models.GetPassesResponseModel;
import com.own.isspasses.models.PassDetailsModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.own.isspasses.R.id.recycleView;

public class PassListActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    PassesRecyclerAdapter passesRecyclerAdapter;

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_list);
        initViews();
        if (Build.VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
        } else {
            checkAndPromptLocationPermission();
        }
    }

    private void initViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(recycleView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        passesRecyclerAdapter = new PassesRecyclerAdapter(this, null);
        recyclerView.setAdapter(passesRecyclerAdapter);
        Toast.makeText(this, "Loading.. please wait..", Toast.LENGTH_SHORT).show();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    public void onPause() {
        super.onPause();
        //unregister the location update listener..
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //connect to the API
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        //Toast.makeText(this, "Latitude : " + latitude + "\n Longitude :" + longitude, Toast.LENGTH_LONG).show();
        makeServiceCallToIssServer(latitude, longitude);
    }

    private void makeServiceCallToIssServer(double latitude, double longitude) {
        String api = "http://api.open-notify.org/iss-pass.json";
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("lat", String.valueOf(latitude));
        queryParams.put("lon", String.valueOf(longitude));
        ApiGetHandler.makeGetApiRequest(this, api, "GET_PASSES", queryParams, null, new ApiResponseListener() {
            @Override
            public void onSuccessResponse(String apiTag, String resJsonStr) {
               processApiResponse(resJsonStr);
            }

            @Override
            public void onErrorResponse(String apiTag, VolleyError error) {
                Log.e(PassListActivity.class.getSimpleName(), "Something went wrong with API call");
                Toast.makeText(PassListActivity.this, "Something went wrong with API call :" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void processApiResponse(String resJsonStr) {
        Gson gson = new Gson();
        GetPassesResponseModel getPassesResponseModel = gson.fromJson(resJsonStr, GetPassesResponseModel.class);
        ArrayList<PassDetailsModel> passDetailsList = getPassesResponseModel.getPassListResponse();
        passesRecyclerAdapter.setPassDetailsList(passDetailsList);
    }

    private void checkAndPromptLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == PackageManager.PERMISSION_GRANTED) {
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, MY_PERMISSIONS_REQUEST_LOCATION);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create()
                .setInterval(15 * 1000) // 15 seconds
                .setFastestInterval(10 * 1000) // 10 seconds
                .setSmallestDisplacement(50.0f)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0) {
            LocationServices.FusedLocationApi.requestLocationUpdates(this.mGoogleApiClient, this.mLocationRequest, (LocationListener) this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    Toast.makeText(this, "Permission Denied. Please enable the permission in app settings to access the current location details.", Toast.LENGTH_LONG).show();
                    return;
                } else if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == PackageManager.PERMISSION_GRANTED) {
                    if (mGoogleApiClient == null) {
                        buildGoogleApiClient();
                    }
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }
}
