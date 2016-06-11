package ca.michalwozniak.jiraflow.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ca.michalwozniak.jiraflow.R;
import ca.michalwozniak.jiraflow.model.Project;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by michal on 5/4/2016.
 */
public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ProjectViewHolder> {

    public static class ProjectViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        CircleImageView circleImageView;
        TextView title;
        TextView subTitle;

        public ProjectViewHolder(final View itemView) {
            super(itemView);
            this.cardView = (CardView) itemView.findViewById(R.id.cardView);
            this.circleImageView = (CircleImageView) itemView.findViewById(R.id.image);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.subTitle = (TextView) itemView.findViewById(R.id.subtitle);

            itemView.findViewById(R.id.ripple).setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Log.d("Ripple","click");
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("clicked","click");
                }
            });
        }

    }

    List<Project> projects;

    public CardViewAdapter(List<Project> projects)
    {
        this.projects = projects;
    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view,parent,false);

        //set the view's size, margin , padding and layout parameters
        //...
        return new ProjectViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProjectViewHolder holder, int position) {
        holder.circleImageView.setImageDrawable(projects.get(position).getAvatar());
        holder.title.setText(projects.get(position).getName());
        holder.subTitle.setText(projects.get(position).getProjectTypeKey());

    }

    @Override
    public int getItemCount() {
        return projects.size();
    }
}
