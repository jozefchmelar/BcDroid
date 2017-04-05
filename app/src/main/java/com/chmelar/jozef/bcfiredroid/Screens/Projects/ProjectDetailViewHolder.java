package com.chmelar.jozef.bcfiredroid.Screens.Projects;


import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
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
    @BindView(R.id.llMessages)
    LinearLayout messagesLayout;
    @BindView(R.id.llPeople)
    LinearLayout pplLayout;
    private IProjectDetail clicks;

    public ProjectDetailViewHolder(@NonNull Context context, @LayoutRes int layoutRes, @NonNull ViewGroup parent,final IProjectDetail clicks) {

        super(context, layoutRes, parent);
        ButterKnife.bind(this, itemView);
       this.clicks=clicks;
    }

    @Override
    public void setData(@NonNull Project data) {
        final Project p = data;
        projectName.setText(data.getName());
        projectNumber.setText(data.get_id()+"");
        costumer.setText(data.getCostumer());
        pplLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicks.onPeopleClick(p);
            }
        });
        messagesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicks.onMessageClick(p);
            }
        });

    }

}
