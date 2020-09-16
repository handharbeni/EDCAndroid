package com.mhandharbeni.edcandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.mhandharbeni.edcandroid.utils.CustomKeyboard;

public class MainActivity extends AppCompatActivity implements CustomKeyboard.KeyboardInterface {
    CustomKeyboard customKeyboard;
    EditText amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        amount = findViewById(R.id.amount);
        customKeyboard = new CustomKeyboard(this, R.id.keyboardview, R.xml.num_pad, this);
        customKeyboard.registerEditText(R.id.amount);
    }

    @Override
    public void onBackPressed() {
        customKeyboard.hideCustomKeyboard();
    }

    @Override
    public void onDone() {
        Toast.makeText(this, "DONE", Toast.LENGTH_SHORT).show();
        customKeyboard.hideCustomKeyboard();
    }
}