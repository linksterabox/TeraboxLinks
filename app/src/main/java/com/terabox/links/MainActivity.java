package com.terabox.links;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myWebView = findViewById(R.id.webview);
        FloatingActionButton fabChat = findViewById(R.id.fab_chat);

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);

        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Manejo de links de PayPal y TeraBox para abrirlos correctamente
                if (url.contains("paypal.com") || url.contains("terabox.com") || url.startsWith("intent://")) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    } catch (Exception e) {
                        return false; 
                    }
                }
                return false; // Carga normal dentro del WebView
            }
        });

        myWebView.loadUrl("https://linksterabox.github.io");

        // Configuración del botón de Chat
        fabChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Reemplaza con tu número de WhatsApp
                String wpUrl = "https://wa.me/+525621896010"; 
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(wpUrl));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (myWebView.canGoBack()) {
            myWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
