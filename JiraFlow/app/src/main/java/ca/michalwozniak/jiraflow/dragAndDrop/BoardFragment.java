package ca.michalwozniak.jiraflow.dragAndDrop;

/**
 * Created by Michal Wozniak on 8/4/2016.
 */

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.woxthebox.draglistview.BoardView;
import com.woxthebox.draglistview.DragItem;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import ca.michalwozniak.jiraflow.R;
import ca.michalwozniak.jiraflow.helper.JQLHelper;
import ca.michalwozniak.jiraflow.model.BoardConfiguration;
import ca.michalwozniak.jiraflow.model.Issue.Issue;
import ca.michalwozniak.jiraflow.model.Sprint;
import ca.michalwozniak.jiraflow.model.other.Column;
import ca.michalwozniak.jiraflow.service.JiraSoftwareService;
import ca.michalwozniak.jiraflow.service.ServiceGenerator;
import ca.michalwozniak.jiraflow.utility.PreferenceManager;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BoardFragment extends Fragment {

    private static int sCreatedItems = 0;
    private BoardView mBoardView;
    private int mColumns;
    private Unbinder unbinder;
    private PreferenceManager preferenceManager;
    private Activity myActivity;



    public static BoardFragment newInstance() {

        return new BoardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_board_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        preferenceManager = PreferenceManager.getInstance(myActivity);


        mBoardView = (BoardView) view.findViewById(R.id.board_view);
        mBoardView.setSnapToColumnsWhenScrolling(true);
        mBoardView.setSnapToColumnWhenDragging(true);
        mBoardView.setSnapDragItemToTouch(true);
        mBoardView.setCustomDragItem(new MyDragItem(getActivity(), R.layout.test_column_item));
        mBoardView.setBoardListener(new BoardView.BoardListener() {
            @Override
            public void onItemDragStarted(int column, int row) {
                Toast.makeText(mBoardView.getContext(), "Start - column: " + column + " row: " + row, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemChangedColumn(int oldColumn, int newColumn) {
                TextView itemCount1 = (TextView) mBoardView.getHeaderView(oldColumn).findViewById(R.id.item_count);
                itemCount1.setText(Integer.toString(mBoardView.getAdapter(oldColumn).getItemCount()));
                TextView itemCount2 = (TextView) mBoardView.getHeaderView(newColumn).findViewById(R.id.item_count);
                itemCount2.setText(Integer.toString(mBoardView.getAdapter(newColumn).getItemCount()));
            }

            @Override
            public void onItemDragEnded(int fromColumn, int fromRow, int toColumn, int toRow) {
                if (fromColumn != toColumn || fromRow != toRow) {
                    Toast.makeText(mBoardView.getContext(), "End - column: " + toColumn + " row: " + toRow, Toast.LENGTH_SHORT).show();
                }
            }
        });
        myActivity = super.getActivity();

        getBoardConfiguration();

        return view;
    }

    private void getBoardConfiguration() {


        final JiraSoftwareService jiraService = ServiceGenerator.createService(JiraSoftwareService.class, preferenceManager.getUsername(), preferenceManager.getPassword());

        final int boardID = 1;
        jiraService.getBoardConfiguration(boardID)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BoardConfiguration>() {
                    @Override
                    public void onCompleted() {
                        //do nothing

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("errorSubscriber", e.getMessage());
                    }

                    @Override
                    public void onNext(final BoardConfiguration boardConfig) {



                        //Log.v("list size - " , String.valueOf(type.size()));
                        //generateBoard(boardConfig,null);


                        JQLHelper jqlHelper = new JQLHelper(JQLHelper.Query.PROJECT.toString(),"HEL AND sprint in openSprints()");

                        jiraService.getIssuesForActiveSprint(jqlHelper.toString())
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<Sprint>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(Sprint sprint) {
                                        generateBoard(boardConfig,sprint);
                                    }
                                });
                    }
                });
    }

    private void generateBoard(BoardConfiguration boardConfig, Sprint sprint) {


        if(getActivity() != null)
        {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(boardConfig.getName());
        }

        for(Column current : boardConfig.getColumnConfig().getColumns())
        {
            addCustomColumnList(current,sprint);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_board, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_disable_drag).setVisible(mBoardView.isDragEnabled());
        menu.findItem(R.id.action_enable_drag).setVisible(!mBoardView.isDragEnabled());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_disable_drag:
                mBoardView.setDragEnabled(false);
                getActivity().invalidateOptionsMenu();
                return true;
            case R.id.action_enable_drag:
                mBoardView.setDragEnabled(true);
                getActivity().invalidateOptionsMenu();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private View addCustomColumnList(Column current, Sprint sprint) {
        final ArrayList<Pair<Long, DragCardData>> mItemArray = new ArrayList<>();


        int addItems = 0;
        for(Issue issue : sprint.getIssues())
        {
            if(issue.getFields().getStatus().getName().equals(current.getName()))
            {
                DragCardData information = new DragCardData(issue.getKey(),issue.getFields().getSummary(),issue.getFields().getIssuetype().getName());
                mItemArray.add(new Pair<>(Long.parseLong(issue.getId()), information));
                addItems++;
            }
        }

        final int column = mColumns;
        final ItemAdapter listAdapter = new ItemAdapter(mItemArray, R.layout.test_column_item, R.id.item_layout, true);
        final View header = View.inflate(getActivity(), R.layout.test_column_header, null);
        ((TextView) header.findViewById(R.id.text)).setText(current.getName());
        ((TextView) header.findViewById(R.id.item_count)).setText("" + addItems);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = sCreatedItems++;
                Pair item = new Pair<>(id, "FieldCustom " + id);
                mBoardView.addItem(column, 0, item, true);
                //mBoardView.moveItem(4, 0, 0, true);
                //mBoardView.removeItem(column, 0);
                //mBoardView.moveItem(0, 0, 1, 3, false);
                //mBoardView.replaceItem(0, 0, item1, true);
                ((TextView) header.findViewById(R.id.item_count)).setText("" + mItemArray.size());
            }
        });

        mBoardView.addColumnList(listAdapter, header, false);
        mColumns++;
        return header;
    }

    private static class MyDragItem extends DragItem {

        public MyDragItem(Context context, int layoutId) {
            super(context, layoutId);
        }

        @Override
        public void onBindDragView(View clickedView, View dragView) {
            CharSequence text = ((TextView) clickedView.findViewById(R.id.text)).getText();
            ((TextView) dragView.findViewById(R.id.text)).setText(text);

            ImageView clickedViewImage = (ImageView) clickedView.findViewById(R.id.head_image);
            ImageView dragImage = (ImageView) dragView.findViewById(R.id.head_image);

            Drawable image = clickedViewImage.getDrawable();
            dragImage.setImageDrawable(image);

            CardView dragCard = ((CardView) dragView.findViewById(R.id.card));
            CardView clickedCard = ((CardView) clickedView.findViewById(R.id.card));

            dragCard.setMaxCardElevation(40);
            dragCard.setCardElevation(clickedCard.getCardElevation());
            // I know the dragView is a FrameLayout and that is why I can use setForeground below api level 23
            //dragCard.setForeground(clickedView.getResources().getDrawable(R.drawable.zzz_access_point));
        }

        @Override
        public void onMeasureDragView(View clickedView, View dragView) {
            CardView dragCard = ((CardView) dragView.findViewById(R.id.card));
            CardView clickedCard = ((CardView) clickedView.findViewById(R.id.card));

            int widthDiff = dragCard.getPaddingLeft() - clickedCard.getPaddingLeft() + dragCard.getPaddingRight() -
                    clickedCard.getPaddingRight();
            int heightDiff = dragCard.getPaddingTop() - clickedCard.getPaddingTop() + dragCard.getPaddingBottom() -
                    clickedCard.getPaddingBottom();
            int width = clickedView.getMeasuredWidth() + widthDiff;
            int height = clickedView.getMeasuredHeight() + heightDiff;
            dragView.setLayoutParams(new FrameLayout.LayoutParams(width, height));

            int widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
            dragView.measure(widthSpec, heightSpec);
        }

        @Override
        public void onStartDragAnimation(View dragView) {
            CardView dragCard = ((CardView) dragView.findViewById(R.id.card));

            ObjectAnimator anim = ObjectAnimator.ofFloat(dragCard, "CardElevation", dragCard.getCardElevation(), 40);
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(ANIMATION_DURATION);
            anim.start();
        }

        @Override
        public void onEndDragAnimation(View dragView) {
            CardView dragCard = ((CardView) dragView.findViewById(R.id.card));
            ObjectAnimator anim = ObjectAnimator.ofFloat(dragCard, "CardElevation", dragCard.getCardElevation(), 6);
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(ANIMATION_DURATION);
            anim.start();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}