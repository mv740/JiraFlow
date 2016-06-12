package ca.michalwozniak.jiraflow.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import ca.michalwozniak.jiraflow.R;
import ca.michalwozniak.jiraflow.model.Feed.Entry;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by michal on 5/4/2016.
 */
public class CardViewMessageAdapter extends RecyclerView.Adapter<CardViewMessageAdapter.ProjectViewHolder> {


    public static class ProjectViewHolder extends RecyclerView.ViewHolder{

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

    List<Entry> messages;

    public CardViewMessageAdapter(List<Entry> messages)
    {
        this.messages = messages;
    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_message,parent,false);

        //set the view's size, margin , padding and layout parameters
        //...
        return new ProjectViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProjectViewHolder holder, int position) {
        holder.circleImageView.setImageDrawable(messages.get(position).getAvatar());
        holder.title.setText(messages.get(position).getAuthor().getName());
        holder.subTitle.setText(messages.get(position).getObject().getTitle());

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
