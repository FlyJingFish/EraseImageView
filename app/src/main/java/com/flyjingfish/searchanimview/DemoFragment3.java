package com.flyjingfish.searchanimview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.flyjingfish.searchanimviewlib.EraseView;

public class DemoFragment3 extends Fragment {
    private EraseView eraseView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_demo3,container,false);
        eraseView = view.findViewById(R.id.eraseView);
        Button resetBtn = view.findViewById(R.id.btn_reset);
        eraseView.setHandMode(true);
        if (resetBtn != null){
            resetBtn.setOnClickListener(view1 -> {
                eraseView.resetErasePath();
            });
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
