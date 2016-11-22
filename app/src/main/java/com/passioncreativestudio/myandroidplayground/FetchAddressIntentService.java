package com.passioncreativestudio.myandroidplayground;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FetchAddressIntentService extends IntentService {
    private static final String TAG = "FetchAddressIntentService";

    protected ResultReceiver mReceiver;

    public FetchAddressIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        String errorMessage = "";

        Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);

        mReceiver = intent.getParcelableExtra(Constants.RECEIVER);

        // Check if receiver was properly registered.
        if (mReceiver == null) {
            Log.wtf(TAG, "No receiver received. There is nowhere to send the results.");
            return;
        }

        List<Address> addressList = null;

        try {
            addressList = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1);
        } catch (IOException ioException) {
            errorMessage = "Invalid latitude and longitude used.";
        } catch (IllegalArgumentException illegualArgumentException) {
            errorMessage = "Invalid latitude and longitude used.";
        }

        if(addressList == null || addressList.size() == 0) {
            if(errorMessage.isEmpty()) {
                errorMessage = "No address found.";
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
        } else {

            /*

            Address[
            addressLines=[
            0:"373 Clementi Avenue 4",1:"Singapore 120373"
            ],feature=373,admin=null,sub-admin=null,locality=null,thoroughfare=Clementi Avenue 4,postalCode=120373,countryCode=SG,countryName=Singapore,hasLatitude=true,latitude=1.3194504,hasLongitude=true,longitude=103.7673516,phone=null,url=null,extras=null
            ]

            */

            Address address = addressList.get(0);
            ArrayList<String> addressFragments = new ArrayList<>();

            for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }

            deliverResultToReceiver(Constants.SUCCESS_RESULT,
                    TextUtils.join(System.getProperty("line.separator"), addressFragments));
        }
    }

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, bundle);
    }
}
