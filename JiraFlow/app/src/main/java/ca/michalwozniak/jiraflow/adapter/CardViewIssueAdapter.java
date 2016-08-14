package ca.michalwozniak.jiraflow.adapter;

/**
 * Created by Michal Wozniak on 7/24/2016.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.caverock.androidsvg.SVG;

import java.io.InputStream;
import java.util.List;

import ca.michalwozniak.jiraflow.R;
import ca.michalwozniak.jiraflow.model.Issue.Issue;
import ca.michalwozniak.jiraflow.utility.ResourceManager;


public class CardViewIssueAdapter extends RecyclerView.Adapter<CardViewIssueAdapter.IssueViewHolder> {


    public static class IssueViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageView circleImageView;
        TextView title;
        TextView subTitle;
        Context context;


        public IssueViewHolder(final View itemView) {
            super(itemView);
            this.cardView = (CardView) itemView.findViewById(R.id.cardViewIssue);
            this.circleImageView = (ImageView) itemView.findViewById(R.id.image);
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

    List<Issue> issues;

    public CardViewIssueAdapter(List<Issue> issues)
    {
        this.issues = issues;
    }

    @Override
    public IssueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_issue,parent,false);

        //set the view's size, margin , padding and layout parameters
        //...
        return new IssueViewHolder(v);
    }

    @Override
    public void onBindViewHolder(IssueViewHolder holder, int position) {

        GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder = ResourceManager.getGenericRequestBuilderForSVG(holder.context);

        requestBuilder
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .load(Uri.parse(issues.get(position).getFields().getIssuetype().getIconUrl()))
                .into(holder.circleImageView);

        holder.title.setText(issues.get(position).getKey());
        holder.subTitle.setText(issues.get(position).getFields().getSummary());

    }

    @Override
    public int getItemCount() {
        return issues.size();
    }
}
