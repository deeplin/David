package com.david.common.ui;

        import android.databinding.DataBindingUtil;
        import android.databinding.ViewDataBinding;
        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

public abstract class BindingFragment<U extends ViewDataBinding> extends Fragment implements IViewModel {

    protected U binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        init();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    protected abstract int getLayoutId();

    protected abstract void init();
}