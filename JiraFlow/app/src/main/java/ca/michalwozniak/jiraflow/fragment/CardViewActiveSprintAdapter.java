package ca.michalwozniak.jiraflow.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ca.michalwozniak.jiraflow.R;
import ca.michalwozniak.jiraflow.features.board.BoardFragment;
import ca.michalwozniak.jiraflow.model.SprintData;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Michal Wozniak on 5/4/2016.
 */
public class CardViewActiveSprintAdapter extends RecyclerView.Adapter<CardViewActiveSprintAdapter.ProjectViewHolder> {


    private static FragmentManager fragmentManager;

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

                   // context.showFragment(BoardFragment.newInstance());
                    BoardFragment newFragment = new BoardFragment();
                    Bundle bundle = new Bundle();

                    int boardID = Integer.parseInt(String.valueOf(subTitle.getText().toString().charAt(1)));
                    int sprintID = Integer.parseInt(String.valueOf(subTitle.getText().toString().charAt(0)));
                    bundle.putInt("boardID", boardID);
                    bundle.putInt("sprintID", sprintID);
                    newFragment.setArguments(bundle);

                    fragmentManager.beginTransaction()
                            .replace(R.id.container, newFragment)
                            .addToBackStack(null)
                            .commit();

                }
            });
        }

    }

    List<SprintData> sprints;

    public CardViewActiveSprintAdapter(List<SprintData> sprints, android.support.v4.app.FragmentManager fragmentManager) {
        this.sprints = sprints;
        this.fragmentManager = fragmentManager;
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


        Log.e("id --->", String.valueOf(sprints.get(position).getOriginBoardId()));
        holder.title.setText(sprints.get(position).getName());
        holder.subTitle.setText(String.valueOf(sprints.get(position).getId()+String.valueOf(sprints.get(position).getOriginBoardId())));

//        if (sprint.get(position).getImageType() == ImageType.SVG) {
//
//            ResourceManager.loadImageSVG(holder.context, projects.get(position).getAvatarUrls().getSmall(), holder.circleImageView);
//
//        } else {
//
//            ResourceManager.loadImage(holder.context, projects.get(position).getAvatarUrls().getBig(), holder.circleImageView);
//        }
//
//        int length = projects.get(position).getName().length();
//        String title = projects.get(position).getName();
//        if (length > 15) {
//            if (length > 30) {
//                title = title.substring(0,35);
//                title += "...";
//            }
//            holder.title.setTextSize(16);
//        } else
//            holder.title.setTextSize(24);
    // ..   holder.title.setText(title);
     //   holder.subTitle.setText(projects.get(position).getProjectTypeKey());

    }

    @Override
    public int getItemCount() {
        return sprints.size();
    }
}
