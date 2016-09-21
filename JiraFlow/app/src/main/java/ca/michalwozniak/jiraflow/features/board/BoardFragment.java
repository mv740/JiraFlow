package ca.michalwozniak.jiraflow.features.board;

/**
 * Created by Michal Wozniak on 8/4/2016.
 */

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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

import com.woxthebox.draglistview.BoardView;
import com.woxthebox.draglistview.DragItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import ca.michalwozniak.jiraflow.R;
import ca.michalwozniak.jiraflow.features.boardSelection.BoardSelectionFragment;
import ca.michalwozniak.jiraflow.features.dashboard.DashboardActivity;
import ca.michalwozniak.jiraflow.helper.DragCardData;
import ca.michalwozniak.jiraflow.model.BoardConfiguration;
import ca.michalwozniak.jiraflow.model.Issue.Issue;
import ca.michalwozniak.jiraflow.model.Sprint;
import ca.michalwozniak.jiraflow.model.other.Column;
import ca.michalwozniak.jiraflow.model.transition.Transition;
import ca.michalwozniak.jiraflow.model.transition.TransitionModel;
import ca.michalwozniak.jiraflow.utility.LogManager;
import ca.michalwozniak.jiraflow.utility.NetworkManager;
import rx.subscriptions.CompositeSubscription;

public class BoardFragment extends Fragment {

    private static int sCreatedItems = 0;
    private BoardView mBoardView;
    private int mColumns;
    private Unbinder unbinder;
    private NetworkManager networkManager;
    static List<String> columnStatusId;
    static List<String> columnStatus;
    static String currentDraggedIssueKey;
    static int dropColumnIndex;
    private int boardID;
    private int sprintID;
    private Map<String, ImageView> issueTypeIcons;
    private CompositeSubscription subscriptions = new CompositeSubscription();



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

        networkManager = NetworkManager.getInstance(getContext());

        columnStatusId = new ArrayList<>();
        columnStatus = new ArrayList<>();
        issueTypeIcons = new HashMap<>();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            boardID = bundle.getInt("boardID", -1);
            sprintID = bundle.getInt("sprintID", -1);
        }


        mBoardView = (BoardView) view.findViewById(R.id.board_view);
        mBoardView.setSnapToColumnsWhenScrolling(true);
        mBoardView.setSnapToColumnWhenDragging(true);
        mBoardView.setSnapDragItemToTouch(true);
        mBoardView.setCustomDragItem(new MyDragItem(getActivity(), R.layout.test_column_item));
        mBoardView.setBoardListener(new BoardView.BoardListener() {
            @Override
            public void onItemDragStarted(int column, int row) {
                // Toast.makeText(mBoardView.getViewContext(), "Start - column: " + column + " row: " + row, Toast.LENGTH_SHORT).show();
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
                    dropColumnIndex = toColumn;
                    getPossibleTransition();
                }
            }
        });

        // getAllIssueTypeIcons();
        getBoardConfiguration();
        return view;
    }

    //todo need to get all icon in stored them in map
//    private void getAllIssueTypeIcons() {
//        networkManager.getAllIssueType()
//                .subscribe(new Subscriber<List<issueType>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(List<issueType> issueTypeList) {
//                        for (issueType i: issueTypeList
//                             ) {
//
//                            ImageView image = new ImageView(getContext());
//                            ResourceManager.loadImageSVG(getContext(),i.getIconUrl(),image);
//                            issueTypeIcons.put(i.getName(), image);
//
//                        }
//
//                        Handler d = new Handler();
//                        d.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                getBoardConfiguration();
//                            }
//                        },3000);
//                    }
//                });
//
//    }

    private void getPossibleTransition() {
        String name = columnStatus.get(dropColumnIndex);

        subscriptions.add(networkManager.getTransitions(currentDraggedIssueKey, name)
                .subscribe(transition -> doTransition(transition.getId(), name)));


    }

    /**
     * @param id
     * @param name
     */
    private void doTransition(String id, String name) {
        TransitionModel model = new TransitionModel();
        Transition transition = new Transition();
        transition.setName(name);
        transition.setId(id);
        model.setTransition(transition);

        LogManager.displayJSON("doTransition", model);

        subscriptions.add(networkManager.doTransition(currentDraggedIssueKey, model)
                .subscribe(emptyResponse -> {
                }));
    }

    private void getBoardConfiguration() {

        subscriptions.add(networkManager.getSprintState(boardID)
                .subscribe(sprintState ->
                {
                    if (sprintState.getState().equalsIgnoreCase("active")) {
                        subscriptions.add(networkManager.getBoardConfiguration(boardID)
                                .subscribe(boardConfig -> {

                                    subscriptions.add(networkManager.getIssuesForSprint(sprintID)
                                            .subscribe(sprint -> generateBoard(boardConfig, sprint)));
                                }));
                    }else {
                        ((DashboardActivity)getActivity()).showFragment(new BoardSelectionFragment());
                    }

                })

        );

    }

    private void generateBoard(BoardConfiguration boardConfig, Sprint sprint) {

        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Dashboard");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(boardConfig.getName());
        }

        for (Column current : boardConfig.getColumnConfig().getColumns()) {
            addCustomColumnList(current, sprint);
            columnStatusId.add(current.getStatuses().get(0).getId());
            columnStatus.add(current.getName());
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
        menu.clear();
        super.onPrepareOptionsMenu(menu);

        menu.add(0, R.id.action_change_board, 0, R.string.change_board);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_board: {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                getActivity().invalidateOptionsMenu();
                transaction.replace(R.id.container, new BoardSelectionFragment(), "fragment").commit();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private View addCustomColumnList(Column current, Sprint sprint) {
        final ArrayList<Pair<Long, DragCardData>> mItemArray = new ArrayList<>();

        int addItems = 0;
        for (Issue issue : sprint.getIssues()) {
            if (issue.getFields().getStatus().getName().equals(current.getName())) {
                DragCardData information = new DragCardData(issue.getKey(), issue.getFields().getSummary(), issue.getFields().getIssuetype().getName());
                mItemArray.add(new Pair<>(Long.parseLong(issue.getId()), information));
                addItems++;
            }
        }

        final dragItemAdapter listAdapter = new dragItemAdapter(mItemArray, R.layout.test_column_item, R.id.item_layout, true, issueTypeIcons);
        final View header = View.inflate(getActivity(), R.layout.test_column_header, null);
        ((TextView) header.findViewById(R.id.text)).setText(current.getName());
        ((TextView) header.findViewById(R.id.item_count)).setText("" + addItems);


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
            CharSequence textKey = ((TextView) clickedView.findViewById(R.id.textKey)).getText();
            ((TextView) dragView.findViewById(R.id.text)).setText(text);
            ((TextView) dragView.findViewById(R.id.textKey)).setText(textKey);

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

            TextView key = ButterKnife.findById(dragView, R.id.textKey);
            currentDraggedIssueKey = key.getText().toString();

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        subscriptions.unsubscribe();
    }

}