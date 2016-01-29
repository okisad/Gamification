package com.oktaysadoglu.gamification.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.oktaysadoglu.gamification.R;
import com.oktaysadoglu.gamification.interfaces.CustomizeToolbarInteface;
import com.oktaysadoglu.gamification.services.NotificationService;

/**
 * Created by oktaysadoglu on 07/01/16.
 */
public class UserMainFragment extends Fragment implements View.OnClickListener{

    private Button playButton, profileButton, testButton, settingsButton;

    private Button sendNotificationButton;

    private CustomizeToolbarInteface customizeToolbarInteface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main,container,false);

        findViewsById(view);

        customizeScreen();

        registerListeners();

        return view;
    }

    public void findViewsById(View view){

        playButton = (Button) view.findViewById(R.id.fragment_main_play_button);

        profileButton = (Button) view.findViewById(R.id.fragment_main_profile_button);

        testButton = (Button) view.findViewById(R.id.fragment_main_test_button);

        settingsButton = (Button) view.findViewById(R.id.fragment_main_settings_button);

        /*sendNotificationButton = (Button) view.findViewById(R.id.fragment_main_send_notification_button);*/

    }

    private void customizeScreen(){

        customizeToolbarInteface = (CustomizeToolbarInteface) getActivity();

        customizeToolbarInteface.changeToolbarTitle("Menü");

    }

    private void registerListeners(){

        playButton.setOnClickListener(this);

        profileButton.setOnClickListener(this);

        testButton.setOnClickListener(this);

        settingsButton.setOnClickListener(this);

        /*sendNotificationButton.setOnClickListener(this);*/

    }

    public static Fragment newInstance(){

        return new UserMainFragment();

    }

    private void setFragment(Fragment fragment){

        Fragment f = fragment;

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.activity_fragment_container,f).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();

    }

    @Override
    public void onClick(View v) {

        if(v == playButton){

            customizeToolbarInteface.changeToolbarTitle("Oyna");

            setFragment(PlayLevelsListMainFragment.newInstance());

        }else if(v == profileButton){

            customizeToolbarInteface.changeToolbarTitle("Profil");

            setFragment(UserInformationMainFragment.newInstance());

        }else if (v == testButton){

            customizeToolbarInteface.changeToolbarTitle("Seviye Belirleme Testi");

            setFragment(UserStartTestMainFragment.newInstance());

        }else if (v == settingsButton){

            customizeToolbarInteface.changeToolbarTitle("Ayarlar");

            setFragment(UserSettingsMainFragment.newInstance());

        }else if(v == sendNotificationButton){

            NotificationService notificationService = new NotificationService();

            notificationService.sendNotification(getActivity());

        }

    }
}
