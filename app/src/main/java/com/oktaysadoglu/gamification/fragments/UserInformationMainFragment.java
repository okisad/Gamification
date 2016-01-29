package com.oktaysadoglu.gamification.fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.oktaysadoglu.gamification.R;
import com.oktaysadoglu.gamification.preferences.UserSettings;
import com.oktaysadoglu.gamification.preferences.UserStatus;

/**
 * Created by oktaysadoglu on 11/12/15.
 */
public class UserInformationMainFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    public static final String TAG = "user_info_fragment";

    private TextView seenWordsTextView,knownWordsTextView,notKnownWordsTextView;

    private TextView levelTextView;

    private TextView notificationNumberTextView;

    private ProgressBar progressBarSeenWords,progressBarKnownWords,progressBarNotKnownWords;

    private UserStatus userStatus;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userStatus = UserStatus.getInstance(getActivity());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_information, container, false);

        findViewsById(view);

        registerListener();

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

        setInformations();

    }

    private void findViewsById(View view) {

        seenWordsTextView = (TextView) view.findViewById(R.id.fragment_user_information_seen_words_number);

        knownWordsTextView = (TextView) view.findViewById(R.id.fragment_user_information_known_words_number);

        notKnownWordsTextView = (TextView) view.findViewById(R.id.fragment_user_information_not_known_words_number);

        levelTextView = (TextView) view.findViewById(R.id.fragment_user_information_level_text);

        notificationNumberTextView = (TextView) view.findViewById(R.id.fragment_user_information_notification_number_text);

        progressBarSeenWords = (ProgressBar) view.findViewById(R.id.fragment_user_information_seen_words_progress_bar);

        progressBarKnownWords = (ProgressBar) view.findViewById(R.id.fragment_user_information_known_words_progress_bar);

        progressBarNotKnownWords = (ProgressBar) view.findViewById(R.id.fragment_user_information_not_known_words_progress_bar);

    }

    private void registerListener(){

        PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);

    }

    private void setInformations() {



        progressBarSeenWords.setMax(100);

        progressBarKnownWords.setMax(100);

        progressBarNotKnownWords.setMax(100);

        progressBarSeenWords.getProgressDrawable().setColorFilter(UserSettingsMainFragment.getColorFilter("#5cb85c"));
        progressBarNotKnownWords.getProgressDrawable().setColorFilter(UserSettingsMainFragment.getColorFilter("#d9534f"));
        progressBarKnownWords.getProgressDrawable().setColorFilter(UserSettingsMainFragment.getColorFilter("#5cb85c"));

        int ratioSeenWords = (userStatus.getSeenWords()*100)/5000;

        progressBarSeenWords.setProgress(ratioSeenWords);

        int seenWords;

        if(userStatus.getSeenWords() == 0){

            seenWords = 1;

        }else {

            seenWords = userStatus.getSeenWords();

        }

        int ratioKnownWords = (userStatus.getKnownWords()*100)/seenWords;

        progressBarKnownWords.setProgress(ratioKnownWords);

        int ratioNotKnownWords = (userStatus.getNotKnownWords()*100)/seenWords;

        progressBarNotKnownWords.setProgress(ratioNotKnownWords);

        String seenWordsText = userStatus.getSeenWords() + "/5000 (%"+ ratioSeenWords  +")";

        seenWordsTextView.setText(seenWordsText);

        String knownWordsText = userStatus.getKnownWords() + "/" + userStatus.getSeenWords()+" (%"+ ratioKnownWords +")";

        knownWordsTextView.setText(knownWordsText);

        String notKnownWordsText = userStatus.getNotKnownWords() + "/" + userStatus.getSeenWords()+ " (%"+ratioNotKnownWords+")";

        notKnownWordsTextView.setText(notKnownWordsText);

        String levelText = userStatus.getLevel() + "/50";

        levelTextView.setText(levelText);

        UserSettings userSettings = UserSettings.getInstance(getActivity());

        notificationNumberTextView.setText(String.valueOf(userSettings.getRepeat()));

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(UserStatus.KNOWN_WORDS) || key.equals(UserStatus.LEVEL)|| key.equals(UserStatus.NOT_KNOWN_WORDS) ||key.equals(UserStatus.SEEN_WORDS)){

            setInformations();

        }
    }

    public static UserInformationMainFragment newInstance(){

        return new UserInformationMainFragment();

    }


}
