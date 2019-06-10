package com.br.vobis.viewholder;

import android.view.View;
import android.widget.TextView;

import com.br.vobis.R;
import com.br.vobis.model.Category;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class SubcategotyView extends ChildViewHolder {
    private TextView mtextview;

    public SubcategotyView(View itemView) {
        super(itemView);
        mtextview = itemView.findViewById(R.id.expandable_child);
    }

    public void bind(Category category) {

        mtextview.setText(category.name);

    }
}
