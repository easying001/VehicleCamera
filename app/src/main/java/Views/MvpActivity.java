package Views;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import Presenters.MvpBasePresenter;
import Presenters.MvpPresenter;

/**
 * Created by think on 2016/9/26.
 */

public abstract class MvpActivity<P extends MvpPresenter> extends FragmentActivity implements MvpBaseView {
    protected P presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("UsbMain", "MvpActivity.onCreate()");
        presenter = createPresenter();
        if (presenter == null) {
            throw new NullPointerException("Presenter is Null");
        }
        presenter.attachView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("UsbMain", "MvpActivity.onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("UsbMain", "MvpActivity.onStop()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("UsbMain", "MvpActivity.onResume()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("UsbMain", "MvpActivity.onDestroy()");
        presenter.detachView(false);
    }

    @Override
    public Activity getParentActivity() {
        return this;
    }

    public abstract P createPresenter();
}
