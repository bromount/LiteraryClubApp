package com.example.VecLiteraryClubApp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class form_answer extends AppCompatActivity {

    WebViewClientCustom custom_client;
    WebView form_webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_answer);

        getWindow().setNavigationBarColor(Color.BLACK);

        form_webview = findViewById(R.id.form_answer_webview);

        custom_client = new WebViewClientCustom();

        form_webview.canGoBackOrForward(3);
        form_webview.setVerticalScrollBarEnabled(true);
        form_webview.getSettings().setJavaScriptEnabled(true);
        form_webview.setWebViewClient(custom_client);


        form_webview.loadUrl(getIntent().getStringExtra("URL"));


    }
}
