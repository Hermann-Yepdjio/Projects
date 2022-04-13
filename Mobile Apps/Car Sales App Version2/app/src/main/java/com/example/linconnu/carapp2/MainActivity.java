package com.example.linconnu.carapp2;
/*
 *Member#1: Hermann Yepdjio
 * SID: 40917845
 * Member#2: Pushkin Feleke
 * SID: ??
 */

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
{
    TabLayout MyTabs;
    ViewPager MyPage;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        MyTabs = (TabLayout) findViewById(R.id.MyTabs);
        MyPage = (ViewPager) findViewById(R.id.MyPage);
        MyTabs.setupWithViewPager(MyPage);
        SetUpViewPager(MyPage);
        MyPage.setOffscreenPageLimit(3);
    }
    public void SetUpViewPager(ViewPager viewPage)
    {
        MyViewPageAdapter Adapter   = new MyViewPageAdapter(getSupportFragmentManager());
        Adapter.AddFragmentPage(new Page_1(), "SELL");
        Adapter.AddFragmentPage(new Page_2(), "BUY");
        Adapter.AddFragmentPage(new Page_3(), "DISPLAY");
        ViewPager viewPager = (ViewPager)findViewById(R.id.MyPage);
        viewPage.setAdapter(Adapter);
    }
    public class MyViewPageAdapter extends FragmentPagerAdapter
    {
        private List<Fragment> MyFragment = new ArrayList<>();
        private List<String> MyPageTitle = new ArrayList<>();

        public MyViewPageAdapter(android.support.v4.app.FragmentManager manager)
        {
            super(manager);
        }
        public void AddFragmentPage(Fragment Frag, String Title)
        {
            MyFragment.add(Frag);
            MyPageTitle.add(Title);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return MyPageTitle.get(position);
        }

        @Override
        public int getCount()
        {
            return 3;
        }

        @Override
        public int getItemPosition(Object object)
        {
            return super.getItemPosition(object);
        }

        @Override
        public long getItemId(int position)
        {
            return super.getItemId(position);
        }



        @Override
        public Fragment getItem(int position)
        {
            return MyFragment.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


