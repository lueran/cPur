package com.cpur;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cpur.fragment.StoryListFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends BaseActivity {
        private static final String TAG = "MainActivity";

        private FragmentPagerAdapter mPagerAdapter;
        private ViewPager mViewPager;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

           FirebaseInstanceId.getInstance().getInstanceId()
                   .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                       @Override
                       public void onComplete(@NonNull Task<InstanceIdResult> task) {
                           if (!task.isSuccessful()) {
                               Log.w(TAG, "getInstanceId failed", task.getException());
                               return;
                           }

                           // Get new Instance ID token
                           String token = task.getResult().getToken();
                       }
                   });

            // Create the adapter that will return a fragment for each section
            mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
                private final Fragment[] mFragments = new Fragment[] {
                        StoryListFragment.newInstance(0),
                        StoryListFragment.newInstance(1)
                };
                private final String[] mFragmentNames = new String[] {
                        getString(R.string.discover),
                        getString(R.string.heading_my_stories),
                };
                @Override
                public Fragment getItem(int position) {
                    return mFragments[position];
                }
                @Override
                public int getCount() {
                    return mFragments.length;
                }
                @Override
                public CharSequence getPageTitle(int position) {
                    return mFragmentNames[position];
                }
            };
            // Set up the ViewPager with the sections adapter.
            mViewPager = findViewById(R.id.container);
            mViewPager.setAdapter(mPagerAdapter);
            TabLayout tabLayout = findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);

        // Button launches NewStoryActivity
        findViewById(R.id.fab_new_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateStoryActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
