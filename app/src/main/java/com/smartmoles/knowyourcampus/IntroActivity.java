package com.smartmoles.knowyourcampus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

public class IntroActivity extends AppIntro2 {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SliderPage sliderPage1 = new SliderPage();
        sliderPage1.setTitle("Welcome!");
        sliderPage1.setDescription("This is " + getString(R.string.app_name));
        sliderPage1.setImageDrawable(R.drawable.ic_launcher_web);
        sliderPage1.setBgColor(getResources().getColor(R.color.card_bg));
        addSlide(AppIntroFragment.newInstance(sliderPage1));

        SliderPage sliderPage2 = new SliderPage();
        sliderPage2.setTitle("Stay Updated");
        sliderPage2.setDescription("Updated");
        sliderPage2.setImageDrawable(R.drawable.question_mark);
        sliderPage2.setBgColor(getResources().getColor(R.color.card_bg));
        addSlide(AppIntroFragment.newInstance(sliderPage2));

        SliderPage sliderPage3 = new SliderPage();
        sliderPage3.setTitle("Give Feedbacks");
        sliderPage3.setDescription("FeedBacks");
        sliderPage3.setImageDrawable(R.drawable.feedback);
        sliderPage3.setBgColor(getResources().getColor(R.color.card_bg));
        addSlide(AppIntroFragment.newInstance(sliderPage3));

        SliderPage sliderPage4 = new SliderPage();
        sliderPage4.setTitle("Explore");
        sliderPage4.setDescription("Explore Stuffs");
        sliderPage4.setImageDrawable(R.drawable.search);
        sliderPage4.setBgColor(getResources().getColor(R.color.card_bg));
        addSlide(AppIntroFragment.newInstance(sliderPage4));

        setFadeAnimation();
        showSkipButton(false);
        showStatusBar(false);
        setBarColor(getResources().getColor(R.color.card_bg));
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Prefs.with(this).writeBoolean("firstboot",true);
        startActivity(new Intent(this, SelectionActivity.class));
        finish();
    }
}
