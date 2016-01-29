package com.oktaysadoglu.gamification.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.oktaysadoglu.gamification.DAO.BaseWordDAO;
import com.oktaysadoglu.gamification.DAO.WordDAO;
import com.oktaysadoglu.gamification.R;
import com.oktaysadoglu.gamification.interfaces.PagerSlideInterface;
import com.oktaysadoglu.gamification.interfaces.ShowSnackbarInterface;
import com.oktaysadoglu.gamification.model.BaseWord;
import com.oktaysadoglu.gamification.model.Word;
import com.oktaysadoglu.gamification.preferences.UserStatus;


import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by oktaysadoglu on 11/12/15.
 */
public class PlayWordFragment extends Fragment implements View.OnClickListener,DiscreteSeekBar.OnProgressChangeListener{

    private BaseWordDAO mBaseWordDAO;

    private WordDAO mWordDAO;

    private int mLevel,mUserLevel,mWordPosition;

    private BaseWord mBaseWord;

    private TextView mWordNameTextView,mTitleWhichWordId;

    private DiscreteSeekBar mDiscreteSeekBar;

    private Button mButton1,mButton2,mButton3,mButton4;

    private List<BaseWord> mAllBaseWordsForLevel;

    private List<BaseWord> mBaseWordsForOptions;

    private UserStatus mUserStatus;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        onCreateNewObejcts();

