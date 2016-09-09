package mobi.roko.testapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.rokolabs.sdk.RokoMobi;
import com.rokolabs.sdk.links.ResponseVanityLink;
import com.rokolabs.sdk.links.RokoLinks;
import com.rokolabs.sdk.promo.ResponsePromo;
import com.rokolabs.sdk.promo.RokoPromo;
import com.rokolabs.sdk.promo.RokoPromoDeliveryType;

public class DeepLinksActivity extends AppCompatActivity {
    private TextView promo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_links);
        final TextView textView = (TextView) findViewById(R.id.deeplink_info);
        promo = (TextView) findViewById(R.id.promo);
        RokoMobi.start(this);
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
        if (getIntent().getData() != null) {
            String promocode = getIntent().getData().getPathSegments().get(0);
            getPromoInfo(promocode);
        }
    }
    public void getPromoInfo(final String code) {
        RokoPromo.loadPromoDiscountWithPromoCode(code, new RokoPromo.CallbackDiscountLoaded() {
            @Override
            public void success(final ResponsePromo responsePromo) {
                Log.i("loadPromoDiscount", "success");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences sharedPreferences = DeepLinksActivity.this.getSharedPreferences("Promo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("code", code);
                        editor.putFloat("value", (float) responsePromo.data.value);
                        editor.putInt("deliveryType", RokoPromoDeliveryType.LINK.ordinal()).commit();

                        String text = "Code: " + code +
                                "\nCan be used: " + responsePromo.data.canBeUsed + "\n" +
                                "Value: " + responsePromo.data.value + "\n" +
                                "Type: " + responsePromo.data.type;
                        promo.setText(text);
                    }
                });
            }

            @Override
            public void failure(ResponsePromo responsePromo) {
                Log.i("loadPromoDiscount", "failure");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        promo.setText("Failed to get promo info");
                    }
                });
            }
        });
    }
    public void launchEcommerce(View view) {
        startActivity(new Intent(this, EcommerceActivity.class));
    }
}
