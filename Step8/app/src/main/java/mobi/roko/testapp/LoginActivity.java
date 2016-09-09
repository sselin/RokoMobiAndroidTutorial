package mobi.roko.testapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.rokolabs.sdk.account.RokoAccount;
import com.rokolabs.sdk.http.Response;
import com.rokolabs.sdk.http.ResponseCallback;
import com.rokolabs.sdk.referrals.ResponseActivatedDiscount;
import com.rokolabs.sdk.referrals.RokoReferrals;

public class LoginActivity extends AppCompatActivity {
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        activity = this;
        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = String.valueOf(((EditText) findViewById(R.id.email)).getText());
                final String referralCode = String.valueOf(((EditText) findViewById(R.id.referral)).getText());
                if (referralCode != null) {
                    getSharedPreferences("referrals", Context.MODE_PRIVATE).edit().putString("referral_code", referralCode).apply();
                }

                RokoAccount.setUser(getApplicationContext(), email, referralCode, null, new ResponseCallback() {
                    @Override
                    public void success(final Response response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Logined", Toast.LENGTH_SHORT).show();
                                RokoReferrals.activateDiscountWithCode(referralCode, new RokoReferrals.OnActivateDiscountWithCode() {
                                    @Override
                                    public void success(final ResponseActivatedDiscount responseActivatedDiscount) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                getSharedPreferences("discountID", Context.MODE_PRIVATE).edit().putLong("discount", responseActivatedDiscount.discount.data.objectId).apply();
                                                Toast.makeText(getApplicationContext(), responseActivatedDiscount.discount.data.objectId + " Activated", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void failure(String s) {

                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void failure(Response response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

            }
        });
    }
}