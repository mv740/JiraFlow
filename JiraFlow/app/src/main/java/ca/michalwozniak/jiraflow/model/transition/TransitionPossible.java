package ca.michalwozniak.jiraflow.model.transition;

import java.util.List;

/**
 * Created by Michal Wozniak on 9/5/2016.
 */

public class TransitionPossible {

    private String expand;
    private List<Transition> transitions;

    public String getExpand() {
        return expand;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }
}
