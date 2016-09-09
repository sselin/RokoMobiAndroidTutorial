package mobi.roko.testapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rokolabs.sdk.promo.RokoPromo;
import com.rokolabs.sdk.promo.RokoPromoDeliveryType;

public class EcommerceActivity extends AppCompatActivity {
    private EditText quantity;
    private TextView total;
    private TextView message;
    private TextView discount;
    private float discountValue;

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
    

    public void executePurchase(View view) {
        int value = Integer.valueOf(quantity.getText().toString().isEmpty() ? "0" : quantity.getText().toString()) * 10;
        if (value > 0) {
            value = (int) (value - discountValue);
            message.setText("Purchase of " + quantity.getText().toString() + " items for $" + value + " was successful");

            SharedPreferences sharedPreferences = this.getSharedPreferences("Promo", Context.MODE_PRIVATE);
            String code = sharedPreferences.getString("code", "");
            int deliveryType = sharedPreferences.getInt("deliveryType", 0);
            RokoPromo.markPromoCodeAsUsed(code, value, discountValue, RokoPromoDeliveryType.values()[deliveryType], null);
        } else {
            message.setText("Please, enter quantity greater than zero");
        }
    }
}
