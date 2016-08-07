package ca.michalwozniak.jiraflow.dragAndDrop;

/**
 * Created by Michal Wozniak on 8/4/2016.
 */

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;
import java.util.Map;

import ca.michalwozniak.jiraflow.R;

public class ItemAdapter extends DragItemAdapter<Pair<Long, String>, ItemAdapter.ViewHolder> {

    private int mLayoutId;
    private int mGrabHandleId;
    private Map<Long, String> mItemMapIcon;
    private Map<String, Drawable> iconSelection;

    public ItemAdapter(ArrayList<Pair<Long, String>> list, Map<Long, String> mItemMapIcon, int layoutId, int grabHandleId, boolean dragOnLongPress) {
        super(dragOnLongPress);
        mLayoutId = layoutId;
        mGrabHandleId = grabHandleId;
        setHasStableIds(true);
        setItemList(list);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);


    return new ViewHolder(view);
}

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        String text = mItemList.get(position).second;
        holder.mText.setText(text);
        holder.itemView.setTag(text);
        String stringId = String.valueOf(mItemList.get(position).first);

        //// TODO: 8/7/2016 add bug/task/story/epic to the drawable svg ressource because we can't load them directly using glide
        // during hover between colunm we get a null pointer 


    }

    @Override
    public long getItemId(int position) {
        return mItemList.get(position).first;
    }

public class ViewHolder extends DragItemAdapter<Pair<Long, String>, ItemAdapter.ViewHolder>.ViewHolder {
    private TextView mText;
    private ImageView mImage;
    private Context mContext;

    public ViewHolder(final View itemView) {
        super(itemView, mGrabHandleId);
        mText = (TextView) itemView.findViewById(R.id.text);
        mImage = (ImageView) itemView.findViewById(R.id.head_image);
        mContext = itemView.getContext();
    }

    @Override
    public void onItemClicked(View view) {
        Toast.makeText(view.getContext(), "Item clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClicked(View view) {
        Toast.makeText(view.getContext(), "Item long clicked", Toast.LENGTH_SHORT).show();
        return true;
    }
}
}