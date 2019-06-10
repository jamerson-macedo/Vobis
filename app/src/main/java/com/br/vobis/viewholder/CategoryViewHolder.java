package com.br.vobis.viewholder;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.vobis.R;
import com.br.vobis.model.Expandable;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class CategoryViewHolder extends GroupViewHolder {
    private ImageView imageView;
    private TextView textView;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.expandable);
        imageView = itemView.findViewById(R.id.arrow);
    }

    public void bind(Expandable expandable) {
        textView.setText(expandable.getTitle());
    }

    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        RotateAnimation rotate =
                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        imageView.setAnimation(rotate);
    }

    private void animateCollapse() {
        RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        imageView.setAnimation(rotate);
    }

}
