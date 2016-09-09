package mobi.roko.testapp;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.rokolabs.sdk.RokoMobi;
import com.rokolabs.sdk.account.RokoAccount;
import com.rokolabs.sdk.account.model.User;
import com.rokolabs.sdk.analytics.Event;
import com.rokolabs.sdk.analytics.RokoLogger;
import com.rokolabs.sdk.http.Response;
import com.rokolabs.sdk.http.ResponseCallback;
import com.rokolabs.sdk.push.RokoPush;

public class MainActivity extends AppCompatActivity {
    private WebView mainWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainWebView = (WebView) findViewById(R.id.mainWebView);
        mainWebView.setWebViewClient(new MyBrowser());
        String[] permissions = {"android.permission.READ_CONTACTS", "android.permission.READ_PHONE_STATE"};
        ActivityCompat.requestPermissions(this, permissions, 0);
        RokoMobi.start(this);
        RokoPush.start("1001951760913");
        RokoLogger.addEvents(new Event("Android Tutorial. Application Started!"));
        RokoAccount.setUser(this, "testapp@email.com", null, null, new ResponseCallback() {
            @Override
            public void success(Response response) {
                User currentUser = RokoAccount.getLoginUser(MainActivity.this);

                RokoLogger.addEvents(
                        new Event("Android Tutorial. User identified").set("User ID", currentUser.objectId));
            }

            @Override
            public void failure(Response response) {
                Log.e("MainActivity", "Set user: failure: " + response);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.content_a:
                Log.i("MENU", "Content A");
                mainWebView.loadUrl("https://www.google.com");
                break;
            case R.id.content_b:
                Log.i("MENU", "Content B");
                mainWebView.loadUrl("https://www.apple.com");
                break;
            case R.id.content_c:
                Log.i("MENU", "Content C");
                mainWebView.loadUrl("https://www.facebook.com");
                break;
        }
        return true;
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
