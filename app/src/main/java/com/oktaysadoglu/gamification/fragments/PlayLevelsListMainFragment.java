package com.oktaysadoglu.gamification.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.oktaysadoglu.gamification.DAO.LastSeenWordDAO;
import com.oktaysadoglu.gamification.DAO.WordDAO;
import com.oktaysadoglu.gamification.R;
import com.oktaysadoglu.gamification.activities.WordPagerActivity;
import com.oktaysadoglu.gamification.model.LastSeenWord;
import com.oktaysadoglu.gamification.model.WordNumber;
import com.oktaysadoglu.gamification.preferences.UserStatus;

/**
 * Created by oktaysadoglu on 12/12/15.
 */
public class PlayLevelsListMainFragment extends Fragment {

    private static final String TAG = "PlayLevelsListMainFragment";

    private RecyclerView mRecyclerView;

    RecyclerView.Adapter mRecyclerViewAdapter;

    private WordDAO mWordDAO;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWordDAO = new WordDAO(getActivity());

        mRecyclerViewAdapter = new PlayLevelsViewAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_play_levels_list, container, false);

        findViewById(view);

        setupRecyclerView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        mRecyclerViewAdapter.notifyDataSetChanged();

        controlOfLevel();

    }

    @Override
    public void onStop() {
        super.onStop();

        new ApplyLastUserStatusValues(getActivity()).execute();

    }

    private void findViewById(View view){

        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_play_levels_recycler_view);

    }

    private void setupRecyclerView(){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.setAdapter(mRecyclerViewAdapter);

    }

    private void controlOfLevel(){

        UserStatus userStatus = UserStatus.getInstance(getActivity());

        int level = userStatus.getLevel();

        int correctAnsweredWordNumber = mWordDAO.getKnownWordInSpecificLevel(level);

        if(correctAnsweredWordNumber > 70){

            userStatus.setLevel(level + 1);

        }

    }

    public static PlayLevelsListMainFragment newInstance(){

        PlayLevelsListMainFragment playLevelsListMainFragment = new PlayLevelsListMainFragment();

        return playLevelsListMainFragment;
    }

    private class PlayLevelsViewHolder extends RecyclerView.ViewHolder{

        private TextView mLevelTitleTextView;

        private ProgressBar correctStatusProgressBar;

        private TextView mCorrectAnsweredPercentage;

        private View view;

        private int userLevel;

        public PlayLevelsViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getActivity(), WordPagerActivity.class);

                    int level = getAdapterPosition() + 1;

                    intent.putExtra("level", level);

                    intent.putExtra("current_item_id", getLastSeenWordId(level));

                    startActivityForResult(intent, 1);

                }
            });

            view = itemView;

            findViewById();

            UserStatus userStatus = UserStatus.getInstance(getActivity());

            userLevel = userStatus.getLevel();

        }

        private void findViewById(){

            mLevelTitleTextView = (TextView) view.findViewById(R.id.fragment_play_levels_list_item_title);

            correctStatusProgressBar = (ProgressBar) view.findViewById(R.id.fragment_play_levels_list_item_progress_bar_correct_number);

            mCorrectAnsweredPercentage = (TextView) view.findViewById(R.id.fragment_play_levels_list_item_correct_percentage);

        }

        public int getLastSeenWordId(int level){

            LastSeenWordDAO lastSeenWordDAO = new LastSeenWordDAO(getActivity());

            LastSeenWord lastSeenWord = lastSeenWordDAO.getLastSeenWord(level);

            return lastSeenWord.getBaseWordId();

        }


        public void createView(int position){

            int level = position + 1 ;

            ApplyLevelValuesTask task = new ApplyLevelValuesTask(getActivity());

            task.execute(level);

        }

        private class ApplyLevelValuesTask extends AsyncTask<Integer,Void,WordNumber>{

            private Context mContext;

            private int mUserLevel;

            public ApplyLevelValuesTask(Context mContext) {

                this.mContext = mContext;

                UserStatus userStatus = UserStatus.getInstance(mContext);

                mUserLevel = userStatus.getLevel();

            }

            @Override
            protected WordNumber doInBackground(Integer... params) {

                mUserLevel = params[0];

                WordNumber wordNumber = mWordDAO.getAnsweredWordNumberInSpecificLevel(mUserLevel);

                return wordNumber;

            }

            @Override
            protected void onPostExecute(WordNumber wordNumber) {

                super.onPostExecute(wordNumber);

                mLevelTitleTextView.setText(String.valueOf("LEVEL " + (mUserLevel)));

                int correctAnsweredRate = 0;

                int answeredRate = 0;

                if(wordNumber.getmAnsweredWordNumber() != 0){

                    correctAnsweredRate = wordNumber.getmCorrectAnsweredWordNumber();

                    answeredRate = wordNumber.getmAnsweredWordNumber();

                }

                String correctPercentage = "% "+correctAnsweredRate;

                correctStatusProgressBar.setProgress(correctAnsweredRate);

                correctStatusProgressBar.setSecondaryProgress(answeredRate);

                mCorrectAnsweredPercentage.setText(correctPercentage);

            }
        }
    }

    private class PlayLevelsViewAdapter extends RecyclerView.Adapter<PlayLevelsViewHolder>{

        @Override
        public PlayLevelsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater = LayoutInflater.from(getActivity());

            View view = inflater.inflate(R.layout.fragment_play_levels_list_item, parent, false);

            return new PlayLevelsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PlayLevelsViewHolder holder, int position) {

            holder.createView(position);

        }

        @Override
        public int getItemCount() {

            UserStatus userStatus = UserStatus.getInstance(getActivity());

            int level = userStatus.getLevel();

            return level;
        }
    }

    //Sayfadan çıkarken Kelime bilgilerinin sharedpreferences'a yazılması
    public class ApplyLastUserStatusValues extends AsyncTask<Void,Void,Void>{

        private Context context;

        private UserStatus userStatus;

        public ApplyLastUserStatusValues(Context context) {

            this.context = context;

            this.userStatus = UserStatus.getInstance(context);

        }

        @Override
        protected Void doInBackground(Void... params) {

            WordDAO wordDAO = new WordDAO(context);

            int seenWords = wordDAO.getSeenWordNumber();

            int knownWords = wordDAO.getKnownWordsNumber();

            userStatus.setSeenWords(seenWords);

            userStatus.setKnownWords(knownWords);

            userStatus.setNotKnownWords(seenWords-knownWords);

            return null;

        }

    }

}
