package com.mhandharbeni.edcandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.irozon.sneaker.Sneaker;
import com.mhandharbeni.edcandroid.fragments.TapCardFragment;
import com.mhandharbeni.edcandroid.utils.CustomKeyboard;
import com.mhandharbeni.edcandroid.utils.CustomTextWatcher;
import com.mhandharbeni.edcandroid.utils.NumberTextWatcher;

public class MainActivity extends AppCompatActivity implements CustomKeyboard.KeyboardInterface, TapCardFragment.TapCallback {
    CustomKeyboard customKeyboard;
    EditText amount;
    EditText amountHide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        amount = findViewById(R.id.amount);
        amountHide = findViewById(R.id.amountHide);

        amountHide.requestFocus();

        customKeyboard = new CustomKeyboard(this, R.id.keyboardview, R.xml.num_pad, this);
        customKeyboard.registerEditText(R.id.amountHide);

        amountHide.addTextChangedListener(new CustomTextWatcher(amountHide, amount));
        amount.addTextChangedListener(new NumberTextWatcher(amount));
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onDone() {
        Toast.makeText(this, "DONE", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPay() {
        if (!amount.getText().toString().isEmpty()){
            TapCardFragment tapCardFragment = TapCardFragment.getInstance(this, this);
            tapCardFragment.setCancelable(false);
            tapCardFragment.show(getSupportFragmentManager(), tapCardFragment.getTag());
        } else {
            Sneaker.with(this)
                    .autoHide(true)
                    .setDuration(3000)
                    .setTitle("Payment")
                    .setMessage("Please Enter Amount First!")
                    .sneakError();

        }
    }

    @Override
    public void onCheckBalance() {

    }

    @Override
    public void onNfcCheck() {

    }

    @Override
    public void onSamCheck() {
    }

    @Override
    public void onToggleSam() {
        String message = "SAM Activated";
        if (!CustomKeyboard.defaulSamState){
            message = "SAM Deactivated";
        }
        Sneaker.with(this)
                .autoHide(true)
                .setDuration(3000)
                .setTitle("SAM Operation")
                .setMessage(message)
                .sneakSuccess();
    }

    @Override
    public void onToggleNfc() {
        String message = "NFC Activated";
        if (!CustomKeyboard.defaultNfcState){
            message = "NFC Deactivated";
        }
        Sneaker.with(this)
                .autoHide(true)
                .setDuration(3000)
                .setTitle("NFC Operation")
                .setMessage(message)
                .sneakSuccess();
    }

    @Override
    public void onCardTapped() {
        Sneaker.with(this)
                .autoHide(true)
                .setDuration(3000)
                .setTitle("Tap Card")
                .setMessage("Tap Card Success")
                .sneakSuccess();
    }

    @Override
    public void onCardTappedCancelled() {
        Sneaker.with(this)
                .autoHide(true)
                .setDuration(3000)
                .setTitle("Tap Card")
                .setMessage("Tap Card Cancelled")
                .sneakWarning();
    }

    @Override
    public void onCardTimeout() {
        Sneaker.with(this)
                .autoHide(true)
                .setDuration(3000)
                .setTitle("Tap Card")
                .setMessage("Tap Card Timeout")
                .sneakError();
    }
}