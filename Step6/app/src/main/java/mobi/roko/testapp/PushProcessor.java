package mobi.roko.testapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.rokolabs.sdk.RokoMobi;
import com.rokolabs.sdk.push.PushData;
import com.rokolabs.sdk.push.RokoPush;

public class PushProcessor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_push_processor);
        final TextView pushMessageTextView = (TextView) findViewById(R.id.push_message);

        RokoMobi.start(this);
        RokoPush.notificationOpened(getIntent(), new RokoPush.CallbackNotificationOpened() {
            @Override
            public void success(final PushData data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pushMessageTextView.setText(data.message);
                    }
                });
            }
        });
    }
}
