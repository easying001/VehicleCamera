package Views;

import android.app.Activity;

/**
 * Created by think on 2016/9/26.
 */

public interface MvpBaseView extends MvpView {

    Activity getParentActivity();

    void showToast(String content);

}
