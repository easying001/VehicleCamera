package Views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by think on 2016/9/26.
 */

public class MvpBaseFragment extends Fragment {
    protected int fragmentType;

    public void setType(int type) {
        fragmentType = type;
    }

    public int getType() {
        return fragmentType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("UsbFragment", "fragmentType = " + fragmentType + " onCreate()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("UsbFragment", "fragmentType = " + fragmentType + " onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("UsbFragment", "fragmentType = " + fragmentType + " onPause()");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("UsbFragment", "fragmentType = " + fragmentType + " onStart()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("UsbFragment", "fragmentType = " + fragmentType + " onStop()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("UsbFragment", "fragmentType = " + fragmentType + " onDestroy()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("UsbFragment", "fragmentType = " + fragmentType + " onDestroyView()");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d("UsbFragment", "fragmentType = " + fragmentType + " onHiddenChanged(): " + hidden);
    }
}
