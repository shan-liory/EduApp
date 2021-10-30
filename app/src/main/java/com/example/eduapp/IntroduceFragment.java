package com.example.eduapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;


public class IntroduceFragment extends Fragment {
    private Button start;
    private ImageButton back,next;
    private ViewPager2 viewPager2;
    private TextView title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return (ViewGroup) inflater.inflate(
                R.layout.fragment_introduce, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = view.findViewById(R.id.introH);
        back = view.findViewById(R.id.back);
        back.setVisibility(View.INVISIBLE);
        next = view.findViewById(R.id.next);
        start = view.findViewById(R.id.startBtn);
        start.setVisibility(View.INVISIBLE);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.action_introduceLesson_to_welcomeActivity);
            }
        });
        // find views by id
        ViewPager viewPager = view.findViewById(R.id.pager);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);

        // attach tablayout with viewpager
        tabLayout.setupWithViewPager(viewPager);


        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        // add your fragments
        adapter.addFrag(new IntroduceLesson());
        adapter.addFrag(new IntroduceQuiz());
        adapter.addFrag(new IntroduceRewards());
        adapter.addFrag(new IntroduceCompetition());

        // set adapter on viewpager
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setIcon(R.drawable.ic_baseline_circle_24);
        }
        System.out.println(adapter.getCount());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 3){
                    start.setVisibility(View.VISIBLE);
                    next.setVisibility(View.INVISIBLE);
                    back.setVisibility(View.VISIBLE);
                }
                else{
                    if(position == 0){
                        back.setVisibility(View.INVISIBLE);
                    }
                    else
                        back.setVisibility(View.VISIBLE);
                    next.setVisibility(View.VISIBLE);
                    start.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public void startBtn(){

    }
}







