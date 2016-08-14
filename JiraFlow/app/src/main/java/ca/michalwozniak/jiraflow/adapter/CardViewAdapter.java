package ca.michalwozniak.jiraflow.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.caverock.androidsvg.SVG;

import java.io.InputStream;
import java.util.List;

import ca.michalwozniak.jiraflow.ProjectActivity;
import ca.michalwozniak.jiraflow.R;
import ca.michalwozniak.jiraflow.model.ImageType;
import ca.michalwozniak.jiraflow.model.Project;
import ca.michalwozniak.jiraflow.utility.ResourceManager;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Michal Wozniak on 5/4/2016.
 */
public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ProjectViewHolder> {


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

    public CardViewAdapter(List<Project> projects) {
        this.projects = projects;
    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);

        //set the view's size, margin , padding and layout parameters
        //...
        return new ProjectViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProjectViewHolder holder, int position) {


        if (projects.get(position).getImageType() == ImageType.SVG) {
            GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder = ResourceManager.getGenericRequestBuilderForSVG(holder.context);

            requestBuilder
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .load(Uri.parse(projects.get(position).getAvatarUrls().getSmall()))
                    .into(holder.circleImageView);
        } else {

            GlideUrl glideUrl = new GlideUrl(projects.get(position).getAvatarUrls().getBig(), new LazyHeaders.Builder()
                    .addHeader("Authorization", ResourceManager.getEncoredCredentialString(holder.context))
                    .addHeader("Accept", "application/json")
                    .build());

            Glide
                    .with(holder.context)
                    .load(glideUrl)
                    //.load(Uri.parse(ResourceManager.fixImageUrl(projects.get(position).getAvatarUrls().getSmall())))
                    .placeholder(R.drawable.ic_check)
                    .error(R.drawable.zzz_controller_xbox)
                    .dontAnimate()
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .listener(new RequestListener<Uri, GlideDrawable>() {
//                        @Override
//                        public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
//                            Log.e("glideError",e.getMessage());
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                            return false;
//                        }
//                    })
                    .into(holder.circleImageView);
        }

        holder.title.setText(projects.get(position).getName());
        holder.subTitle.setText(projects.get(position).getProjectTypeKey());

    }

    @Override
    public int getItemCount() {
        return projects.size();
    }
}
