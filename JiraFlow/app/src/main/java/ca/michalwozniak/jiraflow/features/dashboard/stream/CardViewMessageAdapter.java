package ca.michalwozniak.jiraflow.features.dashboard.stream;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ca.michalwozniak.jiraflow.R;
import ca.michalwozniak.jiraflow.model.Feed.Entry;
import ca.michalwozniak.jiraflow.model.ImageType;
import ca.michalwozniak.jiraflow.utility.ResourceManager;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by michal on 5/4/2016.
 */
public class CardViewMessageAdapter extends RecyclerView.Adapter<CardViewMessageAdapter.ProjectViewHolder> {


    private Context context;

    public static class ProjectViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        CircleImageView circleImageView;
        @BindView(R.id.message_contentImage)
        ImageView messageContentImage;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.message_content)
        TextView message_content;
        @BindView(R.id.messageTypeIcon)
        ImageView messageTypeIcon;
        @BindView(R.id.dateUpdated)
        TextView dateUpdated;
        @BindView(R.id.buttonMessage)
        ImageButton button;
        Context holderContext;


        public ProjectViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.holderContext = itemView.getContext();

            Drawable icon = ContextCompat.getDrawable(holderContext, R.drawable.zzz_message);
            icon.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

            title.setPadding(0, 0, 0, 0);
            title.setTextSize(15);

            button.setImageDrawable(icon);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("buttonMesg", "click");
                }
            });

        }

    }

    List<Entry> messages;

    public CardViewMessageAdapter(List<Entry> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_message, parent, false);
        return new ProjectViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProjectViewHolder holder, int position) {
        //initialized to empty, or else view will used previous object text if it isn't overwritten
        holder.message_content.setText(null);

        //Svg picture are not protected on jira : public
        if (messages.get(position).getImageType() == ImageType.SVG) {

            ResourceManager.loadImageSVG(context, messages.get(position).getAuthor().getLink().get(0).getHref(), holder.circleImageView);

        } else {

            ResourceManager.loadImage(context, messages.get(position).getAuthor().getLink().get(0).getHref(), holder.circleImageView);

        }

        String titleHTML = messages.get(position).getTitle();
        String title = String.valueOf(Html.fromHtml(titleHTML));

        holder.title.setText(Html.fromHtml(titleHTML));
        if (messages.get(position).getContent() != null) {

            if (title.contains("attached")) {

                String[] links = ResourceManager.extractLinks(messages.get(position).getContent());

                //images
                if (links.length != 0) {
                    String url = links[0];
                    String extension = url.substring(url.length() - 3);
                    if (extension.contains("png")) {

                        ResourceManager.loadImage(context, url, holder.messageContentImage);
                    }
                }

            }else {
                holder.messageContentImage.setImageDrawable(null); //don't show the content

                String result;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    result = String.valueOf(Html.fromHtml(messages.get(position).getContent(), Html.FROM_HTML_MODE_LEGACY));
                } else {
                    result = String.valueOf(Html.fromHtml(messages.get(position).getContent()));

                }
                holder.message_content.setText(result);
            }


        }else{
            holder.message_content.setText(null);
        }

        //if it is a issue (bug/story ... ) it will have icons links
        if(messages.get(position).getLink().size()>1)
        {
            ResourceManager.loadImageSVG(context, messages.get(position).getLink().get(1).getHref(), holder.messageTypeIcon);
        }else
        {
            holder.messageTypeIcon.setImageDrawable(null);
        }


        String newFormat = ResourceManager.getDate(messages.get(position).getUpdated());
        holder.dateUpdated.setText(newFormat);
    }


    public void add(int position, Entry newItem) {
        messages.add(position, newItem);
        notifyItemInserted(position);

    }

    // This removes the data from our Dataset and Updates the Recycler View.
    public void remove(Entry entryToRemove) {

        int currPosition = messages.indexOf(entryToRemove);
        messages.remove(currPosition);
        notifyItemRemoved(currPosition);
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }
}
