package com.mhandharbeni.edcandroid.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;

import com.mhandharbeni.edcandroid.constant.KeyboardConstants;

public class CustomKeyboard {
    private final String TAG = CustomKeyboard.class.getSimpleName();
    private KeyboardView mKeyboardView;
    private Activity mHostActivity;
    private KeyboardInterface keyboardInterface;

    public CustomKeyboard(Activity host, int viewId, int layoutId, final KeyboardInterface keyboardInterface) {
        this.mHostActivity = host;
        this.keyboardInterface = keyboardInterface;

        this.mKeyboardView = mHostActivity.findViewById(viewId);
        this.mKeyboardView.setKeyboard(new Keyboard(mHostActivity, layoutId));
        this.mKeyboardView.setPreviewEnabled(false);

        KeyboardView.OnKeyboardActionListener mOnKeyboardActionListener = new KeyboardView.OnKeyboardActionListener() {
            @Override
            public void onKey(int primaryCode, int[] keyCodes) {
                View focusCurrent = mHostActivity.getWindow().getCurrentFocus();
                if (focusCurrent == null ||
                        focusCurrent.getClass() != AppCompatEditText.class){
                    return;
                }
                EditText edittext = (EditText) focusCurrent;
                Editable editable = edittext.getText();
                int start = edittext.getSelectionStart();
                if (primaryCode == KeyboardConstants.codeBackSpace) {
                    if (editable != null && start > 0) editable.delete(start - 1, start);
                } else if(primaryCode == KeyboardConstants.codeClear) {
                    if (editable != null) editable.clear();
                } else if(primaryCode == KeyboardConstants.codeCheckNfc){
                    keyboardInterface.onNfcCheck();
                } else if(primaryCode == KeyboardConstants.codeCheckSam){
                    keyboardInterface.onSamCheck();
                } else if(primaryCode == KeyboardConstants.codePay){
                    keyboardInterface.onPay();
                } else if(primaryCode == KeyboardConstants.codeCheckSaldo){
                    keyboardInterface.onCheckBalance();
                } else {
                    editable.insert(start, Character.toString((char) primaryCode));
                }
            }

            @Override
            public void onPress(int arg0) {}

            @Override
            public void onRelease(int primaryCode) {}

            @Override
            public void onText(CharSequence text) {}

            @Override
            public void swipeDown() {}

            @Override
            public void swipeLeft() {}

            @Override
            public void swipeRight() {}

            @Override
            public void swipeUp() {}
        };
        this.mKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);
        this.mHostActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public boolean isCustomKeyboardVisible() {
        return mKeyboardView.getVisibility() == View.VISIBLE;
    }

    public void showCustomKeyboard(View v) {
        mKeyboardView.setVisibility(View.VISIBLE);
        mKeyboardView.setEnabled(true);
        if (v != null)
            ((InputMethodManager) mHostActivity.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void hideCustomKeyboard() {
        mKeyboardView.setVisibility(View.GONE);
        mKeyboardView.setEnabled(false);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void registerEditText(int resid) {
        EditText edittext = mHostActivity.findViewById(resid);
        edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) showCustomKeyboard(v);
                else hideCustomKeyboard();
            }
        });
        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomKeyboard(v);
            }
        });
        edittext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                EditText edittext = (EditText) v;
                int inType = edittext.getInputType();
                edittext.setInputType(InputType.TYPE_NULL);
                edittext.onTouchEvent(event);
                edittext.setInputType(inType);
                return true;
            }
        });
        edittext.setInputType(edittext.getInputType() | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    }

    public interface KeyboardInterface{
        void onDone();
        void onPay();
        void onCheckBalance();
        void onNfcCheck();
        void onSamCheck();
        void onToggleSam();
        void onToggleNfc();
    }
}
