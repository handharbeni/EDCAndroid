package com.mhandharbeni.edcandroid.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class CustomTextWatcher implements TextWatcher {
    EditText etSource;
    EditText etTarget;

    public CustomTextWatcher(EditText etSource, EditText etTarget) {
        this.etSource = etSource;
        this.etTarget = etTarget;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        etSource.removeTextChangedListener(this);
        etTarget.setText(etSource.getText().toString());
        etSource.addTextChangedListener(this);
    }
}
