package com.example.hikersapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    LocationManager lm;
    LocationListener ll;
    TextView latt;
    TextView longi;
    TextView acc;
    TextView alti;
    TextView addr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lm=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

           latt=(TextView)findViewById(R.id.lattitude);
           longi=(TextView)findViewById(R.id.longitude);
           acc=(TextView)findViewById(R.id.accuracy);
           alti =(TextView)findViewById(R.id.altitude);
           addr=(TextView)findViewById(R.id.address);

        ll=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("Location is ",location.toString());
                updateLocation(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else
        {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,ll);
            Location lastlocation=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastlocation!=null)
            {
                updateLocation(lastlocation);
            }
        }
    }
    public void updateLocation(Location lc)
    {
        latt.setText("Lattitude: "+lc.getLatitude());
        longi.setText("Longitude: "+lc.getLongitude());
        acc.setText("Accuracy: "+lc.getAccuracy());
        alti.setText("Altitude: "+lc.getAltitude());
        String address="Could not find the address";
        Geocoder geocoder=new Geocoder(this, Locale.getDefault());
        try {
            List<Address> listAddress=geocoder.getFromLocation(lc.getLatitude(),lc.getLongitude(),1);
            if(listAddress!=null&&listAddress.size()>0)
            {
                address="Address:\n";
                if(listAddress.get(0).getThoroughfare()!=null)
                {
                    address+=listAddress.get(0).getThoroughfare()+"\n";
                }
                if(listAddress.get(0).getLocality()!=null)
                {
                    address+=listAddress.get(0).getLocality()+"\n";
                }
                if(listAddress.get(0).getPostalCode()!=null)
                {
                    address+=listAddress.get(0).getPostalCode()+"\n";
                }
                if(listAddress.get(0).getAdminArea()!=null)
                {
                    address+=listAddress.get(0).getAdminArea()+"\n";
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        addr.setText(address);
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
            {
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,ll);
            }

        }
    }
}
