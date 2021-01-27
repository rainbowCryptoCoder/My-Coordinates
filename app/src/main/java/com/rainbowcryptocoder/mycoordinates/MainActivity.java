package com.rainbowcryptocoder.mycoordinates;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //Declaring local variables
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private Button btn;

    /* This class provides access to the system location services.
    These services allow applications to obtain periodic updates of the device's geographical location,
    or to be notified when the device enters the proximity of a given geographical location. **/
    private LocationManager locationManager;

    /* Used for receiving notifications from the LocationManager when the location has changed.
    These methods are called if the LocationListener has been registered with the location manager
    service using the LocationManager#requestLocationUpdates(String, long, float, LocationListener) method. **/
    private LocationListener locationListener;

    //onCreate(Bundle) is where you initialize your activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //R is a Class in android that are having the id’s of all the view’s.
        //findViewById is a method that finds the view from the layout resource file that are attached with current Activity
        tv1 = findViewById(R.id.tv_latitude);
        tv2 = findViewById(R.id.tv_longitude);
        tv3 = findViewById(R.id.tv_altitude);
        btn = findViewById(R.id.btn_fetch);
    }

    public void fetchLocation(View view) {
        getGPSLocation();
    }

    private void getGPSLocation() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {

            /* Used for receiving notifications from the LocationManager when the location has changed.
            These methods are called if the LocationListener has been registered with the location manager
            service using the LocationManager#requestLocationUpdates(String, long, float, LocationListener) method. **/
            @Override
            public void onLocationChanged(@NonNull Location location) {
                tv1.setText(Double.toString(location.getLatitude()));
                tv2.setText(Double.toString(location.getLongitude()));
                tv3.setText(Double.toString(location.getAltitude()));
            }
            @Override
            public void onProviderEnabled(@NonNull String provider) {
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestLocationPermission();
            } else {
                btn.setEnabled(false);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 0, locationListener);
            }
        }
    }

    private void requestLocationPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.GPS_permissions).setCancelable(false).setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
            }).show();
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.GPS_permissions).setCancelable(false).setPositiveButton(R.string.btn_watch_permissions, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName())));
                }
            }).setNegativeButton(R.string.btn_close, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).show();
        }
    }
}