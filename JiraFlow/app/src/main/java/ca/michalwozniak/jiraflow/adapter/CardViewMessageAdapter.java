package ca.michalwozniak.jiraflow.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.caverock.androidsvg.SVG;

import java.io.InputStream;
import java.util.List;

import ca.michalwozniak.jiraflow.R;
import ca.michalwozniak.jiraflow.model.Feed.Entry;
import ca.michalwozniak.jiraflow.model.ImageType;
import ca.michalwozniak.jiraflow.utility.ResourceManager;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by michal on 5/4/2016.
 */
public class CardViewMessageAdapter extends RecyclerView.Adapter<CardViewMessageAdapter.ProjectViewHolder> {


    public static class ProjectViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        CircleImageView circleImageView;
        TextView title;
        TextView message_content;
        ImageView messageTypeIcon;
        TextView dateUpdated;
        Context context;



        public ProjectViewHolder(final View itemView) {
            super(itemView);
            this.cardView = (CardView) itemView.findViewById(R.id.cardViewMessage);
            this.circleImageView = (CircleImageView) itemView.findViewById(R.id.image);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.message_content = (TextView) itemView.findViewById(R.id.message_content);
            this.messageTypeIcon = (ImageView) itemView.findViewById(R.id.messageTypeIcon);
            this.dateUpdated = (TextView) itemView.findViewById(R.id.dateUpdated) ;

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

        //Svg picture are not protected on jira : public
        if (messages.get(position).getImageType() == ImageType.SVG) {
            GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder = ResourceManager.getGenericRequestBuilderForSVG(holder.context);

            requestBuilder
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .load(Uri.parse(messages.get(position).getAuthor().getLink().get(0).getHref()))
                    .into(holder.circleImageView);
        } else {


            GlideUrl glideUrl = new GlideUrl(messages.get(position).getAuthor().getLink().get(0).getHref(), new LazyHeaders.Builder()
                    .addHeader("Authorization", ResourceManager.getEncoredCredentialString(holder.context))
                    .addHeader("Accept", "application/json")
                    .build());

            Glide
                    .with(holder.context)
                    .load(glideUrl)
                    .placeholder(R.drawable.ic_check)
                    .error(R.drawable.zzz_controller_xbox)
                    .dontAnimate()
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.circleImageView);
        }

        holder.title.setText(Html.fromHtml(messages.get(position).getTitle()));

        if(messages.get(position).getContent() != null)
        {
            holder.message_content.setText(Html.fromHtml(messages.get(position).getContent()));
        }


        String newFormat = ResourceManager.getDate(messages.get(position).getUpdated());
        holder.dateUpdated.setText(newFormat);

        GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder = ResourceManager.getGenericRequestBuilderForSVG(holder.context);

        requestBuilder
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .load(Uri.parse(messages.get(position).getLink().get(1).getHref()))
                .into(holder.messageTypeIcon);


    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
