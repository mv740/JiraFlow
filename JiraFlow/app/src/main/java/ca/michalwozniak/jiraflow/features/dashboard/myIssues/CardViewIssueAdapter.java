package ca.michalwozniak.jiraflow.features.dashboard.myIssues;

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
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.caverock.androidsvg.SVG;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ca.michalwozniak.jiraflow.R;
import ca.michalwozniak.jiraflow.model.Issue.Issue;
import ca.michalwozniak.jiraflow.utility.ResourceManager;


public class CardViewIssueAdapter extends RecyclerView.Adapter<CardViewIssueAdapter.IssueViewHolder> implements Filterable {

    private List<Issue> backup;
    private List<Issue> issueList;
    private CustomFilter filter = new CustomFilter();

    public CardViewIssueAdapter(List<Issue> original) {
        this.issueList = original;
        this.backup = original;
    }

    @Override
    public IssueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_issue, parent, false);
        return new IssueViewHolder(v);
    }

    @Override
    public void onBindViewHolder(IssueViewHolder holder, int position) {

        GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder = ResourceManager.getGenericRequestBuilderForSVG(holder.context);

        requestBuilder
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .load(Uri.parse(issueList.get(position).getFields().getIssuetype().getIconUrl()))
                .into(holder.circleImageView);

        holder.title.setText(issueList.get(position).getKey());
        holder.subTitle.setText(issueList.get(position).getFields().getSummary());

        String status = issueList.get(position).getFields().getStatus().getName();


        Log.e("statusText", status);
        holder.statusText.setTextColor(ResourceManager.getStatusTextColor(issueList.get(position).getFields().getStatus().getStatusCategory().getColorName()));
        holder.statusText.setText(status);

    }

    @Override
    public int getItemCount() {
        return issueList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }


    public static class IssueViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.image)
        ImageView circleImageView;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.subtitle)
        TextView subTitle;
        @BindView(R.id.statusText)
        TextView statusText;
        @BindView(R.id.buttonMessage)
        ImageButton button;

        Context context;


        public IssueViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.context = itemView.getContext();

            Drawable icon = ContextCompat.getDrawable(context, R.drawable.zzz_message);
            icon.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

            //button.setCompoundDrawables(null,icon,null,null);
            button.setImageDrawable(icon);
            button.setOnClickListener(v -> Log.d("buttonMesg", "click"));

        }

    }

    public void reset()
    {
        issueList = backup;
        notifyDataSetChanged();
    }

    private class CustomFilter extends Filter {


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic

            // We perform filtering operation
            List<Issue> newIssueList = new ArrayList<>();

            //always filter from original backup list
            for (Issue i : backup) {

                if (i.getFields().getStatus().getName().toUpperCase().contains(constraint.toString().toUpperCase()))
                    newIssueList.add(i);
            }

            results.values = newIssueList;
            results.count = newIssueList.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            // Now we have to inform the adapter about the new list filtered

            //issueList = (List<Issue>) results.values;
            issueList = (List<Issue>) results.values;
            notifyDataSetChanged();


        }

    }
}
