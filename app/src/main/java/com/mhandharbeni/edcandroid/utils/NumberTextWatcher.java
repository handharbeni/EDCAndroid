package com.mhandharbeni.edcandroid.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.ParseException;

public class NumberTextWatcher implements TextWatcher {
    private DecimalFormat df;
    private DecimalFormat dfnd;
    private boolean hasFractionalPart;

    private EditText etSource;

    public NumberTextWatcher(EditText etSource)
    {
        df = new DecimalFormat("#,###.##");
        df.setDecimalSeparatorAlwaysShown(true);
        dfnd = new DecimalFormat("#,###");
        this.etSource = etSource;
        hasFractionalPart = false;
    }

    @SuppressWarnings("unused")
    private static final String TAG = "NumberTextWatcher";

    public void afterTextChanged(Editable s)
    {
        etSource.removeTextChangedListener(this);

        try {
            int inilen, endlen;
            inilen = etSource.getText().length();

            String v = s.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "");
            Number n = df.parse(v);
            int cp = etSource.getSelectionStart();
            if (hasFractionalPart) {
                etSource.setText(df.format(n));
            } else {
                etSource.setText(dfnd.format(n));
            }
            endlen = etSource.getText().length();
            int sel = (cp + (endlen - inilen));
            if (sel > 0 && sel <= etSource.getText().length()) {
                etSource.setSelection(sel);
            } else {
                // place cursor at the end?
                etSource.setSelection(etSource.getText().length() - 1);
            }
        } catch (NumberFormatException | ParseException nfe) {
            // do nothing?
        }

        etSource.addTextChangedListener(this);
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        if (s.toString().contains(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator())))
        {
            hasFractionalPart = true;
        } else {
            hasFractionalPart = false;
        }
    }
}
