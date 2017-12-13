package org.aparoksha.app18.ca.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.clock.scratch.ScratchView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.aparoksha.app18.ca.activities.ScratchCardsActivity;
import org.aparoksha.app18.ca.models.Points;
import org.aparoksha.app18.ca.R;

import java.util.ArrayList;

/**
 * Created by sashank on 12/11/17.
 */

public class NewCardFragment extends Fragment {
    @Nullable

    ScratchView mScratchView;
    LottieAnimationView animationView;
    FrameLayout cardFrame;
    private int points;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.popup_card,container,false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mScratchView = view.findViewById(R.id.scratch_view);
        animationView = view.findViewById(R.id.animationView);
        cardFrame = view.findViewById(R.id.card_frame);
        points = Integer.parseInt(this.getArguments().get("points").toString());

        mScratchView.setMaxPercent(40);
        mScratchView.setEraserSize(100.0F);
        mScratchView.setMaskColor(R.color.fui_linkColor);
        mScratchView.setWatermark(R.drawable.ic_demo_user);

        mScratchView.setEraseStatusListener(new ScratchView.EraseStatusListener() {
            @Override
            public void onProgress(final int percent) {
                if(percent > 40 && percent != 100) {
                    mScratchView.clear();
                    showResultScratch(points);

                    if(points != 0)
                        ((TextView) view.findViewById(R.id.textView)).
                                setText("You won " + Integer.toString(points) + " Points!!");
                    else
                        ((TextView) view.findViewById(R.id.textView)).
                                setText("Bad luck. Come back soon !!");
                }
            }

            @Override
            public void onCompleted(View view) {

            }
        });

    }

    private void showResultScratch(int generatedPoint){
        cardFrame.setVisibility(View.GONE);
        animationView.setVisibility(View.VISIBLE);

        if(generatedPoint != 0)
            animationView.setAnimation("emoji_wink.json");
        else
            animationView.setAnimation("emoji_shock.json");

        animationView.playAnimation();
        animationView.loop(true);
    }

    //TODO: Deploy this function to cloud
    /*  Priorities:
        0 -> 50%, 5 -> 20%, 10 -> 10%, 20 -> 5%, 30 -> 5%, 40 -> 5%, 50 -> 3%, 100 -> 2%
        */
    private int getRandomValue(){

        ArrayList<Points> pointsList = new ArrayList<>();
        pointsList.add(new Points(0,0.50));
        pointsList.add(new Points(5,0.20));
        pointsList.add(new Points(10,0.10));
        pointsList.add(new Points(20,0.05));
        pointsList.add(new Points(30,0.05));
        pointsList.add(new Points(40,0.05));
        pointsList.add(new Points(50,0.03));
        pointsList.add(new Points(100,0.02));


        //  Uses priority randomization.
        // TODO: If possible, a better algorithm must be implemented

        double completeWeight = 0.0;
        for (Points item : pointsList)
            completeWeight += item.getPriority();
        double r = Math.random() * completeWeight;
        double countWeight = 0.0;
        for (Points item : pointsList) {
            countWeight += item.getPriority();
            if (countWeight >= r)
                return item.getNumber();
        }
        return 0;
    }

}