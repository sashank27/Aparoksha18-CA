package org.aparoksha.app18.ca.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clock.scratch.ScratchView;

import org.aparoksha.app18.ca.activities.ScratchCardsActivity;
import org.aparoksha.app18.ca.models.Points;
import org.aparoksha.app18.ca.R;

import java.util.ArrayList;

/**
 * Created by sashank on 12/11/17.
 */

public class NewCardFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.popup_card,container,false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final int generatedPoint = getRandomValue();

        //setting points using getters and setters, so as to pass data between fragments

        //TODO: Implement a better approach(e.g. using interface) so as to minimize compling

        ((ScratchCardsActivity) getActivity()).setPointsRecieved(generatedPoint);

        //TODO: Proper image according to points generated is required
        final ScratchView mScratchView = view.findViewById(R.id.scratch_view);
        mScratchView.setMaxPercent(40);
        mScratchView.setEraserSize(100.0F);
        mScratchView.setMaskColor(-0xff8c1a);
        mScratchView.setWatermark(-1);

        mScratchView.setEraseStatusListener(new ScratchView.EraseStatusListener() {
            @Override
            public void onProgress(final int percent) {

                view.findViewById(R.id.outer_frame).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(percent > 30 && percent != 100) {
                            mScratchView.clear();

                            if(generatedPoint != 0)
                                ((TextView) view.findViewById(R.id.textView)).
                                    setText("You won " + Integer.toString(generatedPoint) + " Points!!");
                            else
                                ((TextView) view.findViewById(R.id.textView)).
                                        setText("Bad luck. Come back soon !!");
                        }
                    }
                });
            }

            @Override
            public void onCompleted(View view) {

            }
        });

    }

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