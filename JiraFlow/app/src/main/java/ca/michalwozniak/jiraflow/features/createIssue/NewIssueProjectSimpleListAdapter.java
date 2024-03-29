package ca.michalwozniak.jiraflow.features.createIssue;

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
import ca.michalwozniak.jiraflow.model.ImageType;
import ca.michalwozniak.jiraflow.model.Project;
import ca.michalwozniak.jiraflow.utility.ResourceManager;

/**
 * See the sample project to understand how this is used. Mimics the Simple List dialog style
 * displayed on Google's guidelines site: https://www.google.com/design/spec/components/dialogs.html#dialogs-simple-dialogs
 *
 * @author Aidan Follestad (afollestad)
 */
public class NewIssueProjectSimpleListAdapter extends RecyclerView.Adapter<NewIssueProjectSimpleListAdapter.SimpleListVH> implements MDAdapter {

    public interface Callback {
        void onMaterialListItemSelected(int index, Project item);
    }

    private MaterialDialog dialog;
    private List<Project> mItems;
    private Callback mCallback;

    public NewIssueProjectSimpleListAdapter(Callback callback, List<Project> projectList) {
       // mItems = new ArrayList<>();
        mItems = projectList;
        mCallback = callback;
    }

    public void add(Project item) {
        mItems.add(item);
        notifyItemInserted(mItems.size() - 1);
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    public Project getItem(int index) {
        return mItems.get(index);
    }

    @Override
    public void setDialog(MaterialDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public SimpleListVH onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.material_singlelist_project, parent, false);
        return new SimpleListVH(view, this);
    }

    @Override
    public void onBindViewHolder(SimpleListVH holder, int position) {
        if (dialog != null) {

            final Project item = mItems.get(position);

            if (item.getImageType() == ImageType.SVG) {
                ResourceManager.loadImageSVG(holder.context,item.getAvatarUrls().getBig(),holder.icon);

            } else {

                ResourceManager.loadImage(holder.context,item.getAvatarUrls().getBig(),holder.icon);
            }
            holder.title.setTextColor(dialog.getBuilder().getItemColor());

            if(item.getName().length()> 15)
            {
                String projectName = item.getName().substring(0,15);
                projectName = projectName.concat("...");
                holder.title.setText(projectName);
            }else
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
        final NewIssueProjectSimpleListAdapter adapter;
        Context context;

        public SimpleListVH(View itemView, final NewIssueProjectSimpleListAdapter adapter) {
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
                adapter.mCallback.onMaterialListItemSelected(getAdapterPosition(), adapter.getItem(getAdapterPosition()));
                adapter.dialog.dismiss();
            }
        }
    }
}
