package ca.michalwozniak.jiraflow.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import ca.michalwozniak.jiraflow.R;


public class AssignedIssuesFragment extends Fragment {

    private Unbinder unbinder;

    public AssignedIssuesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_stream, container, false);
        unbinder = ButterKnife.bind(this,view);

        return  view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
