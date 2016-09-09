package mobi.roko.testapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rokolabs.sdk.referrals.invite.RokoInviteFriends;

public class ReferralActivity extends AppCompatActivity {

    private Button showOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referrals);
        showOverlay = (Button) findViewById(R.id.show_overlay);
        showOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RokoInviteFriends.show(ReferralActivity.this);
            }
        });
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }
}
