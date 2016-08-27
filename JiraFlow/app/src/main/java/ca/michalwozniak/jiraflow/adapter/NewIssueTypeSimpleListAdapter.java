package ca.michalwozniak.jiraflow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDAdapter;

import java.util.List;

import ca.michalwozniak.jiraflow.R;
import ca.michalwozniak.jiraflow.model.Issue.issueType;
import ca.michalwozniak.jiraflow.utility.ResourceManager;

/**
 * Created by Michal Wozniak on 8/25/2016.
 *
 * based on aidan Follestad version
 */

public class NewIssueTypeSimpleListAdapter extends RecyclerView.Adapter<NewIssueTypeSimpleListAdapter.SimpleListVH> implements MDAdapter {

    public interface Callback {
        void onIssueTypeItemSelected(int index, issueType item);
    }

    private MaterialDialog dialog;
    private List<issueType> mItems;
    private Callback mCallback;

    public NewIssueTypeSimpleListAdapter(Callback callback, List<issueType> issueTypeList) {
        // mItems = new ArrayList<>();
        mItems = issueTypeList;
        mCallback = callback;
    }

    public void add(issueType item) {
        mItems.add(item);
        notifyItemInserted(mItems.size() - 1);
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    public issueType getItem(int index) {
        return mItems.get(index);
    }

    @Override
    public void setDialog(MaterialDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public SimpleListVH onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.material_singlelist_issue_type, parent, false);
        return new SimpleListVH(view, this);
    }

    @Override
    public void onBindViewHolder(SimpleListVH holder, int position) {
        if (dialog != null) {

            final issueType item = mItems.get(position);
            ResourceManager.loadImageSVG(holder.context,item.getIconUrl(),holder.icon);
            holder.title.setTextColor(dialog.getBuilder().getItemColor());
            holder.title.setText(item.getName());
            dialog.setTypeface(holder.title, dialog.getBuilder().getRegularFont());
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class SimpleListVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView icon;
        final TextView title;
        final NewIssueTypeSimpleListAdapter adapter;
        Context context;

        public SimpleListVH(View itemView, final NewIssueTypeSimpleListAdapter adapter) {
            super(itemView);
            this.context = itemView.getContext();
            icon = (ImageView) itemView.findViewById(android.R.id.icon);
            title = (TextView) itemView.findViewById(android.R.id.title);
            this.adapter = adapter;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (adapter.mCallback != null)
            {
                adapter.mCallback.onIssueTypeItemSelected(getAdapterPosition(), adapter.getItem(getAdapterPosition()));
                adapter.dialog.dismiss();
            }
        }
    }
}

