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
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.eftimoff.viewpagertransformers.DefaultTransformer;
import com.oktaysadoglu.gamification.DAO.LastSeenWordDAO;
import com.oktaysadoglu.gamification.R;
import com.oktaysadoglu.gamification.fragments.PlayLevelsListMainFragment;
import com.oktaysadoglu.gamification.fragments.PlayWordFragment;
import com.oktaysadoglu.gamification.fragments.UserInformationMainFragment;
import com.oktaysadoglu.gamification.fragments.UserMainFragment;
import com.oktaysadoglu.gamification.fragments.UserSettingsMainFragment;
import com.oktaysadoglu.gamification.fragments.UserStartTestMainFragment;
import com.oktaysadoglu.gamification.interfaces.PagerSlideInterface;
import com.oktaysadoglu.gamification.interfaces.ShowSnackbarInterface;
import com.oktaysadoglu.gamification.model.LastSeenWord;
import com.oktaysadoglu.gamification.tools.DividerItemDecoration;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by oktaysadoglu on 11/12/15.
 */
public class WordPagerActivity extends AppCompatActivity implements PagerSlideInterface,ShowSnackbarInterface{

    private LastSeenWordDAO lastSeenWordDAO;

    private Toolbar mToolbar;

    private RecyclerView mRecyclerViewForSearch;

    private DrawerLayout mDrawerLayout;

    private List<String> mDrawerItemNames = new ArrayList<>();

    private NavigationListAdapter mDrawerItemNamesAdapter;

    private ViewPager mViewPager;

    private View mSnackbarView;

    private int mLevel;

    private int mLastItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_word_pager);

        findViewById();

        onCreateNewObjects();

        assignInitialValues();

        setConstantComponents();

        customizeScreen();

        setCurrentWord(mLastItemId);

        setToolbarAsSupport();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:

                Intent resultIntent = new Intent();

                setResult(Activity.RESULT_OK, resultIntent);

                finish();

                return true;

            case R.id.toolbar_menu_action_search:

                if(mDrawerLayout.isDrawerOpen(findViewById(R.id.activity_word_pager_nav_list))){

                    mDrawerLayout.closeDrawers();

                }else {

                    mDrawerLayout.openDrawer(Gravity.RIGHT);

                }
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        return true;
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        saveLastSeenWord();

    }

    private void customizeScreen(){

        mToolbar.setTitle("Oyun");

    }

    private void fillDrawerItemNames(){

        mDrawerItemNames.add("0-10");
        mDrawerItemNames.add("10-20");
        mDrawerItemNames.add("20-30");
        mDrawerItemNames.add("30-40");
        mDrawerItemNames.add("40-50");
        mDrawerItemNames.add("50-60");
        mDrawerItemNames.add("60-70");
        mDrawerItemNames.add("70-80");
        mDrawerItemNames.add("80-90");
        mDrawerItemNames.add("90-100");

    }

    private void findViewById(){

        mToolbar = (Toolbar) findViewById(R.id.toolbar_pager);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_word_pager_drawer_view);

        mRecyclerViewForSearch = (RecyclerView) findViewById(R.id.activity_word_pager_nav_list);

        mViewPager = (ViewPager) findViewById(R.id.activity_word_pager_view_pager);

        mSnackbarView = findViewById(R.id.snackbarPosition);

    }


    private void onCreateNewObjects(){

        lastSeenWordDAO = new LastSeenWordDAO(this);

    }

    private void assignInitialValues() {

        mLevel = getIntent().getExtras().getInt("level");

        mLastItemId = lastSeenWordDAO.getLastSeenWord(mLevel).getBaseWordId();

        fillDrawerItemNames();

        setDrawerLayout();

    }

    private void setDrawerLayout() {

        mRecyclerViewForSearch.setLayoutManager(new LinearLayoutManager(this));

        mDrawerItemNamesAdapter = new NavigationListAdapter(this, mDrawerItemNames);

        mRecyclerViewForSearch.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mRecyclerViewForSearch.setAdapter(mDrawerItemNamesAdapter);

    }

    private void setToolbarAsSupport(){

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    private void saveLastSeenWord(){

        LastSeenWordDAO lastSeenWordDAO = new LastSeenWordDAO(this);

        LastSeenWord lastSeenWord = lastSeenWordDAO.getLastSeenWord(mLevel);

        lastSeenWord.setBaseWordId(mViewPager.getCurrentItem() + 1);

        lastSeenWordDAO.updateBaseWordId(lastSeenWord);

    }

    public void showSnackbar(boolean bool){

        if (bool){

            Snackbar snackbar = Snackbar.make(mSnackbarView, "Doğru Cevap", Snackbar.LENGTH_LONG);

            snackbar.getView().setBackgroundColor(ContextCompat.getColor(this,R.color.play_word_card_foreground_color_true));

            snackbar.show();

        }else {

            Snackbar snackbar = Snackbar.make(mSnackbarView, "Yanlış Cevap", Snackbar.LENGTH_LONG);

            snackbar.getView().setBackgroundColor(ContextCompat.getColor(this,R.color.play_word_card_foreground_color_false));

            snackbar.show();

        }



    }

    private void setConstantComponents(){

        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        /*mToolbar.setNavigationIcon(R.drawable.ic_back_button);*/

        setViewPagerTime();

        FragmentManager fragmentManager = getSupportFragmentManager();

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {

                return PlayWordFragment.newInstance(position, mLevel);

            }

            @Override
            public int getCount() {

                if(mLevel == 50){

                    return 98;

                }else {

                    return 100;

                }
            }
        });

        mViewPager.setPageTransformer(true, new DefaultTransformer());

    }

    private void setCurrentWord(int currentItemId) {

        mViewPager.setCurrentItem(currentItemId - 1, true);

    }

    @Override
    public void slidePage(int id) {

        int realPosition = (id % 100);

        mViewPager.setCurrentItem(realPosition);

    }

    @Override
    public int getPageId() {

        return (mViewPager.getCurrentItem()+1) / 10;

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

    public class NavigationListViewHolder extends RecyclerView.ViewHolder{

        private View view;

        public NavigationListViewHolder(View itemView) {
            super(itemView);

            view = itemView;

        }

        public void createView(String text){

            TextView textView = (TextView) view;

            textView.setText(text);

        }
    }

    public class NavigationListAdapter extends RecyclerView.Adapter<NavigationListViewHolder>{

        private Context context;

        private List<String> navigationList;

        public NavigationListAdapter(Context context,List<String> navigationList) {

            this.context = context;

            this.navigationList = navigationList;

        }

        @Override
        public NavigationListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(context);

            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1,parent,false);

            return new NavigationListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(NavigationListViewHolder holder, int position) {

            String text = navigationList.get(position);

            holder.createView(text);

        }

        @Override
        public int getItemCount() {
            return navigationList.size();
        }
    }

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
