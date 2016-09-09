package mobi.roko.testapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.rokolabs.sdk.RokoMobi;
import com.rokolabs.sdk.account.RokoAccount;
import com.rokolabs.sdk.account.model.User;
import com.rokolabs.sdk.analytics.Event;
import com.rokolabs.sdk.analytics.RokoLogger;
import com.rokolabs.sdk.http.Response;
import com.rokolabs.sdk.http.ResponseCallback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RokoMobi.start(this);
        RokoLogger.addEvents(new Event("Android Tutorial. Application Started!"));
        RokoAccount.setUser(this, "testapp@email.com", null, null, new ResponseCallback() {
            @Override
            public void success(Response response) {
                User currentUser = RokoAccount.getLoginUser(MainActivity.this);

                RokoLogger.addEvents(
                        new Event("Android Tutorial. User identified").set("User ID",currentUser.objectId));
            }

            @Override
            public void failure(Response response) {
                Log.e("MainActivity", "Set user: failure: " + response);
            }});
    }
}
