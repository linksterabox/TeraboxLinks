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
                // Manejo de enlaces externos (PayPal, TeraBox, WhatsApp)
                if (url.contains("paypal.com") || url.contains("terabox.com") || url.startsWith("whatsapp://") || url.startsWith("intent://")) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    } catch (Exception e) {
                        // Si falla la app externa, intentar cargar en el navegador del sistema
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browserIntent);
                        return true;
                    }
                }
                return false; // Carga el resto en el WebView
            }
        });

        myWebView.loadUrl("https://linksterabox.github.io");

        fabChat.setOnClickListener(v -> {
            String wpUrl = "https://wa.me/1234567890"; // CAMBIA ESTO POR TU NUMERO
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(wpUrl)));
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
