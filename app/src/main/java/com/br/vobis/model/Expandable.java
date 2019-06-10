package com.br.vobis.model;

import android.os.Parcel;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class Expandable extends ExpandableGroup<Category> {


    public Expandable(String title, List<Category> items) {
        super(title, items);
    }

    protected Expandable(Parcel in) {
        super(in);
    }
}
