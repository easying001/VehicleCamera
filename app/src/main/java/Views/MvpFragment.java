package Views;

import android.app.Activity;
import android.os.Bundle;

import Presenters.MvpPresenter;

/**
 * Created by think on 2016/9/25.
 */

public abstract class MvpFragment<P extends MvpPresenter> extends MvpBaseFragment implements MvpBaseView {
    protected P presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
        if (presenter == null) {
            throw new NullPointerException("Presenter is Null");
        }
        presenter.attachView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView(false);
        presenter = null;
    }

    public Activity getParentActivity() {
        return getActivity();
    }

    protected abstract P createPresenter();
}
