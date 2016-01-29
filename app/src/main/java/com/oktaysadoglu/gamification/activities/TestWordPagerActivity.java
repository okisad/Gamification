package com.oktaysadoglu.gamification.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.eftimoff.viewpagertransformers.AccordionTransformer;
import com.eftimoff.viewpagertransformers.CubeInTransformer;
import com.eftimoff.viewpagertransformers.DefaultTransformer;
import com.eftimoff.viewpagertransformers.RotateDownTransformer;
import com.eftimoff.viewpagertransformers.ZoomInTransformer;
import com.eftimoff.viewpagertransformers.ZoomOutTranformer;
import com.oktaysadoglu.gamification.R;
import com.oktaysadoglu.gamification.fragments.PlayTestWordFragment;
import com.oktaysadoglu.gamification.interfaces.AddAnsweredWordInterface;
import com.oktaysadoglu.gamification.interfaces.AddCorrectAnsweredWordForTestInterface;
import com.oktaysadoglu.gamification.interfaces.EvaluateTestResult;
import com.oktaysadoglu.gamification.interfaces.PagerSlideInterface;
import com.oktaysadoglu.gamification.interfaces.ShowSnackbarInterface;

import java.lang.reflect.Field;

/**
 * Created by oktaysadoglu on 03/01/16.
 */
public class TestWordPagerActivity extends AppCompatActivity implements AddCorrectAnsweredWordForTestInterface,PagerSlideInterface,EvaluateTestResult,ShowSnackbarInterface{

    private Toolbar mToolbar;

    private ViewPager mViewPager;

    private int mCorrectAnsweredNumber;

    private View snackbarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test_word_pager);

        findViewsById();

        setConstantComponents();


    }

    private void findViewsById(){

        mToolbar = (Toolbar) findViewById(R.id.activity_test_word_pager_toolbar);

        mViewPager = (ViewPager) findViewById(R.id.activity_test_word_view_pager);

        snackbarView = findViewById(R.id.activity_test_word_pager_snackbar);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:

                Intent resultIntent = new Intent();

                setResult(Activity.RESULT_OK, resultIntent);

                finish();

                return true;

        }

        return super.onOptionsItemSelected(item);

    }

    private void setConstantComponents(){

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mToolbar.setTitle("Seviye Belirleme Testi");

        setViewPagerTime();

        FragmentManager fragmentManager = getSupportFragmentManager();

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {

                return PlayTestWordFragment.newInstance(position);

            }

            @Override
            public int getCount() {

                return 50;
            }
        });

        mViewPager.setPageTransformer(true, new DefaultTransformer());

    }

    private void setViewPagerTime(){

        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(mViewPager.getContext(), new DecelerateInterpolator());
            // scroller.setFixedDuration(5000);
            mScroller.set(mViewPager, scroller);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }


    }


    @Override
    public void slidePage(int id) {

        int realPosition = ((id+50)/100);

        Log.e("my",realPosition+"");

        mViewPager.setCurrentItem(realPosition, true);

    }

    @Override
    public int getPageId() {
        return 0;
    }

    @Override
    public int evaluateTest() {

        int level;

        if(mCorrectAnsweredNumber == 0){

            level = 1;

        }else {

            level = mCorrectAnsweredNumber;

        }

        return level;
    }

    @Override
    public void addCorrectAnsweredWord() {

        mCorrectAnsweredNumber++;

    }

    @Override
    public void showSnackbar(boolean bool) {

        /*if (bool){

            Snackbar snackbar = Snackbar.make(snackbarView, "Doğru Cevap", Snackbar.LENGTH_LONG);

            snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.play_word_card_foreground_color_true));

            snackbar.show();

        }else {

            Snackbar snackbar = Snackbar.make(snackbarView, "Yanlış Cevap", Snackbar.LENGTH_LONG);

            snackbar.getView().setBackgroundColor(ContextCompat.getColor(this,R.color.play_word_card_foreground_color_false));

            snackbar.show();

        }*/

    }

    //Sayfa geçiş hızı ayarlayıcısı
    private class FixedSpeedScroller extends Scroller {

        private int mDuration = 300;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }


        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

    }
}