        assignInitialValues();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_play_word,container,false);

        findViewsById(view);

        setWhichWordText();

        createWordCardView();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        setButtonListeners();

        setDiscreteSeekbarListener();
    }

    private void onCreateNewObejcts(){

        mBaseWordDAO = new BaseWordDAO(getActivity());

        mWordDAO = new WordDAO(getActivity());

        mUserStatus = UserStatus.getInstance(getActivity());

    }

    private void assignInitialValues(){

        if(getArguments() != null){

            mLevel = getArguments().getInt("level");

            mWordPosition = getArguments().getInt("word_position");

        }

        mUserLevel = mUserStatus.getLevel();

    }

    private void createWordCardView(){

        new setWordCardTask(getActivity()).execute(mLevel, mWordPosition);

    }

    private void setButtonClickable(boolean isClickable){

        if(isClickable){

            mButton1.setEnabled(true);
            mButton2.setEnabled(true);
            mButton3.setEnabled(true);
            mButton4.setEnabled(true);

        }else {

            mButton1.setEnabled(false);
            mButton2.setEnabled(false);
            mButton3.setEnabled(false);
            mButton4.setEnabled(false);

        }

    }

    private void setWhichWordText(){

        String requiredText = (mWordPosition+1) + "/100";

        mTitleWhichWordId.setText(requiredText);

    }

    private void findViewsById(View view){

        mDiscreteSeekBar = (DiscreteSeekBar) view.findViewById(R.id.fragment_play_word_discrete_seek_bar);

        mWordNameTextView = (TextView) view.findViewById(R.id.fragment_play_word_word_name);

        mTitleWhichWordId = (TextView) view.findViewById(R.id.fragment_play_word_which_word);

        mButton1 = (Button) view.findViewById(R.id.fragment_play_word_first_button);

        mButton2 = (Button) view.findViewById(R.id.fragment_play_word_second_button);

        mButton3 = (Button) view.findViewById(R.id.fragment_play_word_third_button);

        mButton4 = (Button) view.findViewById(R.id.fragment_play_word_fourth_button);

    }

    private void setButtonListeners(){

        mButton1.setOnClickListener(this);

        mButton2.setOnClickListener(this);

        mButton3.setOnClickListener(this);

        mButton4.setOnClickListener(this);
    }

    private void setDiscreteSeekbarListener(){

        mDiscreteSeekBar.setOnProgressChangeListener(this);

    }


    @Override
    public void onClick(View view) {

        if(view == mButton1 || view==mButton2 || view==mButton3 || view==mButton4){

            setButtonClickable(false);

            ShowSnackbarInterface showSnackbar = (ShowSnackbarInterface) getActivity();

            if(mBaseWord != null){

                String mean = mBaseWord.getMean();

                String selectedMean = ((Button) view).getText().toString();

                if(!selectedMean.equals("")){

                    PagerSlideInterface pagerSlideInterface = (PagerSlideInterface) getActivity();

                    if(mean.equals(selectedMean)){

                        new AddWordCardDBTask(getActivity()).execute(0);

                        Log.e("my","show snack");

                        showSnackbar.showSnackbar(true);

                        pagerSlideInterface.slidePage(mBaseWord.getId());

                    }else {

                        new AddWordCardDBTask(getActivity()).execute(1);
                        Log.e("my", "show snack");

                        showSnackbar.showSnackbar(false);

                        pagerSlideInterface.slidePage(mBaseWord.getId());

                    }


                }

            }

        }

        int number= mWordDAO.getKnownWordsNumber();

        Log.e("my", String.valueOf(number));

    }



    public static Fragment newInstance(int word_position,int level){

        Bundle bundle = new Bundle();

        bundle.putInt("word_position", word_position);

        bundle.putInt("level", level);

        PlayWordFragment fragment = new PlayWordFragment();

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {



    }

    @Override
    public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

        PagerSlideInterface pagerSlideInterface = (PagerSlideInterface) getActivity();

        pagerSlideInterface.slidePage( (seekBar.getProgress()*10));


    }


    private class setWordCardTask extends AsyncTask<Integer,Void,Word>{

        private Context mContext;

        public setWordCardTask(Context context) {

            mContext = context;

        }


        @Override//param[0] = level, param[1] = position
        protected Word doInBackground(Integer... params) {

            int level = params[0];

            int position = params[1];

            int id = (position+1) + (level*100) - 100;

            mBaseWord = mBaseWordDAO.getSpecificBaseWord(id);

            //4 base kelimeden oluşan listenin yaratılması içinde hedef kelimede bulunacak şekilde
            mBaseWordsForOptions = mBaseWordDAO.getWordOptionsForTest(mBaseWord);

            //Gerekli word' un user.db içerisinde word tablosundan baseword id sine göre çekilmesi
            Word word = mWordDAO.getSpecificWordFromBaseWordId(mBaseWord.getId());

            return word;

        }

        @Override
        protected void onPostExecute(Word word) {
            super.onPostExecute(word);

            setWordCard(word);

        }

        private void setWordCard(Word word){

            //Card'a kelime adı yazılması
            mWordNameTextView.setText(mBaseWord.getWord());

            List<Integer> sequenceOptions = new ArrayList<>();

            for(int i = 0;i<4;i++){

                sequenceOptions.add(i);

            }

            //toplanan kelimeler için numaraların karıştırılma işlemi
            Collections.shuffle(sequenceOptions);

            mButton1.setText(mBaseWordsForOptions.get(sequenceOptions.get(0)).getMean());

            mButton2.setText(mBaseWordsForOptions.get(sequenceOptions.get(1)).getMean());

            mButton3.setText(mBaseWordsForOptions.get(sequenceOptions.get(2)).getMean());

            mButton4.setText(mBaseWordsForOptions.get(sequenceOptions.get(3)).getMean());

            PagerSlideInterface pagerSlideInterface = (PagerSlideInterface) getActivity();

            mDiscreteSeekBar.setProgress(pagerSlideInterface.getPageId());

            setButtonClickable(true);

        }

    }

    private class AddWordCardDBTask extends AsyncTask<Integer,Void,Word>{

        private Context mContext;

        public AddWordCardDBTask(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected Word doInBackground(Integer... params) {

            Word word = mWordDAO.getSpecificWordFromBaseWordId(mBaseWord.getId());

            if(word != null){

                if(params[0] == 0){

                    // correct answer

                    if (word.getNumberOfNotKnow() == -1){

                        word.setNumberOfNotKnow(0);

                    }else if (word.getNumberOfNotKnow() == -2){

                        word.setNumberOfNotKnow(-2);

                    }else if (word.getNumberOfNotKnow() > 0) {

                        word.setNumberOfNotKnow(word.getNumberOfNotKnow() - 1);

                    }

                    word.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

                    mWordDAO.updateWord(word);

                }else if(params[0] == 1){

                    //wrong answer

                    if(word.getNumberOfNotKnow() == -1){

                        word.setNumberOfNotKnow(2);

                    }else if(word.getNumberOfNotKnow() == -2){

                        word.setNumberOfNotKnow(-2);

                    }else {

                        word.setNumberOfNotKnow(word.getNumberOfNotKnow() + 1);

                    }

                    word.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

                    mWordDAO.updateWord(word);

                }

            }

            return word;
        }


        @Override
        protected void onPostExecute(Word word) {

            super.onPostExecute(word);

            Log.e("my","butonlar "+word.getBaseWordId()+" için açılıyor");

            setButtonClickable(true);

        }
    }
}
