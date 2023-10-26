package com.example.gifviewer.presentation.fragments.base

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

open class BaseFragment(@LayoutRes layoutRes: Int) : Fragment(layoutRes) {

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSubscriptions()
    }

    protected open fun initSubscriptions() {
        // Initialize subscriptions for ViewModel's Flows.
    }
}