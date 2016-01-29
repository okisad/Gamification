package com.oktaysadoglu.gamification.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.oktaysadoglu.gamification.DAO.BaseWordDAO;
import com.oktaysadoglu.gamification.DAO.WordDAO;
import com.oktaysadoglu.gamification.R;
import com.oktaysadoglu.gamification.activities.MainActivity;
import com.oktaysadoglu.gamification.interfaces.AddAnsweredWordInterface;
import com.oktaysadoglu.gamification.interfaces.AddCorrectAnsweredWordForTestInterface;
import com.oktaysadoglu.gamification.interfaces.EvaluateTestResult;
import com.oktaysadoglu.gamification.interfaces.PagerSlideInterface;
import com.oktaysadoglu.gamification.interfaces.ShowSnackbarInterface;
import com.oktaysadoglu.gamification.model.BaseWord;
import com.oktaysadoglu.gamification.preferences.UserStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by oktaysadoglu on 03/01/16.
 */
public class PlayTestWordFragment extends Fragment implements View.OnClickListener{

    TextView mWordBaseWordNameText,mNumberOfWord;

    Button mButton1,mButton2,mButton3,mButton4;

    Button mFinishTestButton;

    List<BaseWord> mBaseWords;

    private int mPosition,correctAnsweredWordNumber;

    private List<BaseWord> mBaseWordsForOptions;

    private BaseWordDAO mBaseWordDAO;

    private BaseWord mBaseWord;

    private WordDAO wordDAO;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null){

            mPosition = getArguments().getInt("position");

        }

        mBaseWordDAO = new BaseWordDAO(getActivity());

        mBaseWords = mBaseWordDAO.getBaseWordsForTest();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_play_test_word,container,false);

        findViewsId(view);

        new PlayTestWordTask(getActivity()).execute(mPosition);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        setButtonListeners();

        setButtonClickable(false);
    }

    private void findViewsId(View view){

        mWordBaseWordNameText = (TextView) view.findViewById(R.id.fragment_user_play_test_word_word_name);

        mNumberOfWord = (TextView) view.findViewById(R.id.fragment_user_play_test_word_number);

        mButton1 = (Button) view.findViewById(R.id.fragment_user_play_test_word_first_button);
        mButton2 = (Button) view.findViewById(R.id.fragment_user_play_test_word_second_button);
        mButton3 = (Button) view.findViewById(R.id.fragment_user_play_test_word_third_button);
        mButton4 = (Button) view.findViewById(R.id.fragment_user_play_test_word_fourth_button);

        mFinishTestButton = (Button) view.findViewById(R.id.fragment_user_play_test_word_finish_test_button);

    }

    private void setButtonListeners(){

        mButton1.setOnClickListener(this);

        mButton2.setOnClickListener(this);

        mButton3.setOnClickListener(this);

        mButton4.setOnClickListener(this);

        mFinishTestButton.setOnClickListener(this);
    }

    public static Fragment newInstance(int position){

        Bundle bundle = new Bundle();

        bundle.putInt("position",position);

        PlayTestWordFragment fragment = new PlayTestWordFragment();

        fragment.setArguments(bundle);

        return fragment;
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

    @Override
    public void onClick(View view) {

        PagerSlideInterface pagerSlideInterface;

        AddCorrectAnsweredWordForTestInterface addCorrectAnsweredWordForTestInterface;

        EvaluateTestResult evaluateTestResult;

        if(view == mButton1 || view==mButton2 || view==mButton3 || view==mButton4){

            if(mBaseWord != null){

                Log.e("my",mBaseWord.toString());

                String mean = mBaseWord.getMean();

                String selectedMean = ((Button) view).getText().toString();

                ShowSnackbarInterface showSnackbarInterface = (ShowSnackbarInterface) getActivity();

                if(!selectedMean.equals("")){

                    pagerSlideInterface = (PagerSlideInterface) getActivity();

                    addCorrectAnsweredWordForTestInterface = (AddCorrectAnsweredWordForTestInterface) getActivity();

                    setButtonClickable(false);

                    if(mean.equals(selectedMean)){

                        addCorrectAnsweredWordForTestInterface.addCorrectAnsweredWord();

                        pagerSlideInterface.slidePage(mBaseWord.getId());

                        showSnackbarInterface.showSnackbar(true);

                    }else {

                        pagerSlideInterface.slidePage(mBaseWord.getId());

                        showSnackbarInterface.showSnackbar(false);

                    }

                }

                if(mBaseWord.getId() == 4950){

                    evaluateTestResult = (EvaluateTestResult) getActivity();

                    int level = evaluateTestResult.evaluateTest();

                    new ApplyTestResultsToDatabase(getActivity()).execute(level);

                }

            }



        }

        if(view == mFinishTestButton ){

            evaluateTestResult = (EvaluateTestResult) getActivity();

            int level = evaluateTestResult.evaluateTest();

            new ApplyTestResultsToDatabase(getActivity()).execute(level);

        }

    }



    public class PlayTestWordTask extends AsyncTask<Integer,Void,Integer>{

        private Context context;

        public PlayTestWordTask(Context context) {
            this.context = context;
        }

        @Override
        protected Integer doInBackground(Integer... params) {

            int position = params[0];

            mBaseWord = mBaseWords.get(position);

            mBaseWordsForOptions = mBaseWordDAO.getWordOptionsForTest(mBaseWord);

            return position;

        }

        @Override
        protected void onPostExecute(Integer position) {

            super.onPostExecute(position);


            setWordCard(position);

        }

        public void setWordCard(int position){

            mWordBaseWordNameText.setText(mBaseWord.getWord());

            mNumberOfWord.setText(String.valueOf(position+1)+"/50");

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

            setButtonClickable(true);

        }
    }

    public class ApplyTestResultsToDatabase extends AsyncTask<Integer,Void,Boolean>{

        private Context mContext;

        private Activity mActivity;

        private ProgressDialog mProgressDialog;

        private int level;

        public ApplyTestResultsToDatabase(Activity context) {

            this.mContext = context;

            this.mActivity = context;

            mProgressDialog = new ProgressDialog(context);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            this.mProgressDialog.setTitle("Seviyeniz Belirleniyor");

            this.mProgressDialog.setMessage("Lütfen Bekleyiniz");

            this.mProgressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Integer... params) {

            try {

                level = params[0];

                Log.e("my", "level : " + level);

                UserStatus userStatus = UserStatus.getInstance(mContext);

                userStatus.setLevel(level);

                Log.e("my", "level belirlendi");

                wordDAO = new WordDAO(mContext);

                Log.e("my", "dao olstu");

                wordDAO.applyKnownCorrectUntilSpecificLevel(level);

                Log.e("my", "uygulandı");

                level = userStatus.getLevel();

                return true;

            }catch (Exception e){

                UserStatus userStatus = UserStatus.getInstance(mContext);

                userStatus.setLevel(1);

                Log.e("my", e.getMessage());

                level = userStatus.getLevel();

                return false;

            }

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if(this.mProgressDialog.isShowing()){

                this.mProgressDialog.dismiss();

            }

            if(aBoolean){

                Toast.makeText(mContext,"Yeni seviyeniz : "+String.valueOf(level),Toast.LENGTH_SHORT).show();

            }else {

                Toast.makeText(mContext,"Hata oluştu ve seviyeniz belirlenemedi",Toast.LENGTH_SHORT).show();

            }

            Intent intent = new Intent(getActivity(), MainActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);

        }
    }
}
