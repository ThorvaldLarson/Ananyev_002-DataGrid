package com.training.task.myapplication;

import android.annotation.TargetApi;
import android.widget.GridLayout;

public class SpecCreator implements ISpecCreator{
    @TargetApi(21)
    @Override
    public GridLayout.Spec getSpec(int position) {
        return GridLayout.spec(position, 1f);
    }
}