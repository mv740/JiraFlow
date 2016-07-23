package ca.michalwozniak.jiraflow.MVP;

/**
 * Created by Michal Wozniak on 7/23/2016.
 */
public interface IPresenter<V> {

    void attachView(V view);

    void detachView();
}
