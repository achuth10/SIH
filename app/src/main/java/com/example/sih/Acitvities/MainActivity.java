package com.example.sih.Acitvities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sih.Models.Constants;
import com.example.sih.Models.MySingleton;
import com.example.sih.R;

import java.util.HashMap;
import java.util.Map;

import static com.example.sih.Models.Constants.DB_URL;
import static com.example.sih.Models.Constants.aws;
import static com.example.sih.Models.Constants.compute;

public class MainActivity extends AppCompatActivity implements LocationListener {
    protected LocationManager locationManager;
    TextView txtLat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtLat = findViewById(R.id.textview);
        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                5000, 0, this);


    }


    public void sendData(final Location loc)
    {

        //String url = DB_URL+"?x="+loc.getLatitude()+"&y="+loc.getLongitude();
       // System.out.println(url);
        final StringRequest request = new StringRequest(Request.Method.POST, aws, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //System.out.println("Request is "+request.toString());
                System.out.println("Response is " + response.toString());
                Toast.makeText(getApplicationContext(),response.toString(), Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                System.out.println("Error is " + error.toString());
                //progressBar.setVisibility(View.INVISIBLE);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> params  = new HashMap<String,String>();
                params.put(Constants.X,String.valueOf(loc.getLatitude()));
                params.put(Constants.y,String.valueOf(loc.getLongitude()));
                //params.put("Content-Type", "text/plain");
                //params.put(Constants.ID, String.valueOf(1));
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

            System.out.println("Data sent");
    }


















    @Override
    public void onLocationChanged(Location location) {
        txtLat = findViewById(R.id.textview);

        txtLat.setText("Latitude:" +
                location.getLatitude() + "\n" +
                "Longitude:" +
                location.getLongitude());
       sendData(location);
        //Toast.makeText(getApplicationContext(),"Data sent",Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }
}



