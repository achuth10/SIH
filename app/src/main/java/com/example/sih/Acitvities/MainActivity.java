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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.sih.Models.Constants;
import com.example.sih.Models.MySingleton;
import com.example.sih.R;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements LocationListener {
    protected LocationManager locationManager;
    TextView txtLat;
    private Button gotomap;
    //private String url = "https://sh935gbgj4.execute-api.us-east-1.amazonaws.com/prod/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtLat = findViewById(R.id.textview);
        gotomap=findViewById(R.id.GoToMap);
        gotomap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MapsActivity.class));
            }
        });
        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                5000, 0, this);


        //AWS
//        // Create an instance of CognitoCachingCredentialsProvider
//        CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
//                this.getApplicationContext(), "identity-pool-id", Regions.US_WEST_2);
//
//// Create LambdaInvokerFactory, to be used to instantiate the Lambda proxy.
//        LambdaInvokerFactory factory = new LambdaInvokerFactory(this.getApplicationContext(),
//                Regions.US_WEST_2, cognitoProvider);
//
//// Create the Lambda proxy object with a default Json data binder.
//// You can provide your own data binder by implementing
//// LambdaDataBinder.
//        final MyInterface myInterface = factory.build(MyInterface.class);
//
//        RequestClass request = new RequestClass("John", "Doe");
//// The Lambda function invocation results in a network call.
//// Make sure it is not called from the main thread.
//        new AsyncTask<RequestClass, Void, ResponseClass>() {
//            @Override
//            protected ResponseClass doInBackground(RequestClass... params) {
//                // invoke "echo" method. In case it fails, it will throw a
//                // LambdaFunctionException.
//                try {
//                    return myInterface.AndroidBackendLambdaFunction(params[0]);
//                } catch (LambdaFunctionException lfe) {
//                    Log.e("Tag", "Failed to invoke echo", lfe);
//                    return null;
//                }
//            }
//
//            @Override
//            protected void onPostExecute(ResponseClass result) {
//                if (result == null) {
//                    return;
//                }
//
//                // Do a toast
//                Toast.makeText(MainActivity.this, result.getGreetings(), Toast.LENGTH_LONG).show();
//            }
//        }.execute(request);


        //

    }


    public void sendData(final Location loc)
    {


        StringRequest request = new StringRequest(Request.Method.POST, Constants.DB_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Response is " + response.toString());
                Toast.makeText(getApplicationContext(),response.toString(), Toast.LENGTH_LONG).show();

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
                location.getLatitude() +
                ", Longitude:" +
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



