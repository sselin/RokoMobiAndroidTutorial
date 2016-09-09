package mobi.roko.testapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rokolabs.sdk.base.BaseResponse;
import com.rokolabs.sdk.promo.RokoPromo;
import com.rokolabs.sdk.promo.RokoPromoDeliveryType;
import com.rokolabs.sdk.referrals.ResponseActivatedDiscountsList;
import com.rokolabs.sdk.referrals.RokoReferrals;

public class EcommerceActivity extends AppCompatActivity {
    private EditText quantity;
    private TextView total;
    private TextView message;
    private TextView discount;
    private float discountValue;
    private double codeDiscount;
    String referralCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecommerce);
        quantity = (EditText) findViewById(R.id.quantity);
        total = (TextView) findViewById(R.id.total);
        message = (TextView) findViewById(R.id.message);
        discount = (TextView) findViewById(R.id.discount);
        SharedPreferences sharedPreferences = this.getSharedPreferences("Promo", Context.MODE_PRIVATE);
        discountValue = sharedPreferences.getFloat("value", 0);
        if (discountValue > 0) {
            discount.setText("$" + discountValue);
        }

        referralCode = getSharedPreferences("referrals", Context.MODE_PRIVATE).getString("referral_code", null);


        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int value = Integer.valueOf(charSequence.toString().isEmpty() ? "0" : charSequence.toString()) * 10;
                total.setText("$" + value);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private int value;

    public void executePurchase(View view) {
        value = Integer.valueOf(quantity.getText().toString().isEmpty() ? "0" : quantity.getText().toString()) * 10;
        if (value > 0) {



            if (referralCode != null) {
                getSharedPreferences("referrals", Context.MODE_PRIVATE).edit().clear().apply();
                final long discountId = getSharedPreferences("discountID", Context.MODE_PRIVATE).getLong("discount", -1L);
                RokoReferrals.loadReferralDiscountsList(new RokoReferrals.OnLoadReferralDiscountsList() {
                    @Override
                    public void success(final ResponseActivatedDiscountsList responseActivatedDiscountsList) {
                        if (responseActivatedDiscountsList.data.length > 0) {
                            codeDiscount = responseActivatedDiscountsList.data[0].value;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    value = (int) (value - discountValue - codeDiscount);
                                    SharedPreferences sharedPreferences = getSharedPreferences("Promo", Context.MODE_PRIVATE);
                                    String code = sharedPreferences.getString("code", "");
                                    int deliveryType = sharedPreferences.getInt("deliveryType", 0);
                                    message.setText("Purchase of " + quantity.getText().toString() + " items for $" + value + " was successful");
                                    RokoPromo.markPromoCodeAsUsed(code, value, discountValue, RokoPromoDeliveryType.values()[deliveryType], null);
                                    RokoReferrals.markReferralDiscountAsUsed(responseActivatedDiscountsList.data[0].objectId, new RokoReferrals.OnMarkReferralDiscountAsUsed() {
                                        @Override
                                        public void success(BaseResponse baseResponse) {

                                        }

                                        @Override
                                        public void failure(String s) {

                                        }
                                    });
                                }
                            });
                        }
                    }

                    @Override
                    public void failure(String s) {
                        Log.i("test", "test1");
                    }
                });

            } else {
                value = (int) (value - discountValue);
                SharedPreferences sharedPreferences = this.getSharedPreferences("Promo", Context.MODE_PRIVATE);
                String code = sharedPreferences.getString("code", "");
                int deliveryType = sharedPreferences.getInt("deliveryType", 0);
                RokoPromo.markPromoCodeAsUsed(code, value, discountValue, RokoPromoDeliveryType.values()[deliveryType], null);
                message.setText("Purchase of " + quantity.getText().toString() + " items for $" + value + " was successful");
            }
        } else {
            message.setText("Please, enter quantity greater than zero");
        }
    }
}
