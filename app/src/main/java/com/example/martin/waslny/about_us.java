package com.example.martin.waslny;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class about_us extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        WebView web = findViewById(R.id.web);
        web.setWebViewClient(new WebViewClient());
        web.loadUrl("https://waslnyangaz.firebaseapp.com/");
    }


   /* AdView mAdView;
    @Override
    protected void onStart() {
        super.onStart();

        MobileAds.initialize(this, "ca-app-pub-9940348601431996~3658002235");
        mAdView = findViewById(R.id.adView);
        final AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                if (errorCode == 3)
                    Toast.makeText(about_us.this, "banner " + errorCode + " ERROR_CODE_NO_FILL", Toast.LENGTH_SHORT).show();
                if (errorCode == 2)
                    Toast.makeText(about_us.this, "banner " + errorCode + " ERROR_CODE_NETWORK_ERROR", Toast.LENGTH_SHORT).show();
                if (errorCode == 1)
                    Toast.makeText(about_us.this, "banner " + errorCode + " ERROR_CODE_INVALID_REQUEST", Toast.LENGTH_SHORT).show();
            }

        });
    }*/
}
