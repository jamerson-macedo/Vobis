package com.br.vobis.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.br.vobis.R;
import com.br.vobis.model.Category;
import com.br.vobis.model.Expandable;
import com.br.vobis.viewholder.CategoryViewHolder;
import com.br.vobis.viewholder.SubcategotyView;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class CategoryAdapter extends ExpandableRecyclerViewAdapter<CategoryViewHolder, SubcategotyView> {
    public CategoryAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public CategoryViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_recyclerview, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public SubcategotyView onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_child, parent, false);
        return new SubcategotyView(view);
    }

    @Override
    public void onBindChildViewHolder(SubcategotyView holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final Category category = (Category) group.getItems().get(childIndex);
        holder.bind(category);
    }

    @Override
    public void onBindGroupViewHolder(CategoryViewHolder holder, int flatPosition, ExpandableGroup group) {
        final Expandable expandable = (Expandable) group;
        holder.bind(expandable);
    }
}
