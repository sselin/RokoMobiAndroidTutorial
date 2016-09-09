package mobi.roko.testapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.rokolabs.sdk.links.ResponseVanityLink;
import com.rokolabs.sdk.links.RokoLinks;

public class DeepLinksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_links);
        final TextView textView = (TextView) findViewById(R.id.deeplink_info);
        RokoLinks.getByVanityLinkCmd(this, getIntent(), new RokoLinks.CallbackVanityLink() {
            @Override
            public void success(final ResponseVanityLink res) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(res.data.name);
                    }
                });

            }

            @Override
            public void failure(final String res) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText("Failure");
                    }
                });
            }
        });
    }
}
