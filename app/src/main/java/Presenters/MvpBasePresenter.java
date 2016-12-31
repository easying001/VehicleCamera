package Presenters;

import android.util.Log;

import java.lang.ref.WeakReference;

import Views.MvpView;

/**
 * Created by think on 2016/9/25.
 */

public class MvpBasePresenter<V extends MvpView> implements MvpPresenter<V> {
    private WeakReference<V> viewRef;

    @Override
    public void attachView(V view) {
        Log.d("UsbBase", "attached View = " + view);
        viewRef = new WeakReference<V>(view);
    }

    @Override
    public void detachView(boolean retainInstance) {
        if (viewRef != null) {
            viewRef.clear();
            viewRef = null;
        }
    }

    public V getView() {
        return viewRef == null ? null : viewRef.get();
    }

    public boolean isViewAttached() {
        return viewRef != null && viewRef.get() != null;
    }

}
