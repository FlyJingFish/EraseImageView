package com.flyjingfish.searchanimview;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    TabMode[] tabModes = new TabMode[]{
            new TabMode(R.layout.fragment_demo1, "动画模式",false),
            new TabMode(R.layout.fragment_demo2, "手动模式",true),
            new TabMode(R.layout.fragment_demo3, "刮刮乐",true)
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);

        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return getFragment(position);
            }

            @Override
            public int getItemCount() {
                return tabModes.length;
            }
        });
        viewPager.setOffscreenPageLimit(tabModes.length-1);

        TabLayoutMediator  mediator = new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(tabModes[position].title);
            }
        });
        mediator.attach();
        viewPager.setUserInputEnabled(false);
    }

    private Fragment getFragment(int position){
        switch (position){
            case 1:
                return new DemoFragment2();
            case 2:
                return new DemoFragment3();
            default:
                return new DemoFragment1();
        }
    }

    protected static class TabMode {
        int layoutRes;
        String title;

        boolean handMode;

        public TabMode(int layoutRes, String title, boolean handMode) {
            this.layoutRes = layoutRes;
            this.title = title;
            this.handMode = handMode;
        }
    }
}
