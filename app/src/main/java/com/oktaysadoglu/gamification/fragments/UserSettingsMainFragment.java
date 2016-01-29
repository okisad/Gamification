package com.oktaysadoglu.gamification.fragments;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.oktaysadoglu.gamification.R;
import com.oktaysadoglu.gamification.preferences.UserSettings;
import com.oktaysadoglu.gamification.preferences.UserStatus;
import com.oktaysadoglu.gamification.services.NotificationService;

import java.util.Calendar;

/**
 * Created by oktaysadoglu on 11/12/15.
 */

public class UserSettingsMainFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, NumberPicker.OnValueChangeListener,SharedPreferences.OnSharedPreferenceChangeListener,View.OnClickListener {

    public static final String TAG = "user_set_fragment";

    private TextView mSavedNotificationRepeatText;

    private Switch mSwitchOnOff;

    private Button mDetermineRepeatNumberButton;

    private UserSettings mUserSettings;

    private UserStatus mUserStatus;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mUserSettings = UserSettings.getInstance(getActivity());

        mUserStatus = UserStatus.getInstance(getActivity());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_settings, container, false);

        findViewsById(view);

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

        setPreviousComponentStatus();

        registerListeners(true);

    }

    @Override
    public void onPause() {
        super.onPause();

        registerListeners(false);
    }


    private void findViewsById(View view) {

        mSavedNotificationRepeatText = (TextView) view.findViewById(R.id.fragment_user_settings_saved_repeat_text);

        mSwitchOnOff = (Switch) view.findViewById(R.id.fragment_user_settings_notification_switch);

        mDetermineRepeatNumberButton = (Button) view.findViewById(R.id.fragment_user_settings_determine_repeat_button);

    }


    public static UserSettingsMainFragment newInstance(){

        return new UserSettingsMainFragment();

    }

    private void setPreviousComponentStatus(){

        mDetermineRepeatNumberButton.setText(String.valueOf(mUserSettings.getRepeat()));

        mSavedNotificationRepeatText.setText(mDetermineRepeatNumberButton.getText());

        boolean onOff = mUserSettings.getNotificationIsOn();

        mSwitchOnOff.setChecked(onOff);

        if(onOff){

            mSavedNotificationRepeatText.setVisibility(View.VISIBLE);

            if(mUserSettings.getTempRepeat() == 0){

                mSavedNotificationRepeatText.setVisibility(View.GONE);

            }

        }else {

            mSavedNotificationRepeatText.setVisibility(View.GONE);

        }

    }

    public static ColorFilter getColorFilter(String color){

        int iColor = Color.parseColor(color);

        int red   = (iColor & 0xFF0000) / 0xFFFF;
        int green = (iColor & 0xFF00) / 0xFF;
        int blue  = iColor & 0xFF;

        float[] matrix = { 0, 0, 0, 0, red,
                0, 0, 0, 0, green,
                0, 0, 0, 0, blue,
                0, 0, 0, 1, 0 };

        ColorFilter colorFilter = new ColorMatrixColorFilter(matrix);

        return colorFilter;

    }


    private void registerListeners(boolean isRegister){

        if (isRegister){

            mSwitchOnOff.setOnCheckedChangeListener(this);

            mDetermineRepeatNumberButton.setOnClickListener(this);

            PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);

        }else {

            PreferenceManager.getDefaultSharedPreferences(getActivity()).unregisterOnSharedPreferenceChangeListener(this);

        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        NotificationService notificationService = new NotificationService();

        if(isChecked){

            int repeat = Integer.parseInt(mDetermineRepeatNumberButton.getText().toString());
            mUserSettings.setRepeat(repeat);
            mUserSettings.setTempRepeat(repeat);
            prepareUserSettingIntervalRepeat(repeat);

            mUserSettings.setNotificationIsOn(true);
            mUserSettings.setWeekOfDay(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));

            mSavedNotificationRepeatText.setVisibility(View.VISIBLE);

            Toast.makeText(getActivity(), "Notification " + mUserSettings.getRepeat() + " tekrar sayısıyla başarıyla açıldı", Toast.LENGTH_LONG).show();

            notificationService.setService(getActivity(), true);

        }else {

            mUserSettings.setNotificationIsOn(false);

            mSavedNotificationRepeatText.setVisibility(View.GONE);

            Toast.makeText(getActivity(), "Notification başarı ile kapatıldı", Toast.LENGTH_LONG).show();

            android.util.Log.e(TAG, mUserSettings.toString());

            notificationService.setService(getActivity(), false);

        }

        setPreviousComponentStatus();

    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        mSavedNotificationRepeatText.setText(String.valueOf(newVal));

        if (mSwitchOnOff.isChecked()) {

            int repeat = newVal;

            mUserSettings.setRepeat(repeat);
            mUserSettings.setTempRepeat(repeat);

            prepareUserSettingIntervalRepeat(repeat);

            mUserSettings.setNotificationIsOn(true);
            mUserSettings.setInstantNotificationIsOn(true);
            mUserSettings.setWeekOfDay(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));

            NotificationService notificationService = new NotificationService();

            notificationService.setService(getActivity(), true);

            setPreviousComponentStatus();

        }




    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if(key.equals(UserSettings.TEMP_REPEAT)){

            setPreviousComponentStatus();

        }else if (key.equals(UserSettings.NOTIFICATION_IS_ON)){

            setPreviousComponentStatus();

        }else if(key.equals(UserSettings.INSTANT_NOTIFICATION_IS_ON)){

            setPreviousComponentStatus();

        }

    }

    private void prepareUserSettingIntervalRepeat(int repeat){

        if (repeat == 1) {

            mUserSettings.setInterval(0);
            mUserSettings.setTempInterval(0);

        } else if (repeat == 2) {

            mUserSettings.setInterval(6);
            mUserSettings.setTempInterval(6);

        } else if (repeat == 3) {

            mUserSettings.setInterval(5);
            mUserSettings.setTempInterval(5);

        } else if (repeat == 4) {

            mUserSettings.setInterval(3);
            mUserSettings.setTempInterval(3);

        } else if (repeat == 5) {

            mUserSettings.setInterval(2);
            mUserSettings.setTempInterval(2);

        } else if (repeat == 6) {

            mUserSettings.setInterval(2);
            mUserSettings.setTempInterval(2);

        } else {

            mUserSettings.setInterval(1);
            mUserSettings.setTempInterval(1);

        }

    }

    @Override
    public void onClick(View v) {

        if(v == mDetermineRepeatNumberButton){

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();

            View view = inflater.inflate(R.layout.fragment_user_settings_repeat_dialog,null);

            final NumberPicker numberPicker = (NumberPicker) view.findViewById(R.id.fragment_user_settings_repeat_dialog_number_picker);

            numberPicker.setMinValue(1);

            numberPicker.setMaxValue(10);

            numberPicker.setValue(mUserSettings.getRepeat());

            builder.setTitle("Günlük Tekrar Sayısı").setView(view)
            .setPositiveButton("Uygula", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int repeat = numberPicker.getValue();

                    mUserSettings.setRepeat(repeat);
                    mUserSettings.setTempRepeat(repeat);

                    prepareUserSettingIntervalRepeat(repeat);

                    mUserSettings.setNotificationIsOn(true);
                    mUserSettings.setInstantNotificationIsOn(true);
                    mUserSettings.setWeekOfDay(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));

                    NotificationService notificationService = new NotificationService();

                    notificationService.setService(getActivity(), true);

                }
            })
                    .setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            AlertDialog alertDialog = builder.create();

            alertDialog.show();

        }

    }

}
