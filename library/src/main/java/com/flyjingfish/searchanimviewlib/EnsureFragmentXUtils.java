package com.flyjingfish.searchanimviewlib;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.savedstate.SavedStateRegistryOwner;

class EnsureFragmentXUtils {

    static EnsureFragmentX ensureInFragmentX(@Nullable View view) {
        if (view == null) {
            return new EnsureFragmentX(null,false);
        } else if (view.getParent() instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (isInFragment(viewGroup)) {
                Object object1 = viewGroup.getTag(androidx.lifecycle.runtime.R.id.view_tree_lifecycle_owner);
                return new EnsureFragmentX((LifecycleOwner) object1,true);
            } else {
                return ensureInFragmentX(viewGroup);
            }
        } else {
            return new EnsureFragmentX(null,false);
        }
    }

    private static boolean isInFragment(View view){
        Object object1 = view.getTag(androidx.lifecycle.runtime.R.id.view_tree_lifecycle_owner);
        Object object2 = view.getTag(androidx.lifecycle.viewmodel.R.id.view_tree_view_model_store_owner);
        Object object3 = view.getTag(androidx.savedstate.R.id.view_tree_saved_state_registry_owner);
        return object1 instanceof LifecycleOwner && object2 instanceof ViewModelStoreOwner && object3 instanceof SavedStateRegistryOwner && !(object1 instanceof Activity);
    }
}
