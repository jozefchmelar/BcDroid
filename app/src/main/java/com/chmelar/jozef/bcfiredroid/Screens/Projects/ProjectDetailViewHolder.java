package com.chmelar.jozef.bcfiredroid.Screens.Projects;


import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chmelar.jozef.bcfiredroid.API.Model.Project;
import com.chmelar.jozef.bcfiredroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.inloop.simplerecycleradapter.SettableViewHolder;

public class ProjectDetailViewHolder extends SettableViewHolder<Project> {
    @BindView(R.id.tvNameOfProject)
    TextView projectName;
    @BindView(R.id.tvProjectNumber)
    TextView projectNumber;
    @BindView(R.id.tvCostumer)
    TextView costumer;
    @BindView(R.id.tvNumberOfMessages)
    TextView numberOfMessages;
    @BindView(R.id.llMessages)
    LinearLayout messagesLayout;

    public ProjectDetailViewHolder(@NonNull Context context, @LayoutRes int layoutRes, @NonNull ViewGroup parent) {
        super(context, layoutRes, parent);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setData(@NonNull Project data) {
        projectName.setText(data.getName());
        projectNumber.setText(data.getNumber());
        costumer.setText(data.getCostumer());
        numberOfMessages.setText(data.getNumber());

    }

}
