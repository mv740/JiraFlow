package ca.michalwozniak.jiraflow.dragAndDrop;

/**
 * Created by Michal Wozniak on 8/4/2016.
 */

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
import ca.michalwozniak.jiraflow.utility.ResourceManager;

public class ItemAdapter extends DragItemAdapter<Pair<Long, DragCardData>, ItemAdapter.ViewHolder> {

    private int mLayoutId;
    private int mGrabHandleId;
    private Map<Long, String> mItemMapIconType;

    public ItemAdapter(ArrayList<Pair<Long, DragCardData>> list, int layoutId, int grabHandleId, boolean dragOnLongPress) {
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

        DragCardData data = mItemList.get(position).second;

        String text =data.getSummary();
        holder.mText.setText(text);
        holder.itemView.setTag(text);
        
        holder.mImage.setImageResource(ResourceManager.getIssueTypeIconId(data.getIconType()));
    }

    @Override
    public long getItemId(int position) {
        return mItemList.get(position).first;
    }

public class ViewHolder extends DragItemAdapter<Pair<Long, DragCardData>, ItemAdapter.ViewHolder>.ViewHolder {
    private TextView mText;
    private ImageView mImage;

    public ViewHolder(final View itemView) {
        super(itemView, mGrabHandleId);
        mText = (TextView) itemView.findViewById(R.id.text);
        mImage = (ImageView) itemView.findViewById(R.id.head_image);
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