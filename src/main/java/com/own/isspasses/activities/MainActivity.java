package com.own.isspasses.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.own.isspasses.R;
import com.own.isspasses.utils.AlertDialogUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        Button btnGetPasses = (Button) findViewById(R.id.btnGetPasses);
        btnGetPasses.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){ // We may have more ui elements to handle on click events
            case R.id.btnGetPasses:
                if(isLocationEnabled())
                    startActivity(new Intent(MainActivity.this, PassListActivity.class));
                else
                    launchLocationSettingsDialog();
                break;
        }
    }

    public boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        }else{
            locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    private void launchLocationSettingsDialog() {
        AlertDialogUtils.displayAlertDialog(this, "Enable Location!", "Please enable location service to catch your location.", "Enable", "Cancel", null, new AlertDialogUtils.AlertDialogListener() {
            @Override
            public void onPositiveBtnClick() {
                Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(viewIntent);
            }
        });
    }
}
