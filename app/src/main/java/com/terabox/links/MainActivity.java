package com.terabox.links;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

        WebSettings settings = myWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);

        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Manejar TeraBox, PayPal y esquemas de intencion externos
                if (url.contains("terabox.com") || url.contains("paypal.com") || url.startsWith("whatsapp://") || url.startsWith("intent://")) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                }
                return false;
            }
        });

        // Tu URL de GitHub Pages
        myWebView.loadUrl("https://linksterabox.github.io");

        // Boton de Chat de Soporte (WhatsApp)
        fabChat.setOnClickListener(v -> {
            String whatsappUrl = "https://wa.me/+525621896010"; // Reemplaza con tu numero real
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(whatsappUrl));
            startActivity(intent);
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
