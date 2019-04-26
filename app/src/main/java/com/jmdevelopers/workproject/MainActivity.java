package com.jmdevelopers.workproject;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jmdevelopers.workproject.Fragments.DoarFragment;
import com.jmdevelopers.workproject.Fragments.MinhasDoacoesFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private SmartTabLayout  smartTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.viewpager);
        smartTabLayout = findViewById(R.id.viewpagertab);
        getSupportActionBar().setElevation(0);
        // configurar action bar

        getSupportActionBar().setTitle("Doações");
        FragmentPagerItemAdapter adapter=new FragmentPagerItemAdapter(getSupportFragmentManager(),
                FragmentPagerItems.with(this).add("Doar", DoarFragment.class).add("Minhas Doações", MinhasDoacoesFragment.class)
                        .create());
        viewPager.setAdapter(adapter);
        smartTabLayout.setViewPager(viewPager);
    }
}
