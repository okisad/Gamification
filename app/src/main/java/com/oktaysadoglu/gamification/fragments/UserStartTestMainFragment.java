package com.oktaysadoglu.gamification.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.oktaysadoglu.gamification.DAO.WordDAO;
import com.oktaysadoglu.gamification.R;
import com.oktaysadoglu.gamification.activities.TestWordPagerActivity;
import com.oktaysadoglu.gamification.model.Word;
import com.oktaysadoglu.gamification.preferences.UserStatus;
import com.oktaysadoglu.gamification.services.NotificationService;

import java.util.List;

/**
 * Created by oktaysadoglu on 03/01/16.
 */
public class UserStartTestMainFragment extends Fragment implements View.OnClickListener{

    private Button mStartButton;

    private UserStatus mUserStatus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_test,container,false);

        findViewsId(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        setListeners();
    }

    private void findViewsId(View view){

        mStartButton = (Button) view.findViewById(R.id.fragment_user_test_start_button);

        mUserStatus = UserStatus.getInstance(getActivity());

    }

    private void setListeners(){

        mStartButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if(v == mStartButton){

            int level = mUserStatus.getLevel();

            if(level == 1){

                Intent intent = new Intent(getActivity(), TestWordPagerActivity.class);

                startActivity(intent);

            }else {

                Toast.makeText(getActivity(),"Sadece levelınız 1 ise seviye belirleme testini uygulayabilirsiniz.",Toast.LENGTH_LONG).show();

            }

            /*NotificationService notificationService = new NotificationService();

            notificationService.sendNotification(getActivity());*/

            /*WordDAO wordDAO = new WordDAO(getActivity());

            List<Word> wordList = wordDAO.getNotSeenWordUntilSpecificLevel(2);

            Log.e("my",wordList.toString());*/

        }
    }

    public static UserStartTestMainFragment newInstance(){

        return new UserStartTestMainFragment();

    }
}
