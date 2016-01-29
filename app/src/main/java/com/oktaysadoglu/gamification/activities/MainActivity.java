package com.oktaysadoglu.gamification.activities;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.oktaysadoglu.gamification.R;
import com.oktaysadoglu.gamification.databaseHelper.DictionaryHelper;
import com.oktaysadoglu.gamification.databaseHelper.UserHelper;
import com.oktaysadoglu.gamification.fragments.PlayLevelsListMainFragment;
import com.oktaysadoglu.gamification.fragments.UserInformationMainFragment;
import com.oktaysadoglu.gamification.fragments.UserMainFragment;
import com.oktaysadoglu.gamification.fragments.UserSettingsMainFragment;
import com.oktaysadoglu.gamification.fragments.UserStartTestMainFragment;
import com.oktaysadoglu.gamification.interfaces.CustomizeToolbarInteface;
import com.oktaysadoglu.gamification.services.DayStartService;
import com.oktaysadoglu.gamification.services.NotificationService;
import com.oktaysadoglu.gamification.tools.DividerItemDecoration;


import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oktaysadoglu on 11/12/15.
 */
public class MainActivity extends AppCompatActivity implements CustomizeToolbarInteface {

    private RecyclerView mDrawerRecyclerView;

    private DrawerLayout mDrawerLayout;

    private Toolbar mToolbar;

    private Toast mInformExitApplicationToast;

    private long mExitApplicationlastBackPressTime = 0;

    private List<String> mDrawerItemNames = new ArrayList<>();

    private NavigationListAdapter mDrawerItemNamesAdapter;

    private Typeface mDrawerItemNamesTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Fabric.with(this, new Crashlytics());*/

        setContentView(R.layout.activity_main);

        DictionaryHelper.getInstance(this);

        UserHelper.getInstance(this);

        findViewById();

        setComponents();

        startAlarmServices();

    }

    @Override
    protected void onStart() {
        super.onStart();

        setFirstFragment();

    }

    //Uygulamadan çıkmak için geri tuşuna iki kere tıklanması
    @Override
    public void onBackPressed() {

        if (this.mExitApplicationlastBackPressTime < System.currentTimeMillis() - 3500 ){

            mInformExitApplicationToast = Toast.makeText(this,"Uygulamayı kapatmak için tekrar geri tuşuna basınız",Toast.LENGTH_LONG);

            mInformExitApplicationToast.show();

            this.mExitApplicationlastBackPressTime = System.currentTimeMillis();

        }else {

            if(mInformExitApplicationToast != null){

                mInformExitApplicationToast.cancel();

            }

            super.onBackPressed();

        }
    }

    private void startAlarmServices(){

        NotificationService notificationService = new NotificationService();

        DayStartService dayStartService = new DayStartService();

        notificationService.setService(this, true);

        dayStartService.startService(this, true, 10, 0);

    }

    private void setComponents(){

        setDrawerLayout();

    }

    /*public void forceCrash(View view) {
        throw new RuntimeException("This is a crash");
    }*/


    private void setDrawerLayout(){

        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle mActionBarDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.drawer_open,R.string.drawer_close);

        mActionBarDrawerToggle.syncState();

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        fillDrawerItemNames();

        mDrawerItemNamesTypeface = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");

        mDrawerRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDrawerItemNamesAdapter = new NavigationListAdapter(this, mDrawerItemNames);

        mDrawerRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mDrawerRecyclerView.setAdapter(mDrawerItemNamesAdapter);

    }

    private void findViewById(){

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mDrawerRecyclerView = (RecyclerView) findViewById(R.id.navList);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_view);

    }

    private void fillDrawerItemNames(){

        mDrawerItemNames.add("Oyna");

        mDrawerItemNames.add("Profil");

        mDrawerItemNames.add("Menü");

        mDrawerItemNames.add("Test");

        mDrawerItemNames.add("Ayarlar");

    }
    private void setFirstFragment(){

        Fragment fragment = UserMainFragment.newInstance();

        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment f = fragmentManager.findFragmentById(R.id.activity_fragment_container);

        if (f == null){

            fragmentManager.beginTransaction().add(R.id.activity_fragment_container,fragment).commit();

        }

    }
    private void setFragment(Fragment fragment){

        Fragment f = fragment;

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.activity_fragment_container,f).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();

    }
    @Override
    public void changeToolbarTitle(String title) {

        mToolbar.setTitle(title);

    }

    public class NavigationListViewHolder extends RecyclerView.ViewHolder{

        private View view;

        public NavigationListViewHolder(View itemView) {
            super(itemView);

            view = itemView;

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(getAdapterPosition()==0){

                        //Oyna

                        mToolbar.setTitle("Oyna");

                        setFragment(PlayLevelsListMainFragment.newInstance());

                    }else if(getAdapterPosition()==1){

                        //Profil

                        mToolbar.setTitle("Profil");

                        setFragment(UserInformationMainFragment.newInstance());

                    }else if (getAdapterPosition() == 2){

                        //Menü

                        mToolbar.setTitle("Menü");

                        setFragment(UserMainFragment.newInstance());

                    } else if(getAdapterPosition()==3){

                        //Test

                        mToolbar.setTitle("Seviye Belirleme Testi");

                        setFragment(UserStartTestMainFragment.newInstance());

                    }else if(getAdapterPosition()==4){

                        //Ayarlar

                        mToolbar.setTitle("Ayarlar");

                        setFragment(UserSettingsMainFragment.newInstance());

                    }

                    mDrawerLayout.closeDrawers();

                }
            });
        }

        public void createView(String text){

            TextView textView = (TextView) view.findViewById(R.id.activity_main_navigation_list_item_text_view);

            ImageView imageView = (ImageView) view.findViewById(R.id.activity_main_navigation_list_item_image_view);

            int order = getAdapterPosition();

            if (order == 0){

                imageView.setImageResource(R.drawable.ic_play);

            }else if (order == 1){

                imageView.setImageResource(R.drawable.ic_profile);

            }else if( order == 2){

                imageView.setImageResource(R.drawable.ic_menu);

            }else if (order == 3){

                imageView.setImageResource(R.drawable.ic_test);

            }else if (order == 4){

                imageView.setImageResource(R.drawable.ic_settings);

            }

            imageView.setColorFilter(UserSettingsMainFragment.getColorFilter("#f0ad4e"));

            textView.setTypeface(mDrawerItemNamesTypeface);

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

            View view = layoutInflater.inflate(R.layout.activity_main_navigation_list_item,parent,false);

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

}
