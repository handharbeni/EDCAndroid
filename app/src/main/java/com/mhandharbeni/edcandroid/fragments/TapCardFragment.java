package com.mhandharbeni.edcandroid.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mhandharbeni.edcandroid.R;
import com.mhandharbeni.edcandroid.constant.AppConstants;

import static com.mhandharbeni.edcandroid.constant.AppConstants.CountDownMillis;
import static com.mhandharbeni.edcandroid.constant.AppConstants.CountDownStep;

public class TapCardFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    Activity activity;
    TapCallback tapCallback;

    private View view;
    private CountDownTimer countDownTimer;

    boolean cardDetected = true;

    private TextView countDownTap;
    private Button btnCancel;

    public static TapCardFragment getInstance(Activity activity, TapCallback tapCallback){
        return new TapCardFragment(activity, tapCallback);
    }

    public TapCardFragment(Activity activity, TapCallback tapCallback) {
        this.activity = activity;
        this.tapCallback = tapCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.view_tap_card, container, false);

        countDownTap = view.findViewById(R.id.countDownTap);
        btnCancel = view.findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (countDownTimer!=null){
            countDownTimer.cancel();
            countDownTimer = null;
        }

        countDownTimer = new CountDownTimer(CountDownMillis, CountDownStep) {
            @Override
            public void onTick(final long l) {
                String currentCount = String.valueOf(l / CountDownStep);
                countDownTap.setText(String.format("%ss", currentCount));
            }

            @Override
            public void onFinish() {
                if (!cardDetected){
                    tapCallback.onCardTimeout();
                    dismiss();
                } else {
                    tapCallback.onCardTapped();
                    dismiss();
                }
            }
        };
        countDownTimer.start();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnCancel){
            tapCallback.onCardTappedCancelled();
            dismiss();
        }
    }

    public interface TapCallback{
        void onCardTapped();
        void onCardTappedCancelled();
        void onCardTimeout();
    }
}
