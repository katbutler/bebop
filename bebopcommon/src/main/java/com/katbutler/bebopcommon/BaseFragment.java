package com.katbutler.bebopcommon;



import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

/**
 * Parent for all fragments that use Presenters and Ui design.
 */
public abstract class BaseFragment<T extends Presenter<U>, U extends Ui> extends Fragment {

    private T mPresenter;

    public abstract T createPresenter();

    public abstract U getUi();

    protected BaseFragment() {
        mPresenter = createPresenter();
    }

    /**
     * Presenter will be available after onActivityCreated().
     *
     * @return The presenter associated with this fragment.
     */
    public T getPresenter() {
        return mPresenter;
    }

    //region Life Cycle

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.onUiReady(getUi());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mPresenter.onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onUiDestroy(getUi());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenter.onSaveInstanceState(outState);
    }

    //endregion


    //region Helpers

    @SuppressWarnings("unchecked")
    public final <E extends View> E findViewOnActivity (final int id) {
        return (E) getActivity().findViewById(id);
    }

    @SuppressWarnings("unchecked")
    public final <E extends View> E findViewOnView (final View view, final int id) {
        return (E) view.findViewById(id);
    }

    //endregion
}
