package pers.victor.androiddemos.fragment;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;


public class BaseFragment extends Fragment {
    protected Context mContext;
    protected Activity mActivity;

    @Override
    public void onAttach(Context context) {
        setHasOptionsMenu(true);
        mContext = context;
        mActivity = (Activity) context;
        super.onAttach(context);
    }
}
