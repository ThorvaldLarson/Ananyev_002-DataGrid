package com.training.task.myapplication;

import android.widget.GridLayout;

public class SpecCreatorBeforeApi21 implements ISpecCreator{
    @Override
    public GridLayout.Spec getSpec(int position) {
        return GridLayout.spec(position);
    }
}