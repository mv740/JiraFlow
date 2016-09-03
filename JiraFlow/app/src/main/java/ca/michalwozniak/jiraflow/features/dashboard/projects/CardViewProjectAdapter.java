package ca.michalwozniak.jiraflow.features.dashboard.projects;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ca.michalwozniak.jiraflow.features.dashboard.projects.issues.ProjectActivity;
import ca.michalwozniak.jiraflow.R;
import ca.michalwozniak.jiraflow.model.ImageType;
import ca.michalwozniak.jiraflow.model.Project;
import ca.michalwozniak.jiraflow.utility.ResourceManager;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Michal Wozniak on 5/4/2016.
 */
public class CardViewProjectAdapter extends RecyclerView.Adapter<CardViewProjectAdapter.ProjectViewHolder> {


    public static class ProjectViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        CircleImageView circleImageView;
        TextView title;
        TextView subTitle;
        Context context;


        public ProjectViewHolder(final View itemView) {
            super(itemView);
            this.cardView = (CardView) itemView.findViewById(R.id.cardView);
            this.circleImageView = (CircleImageView) itemView.findViewById(R.id.image);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.subTitle = (TextView) itemView.findViewById(R.id.subtitle);
            this.context = itemView.getContext();

            itemView.findViewById(R.id.ripple).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Ripple", "click");
                    Log.d("Ripple", title.getText().toString());

                    Intent project = new Intent(context, ProjectActivity.class);
                    project.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    project.putExtra("title", title.getText().toString());
                    context.startActivity(project);
                }
            });
        }

    }

    List<Project> projects;

    public CardViewProjectAdapter(List<Project> projects) {
        this.projects = projects;
    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_project, parent, false);

        //set the view's size, margin , padding and layout parameters
        //...
        return new ProjectViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProjectViewHolder holder, int position) {


        if (projects.get(position).getImageType() == ImageType.SVG) {

            ResourceManager.loadImageSVG(holder.context, projects.get(position).getAvatarUrls().getSmall(), holder.circleImageView);

        } else {

            ResourceManager.loadImage(holder.context, projects.get(position).getAvatarUrls().getBig(), holder.circleImageView);
        }

        int length = projects.get(position).getName().length();
        String title = projects.get(position).getName();
        if (length > 15) {
            if (length > 30) {
                title = title.substring(0,35);
                title += "...";
            }
            holder.title.setTextSize(16);
        } else
            holder.title.setTextSize(24);
        holder.title.setText(title);
        holder.subTitle.setText(projects.get(position).getProjectTypeKey());

    }

    @Override
    public int getItemCount() {
        return projects.size();
    }
}
