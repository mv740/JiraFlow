package ca.michalwozniak.jiraflow.features.createIssue;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDAdapter;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.caverock.androidsvg.SVG;

import java.io.InputStream;
import java.util.List;

import ca.michalwozniak.jiraflow.R;
import ca.michalwozniak.jiraflow.model.User;
import ca.michalwozniak.jiraflow.utility.ResourceManager;

/**
 * See the sample project to understand how this is used. Mimics the Simple List dialog style
 * displayed on Google's guidelines site: https://www.google.com/design/spec/components/dialogs.html#dialogs-simple-dialogs
 *
 * @author Aidan Follestad (afollestad)
 */
public class UserSimpleListAdapter extends RecyclerView.Adapter<UserSimpleListAdapter.SimpleListVH> implements MDAdapter {

    public interface Callback {
        void onMaterialListItemSelected(int index, User item);
    }

    private MaterialDialog dialog;
    private List<User> mItems;
    private Callback mCallback;

    public UserSimpleListAdapter(Callback callback, List<User> projectList) {
       // mItems = new ArrayList<>();
        mItems = projectList;
        mCallback = callback;
    }

    public void add(User item) {
        mItems.add(item);
        notifyItemInserted(mItems.size() - 1);
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    public User getItem(int index) {
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

            final User item = mItems.get(position);

//            if (item.getImageType() == ImageType.SVG) {
//                ResourceManager.loadImageSVG(holder.context,item.getAvatarUrls().getSmall(),holder.icon);
//
//            } else {
//
//                ResourceManager.loadImage(holder.context,item.getAvatarUrls().getSmall(),holder.icon);
//            }



            final GlideUrl glideUrl = new GlideUrl( item.getAvatarUrls().getBig(), new LazyHeaders.Builder()
                    .addHeader("Authorization", ResourceManager.getEncoredCredentialString(holder.itemView.getContext()))
                    .addHeader("Accept", "application/json")
                    .build());

            GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder = ResourceManager.getGenericRequestBuilderForSVG(holder.itemView.getContext());

            requestBuilder
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .load(Uri.parse(item.getAvatarUrls().getSmall()))
                    .listener(new RequestListener<Uri, PictureDrawable>() {
                        @Override
                        public boolean onException(Exception e, Uri model, Target<PictureDrawable> target, boolean isFirstResource) {
                            Log.v("Profile Icon", "png");

                            Glide.with(holder.itemView.getContext())
                                    .load(glideUrl)
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .dontTransform()
                                    .dontAnimate()
                                    .into(holder.icon);

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(PictureDrawable resource, Uri model, Target<PictureDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(holder.icon);


            holder.title.setTextColor(dialog.getBuilder().getItemColor());
            holder.title.setText(item.getKey());
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
        final UserSimpleListAdapter adapter;
        Context context;

        public SimpleListVH(View itemView, final UserSimpleListAdapter adapter) {
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
