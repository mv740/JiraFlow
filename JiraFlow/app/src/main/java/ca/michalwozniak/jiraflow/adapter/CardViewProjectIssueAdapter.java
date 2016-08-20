package ca.michalwozniak.jiraflow.adapter;

/**
 * Created by michal on 7/10/2016.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ca.michalwozniak.jiraflow.R;
import ca.michalwozniak.jiraflow.model.Issue.Issue;
import ca.michalwozniak.jiraflow.utility.ResourceManager;


public class CardViewProjectIssueAdapter extends RecyclerView.Adapter<CardViewProjectIssueAdapter.ProjectViewHolder> {


    public static class ProjectViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.image)
        ImageView circleImageView;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.subtitle)
        TextView subTitle;

        Context context;


        public ProjectViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            Drawable icon = ContextCompat.getDrawable(context, R.drawable.zzz_message);
            icon.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

            //need to style using xml style
            title.setPadding(0,0,0,0);
            title.setTextSize(15);
            ImageButton button = (ImageButton) itemView.findViewById(R.id.buttonMessage);
            //button.setCompoundDrawables(null,icon,null,null);
            button.setImageDrawable(icon);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("buttonMesg","click");
                }
            });

        }

    }

    List<Issue> issues;

    public CardViewProjectIssueAdapter(List<Issue> issues)
    {
        this.issues = issues;
    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_issue,parent,false);
        return new ProjectViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProjectViewHolder holder, int position) {
        ResourceManager.loadImageSVG(holder.context,issues.get(position).getFields().getIssuetype().getIconUrl(),holder.circleImageView);
        holder.title.setText(issues.get(position).getKey());
        holder.subTitle.setText(issues.get(position).getFields().getSummary());

    }

    @Override
    public int getItemCount() {
        return issues.size();
    }
}
