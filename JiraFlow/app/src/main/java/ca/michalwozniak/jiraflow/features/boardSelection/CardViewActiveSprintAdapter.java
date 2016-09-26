package ca.michalwozniak.jiraflow.features.boardSelection;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import java.util.List;

import ca.michalwozniak.jiraflow.R;
import ca.michalwozniak.jiraflow.features.board.BoardFragment;
import ca.michalwozniak.jiraflow.model.SprintState;
import ca.michalwozniak.jiraflow.utility.SessionManager;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Michal Wozniak on 5/4/2016.
 */
public class CardViewActiveSprintAdapter extends RecyclerView.Adapter<CardViewActiveSprintAdapter.ProjectViewHolder> {


    private FragmentManager fragmentManager;
    private List<SprintState> sprints;
    private SparseIntArray sprintIds;
    private SparseIntArray boardIds;
    private int favoriteBoardPosition;
    private int currentSavedSprintId;
    private SessionManager sm;
    private static final int NO_FAVORITE = -1;


    public CardViewActiveSprintAdapter(List<SprintState> sprints, FragmentManager fragmentManager, SessionManager sessionManager) {
        this.sprints = sprints;
        this.fragmentManager = fragmentManager;
        this.sprintIds = new SparseIntArray();
        this.boardIds = new SparseIntArray();
        this.sm = sessionManager;
        this.favoriteBoardPosition =NO_FAVORITE;
        this.currentSavedSprintId = sessionManager.getFavoriteSprintId();


    }



    public class ProjectViewHolder extends RecyclerView.ViewHolder {

        MaterialFavoriteButton favoriteBoardButton;
        CardView cardView;
        CircleImageView circleImageView;
        TextView title;
        Context context;


        public ProjectViewHolder(final View itemView) {
            super(itemView);
            this.cardView = (CardView) itemView.findViewById(R.id.cardView);
            this.circleImageView = (CircleImageView) itemView.findViewById(R.id.image);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.favoriteBoardButton = (MaterialFavoriteButton) itemView.findViewById(R.id.favoriteBoard);
            this.context = itemView.getContext();


            itemView.findViewById(R.id.ripple).setOnClickListener(v -> {

                BoardFragment boardFragment = BoardFragment.newInstance();
                Bundle bundle = new Bundle();

                boardFragment.setArguments(bundle);

                fragmentManager.beginTransaction()
                        .replace(R.id.container, boardFragment)
                        .addToBackStack(null)
                        .commit();

            });

            favoriteBoardButton.setOnFavoriteChangeListener((buttonView, favorite) -> {

                if (favorite) {
                    favoriteBoardPosition = getAdapterPosition();
                    currentSavedSprintId = sprintIds.get(getAdapterPosition());
                    sm.saveFavoriteBoardId(boardIds.get(getAdapterPosition()));
                    sm.saveFavoriteSprintId(currentSavedSprintId);
                }else
                {
                    favoriteBoardPosition = NO_FAVORITE;
                    currentSavedSprintId  = NO_FAVORITE;
                    sm.deleteFavoriteBoardId();
                    sm.deleteFavoriteSprintId();
                }
            });
            favoriteBoardButton.setOnFavoriteAnimationEndListener((buttonView, favorite) -> notifyDataSetChanged());

        }

    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_board_selection, parent, false);

        //set the view's size, margin , padding and layout parameters
        //...
        return new ProjectViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProjectViewHolder holder, int position) {

        SprintState sprintState = sprints.get(position);
        holder.title.setText(sprintState.getName());

        boardIds.append(position, sprintState.getOriginBoardId());
        sprintIds.append(position, sprintState.getId());

        if(favoriteBoardPosition != -1)
        {
            holder.favoriteBoardButton.setFavorite(position == favoriteBoardPosition);
        }else
        {
            holder.favoriteBoardButton.setFavorite(sprintState.getId() == currentSavedSprintId);
        }


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
