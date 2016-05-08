package ca.michalwozniak.jiraflow.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ca.michalwozniak.jiraflow.R;
import ca.michalwozniak.jiraflow.model.Project;

/**
 * Created by michal on 5/4/2016.
 */
public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ProjectViewHolder> {

    public static class ProjectViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView title;
        TextView subTitle;

        public ProjectViewHolder(View itemView) {
            super(itemView);
            this.cardView = (CardView) itemView.findViewById(R.id.cardView);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.subTitle = (TextView) itemView.findViewById(R.id.subtitle);
        }

    }

    List<Project> projects;

    CardViewAdapter(List<Project> projects)
    {
        this.projects = projects;
    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ProjectViewHolder holder, int position) {

        holder.title.setText(projects.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }
}
