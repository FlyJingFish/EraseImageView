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

public class DemoFragment2 extends Fragment {
    private EraseView eraseView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_demo2,container,false);
        eraseView = view.findViewById(R.id.eraseView);
        Button eraseBtn = view.findViewById(R.id.btn_erase);
        eraseBtn.setOnClickListener(view1 -> {
            eraseView.setEraseMode(!eraseView.isEraseMode());
            eraseBtn.setText(eraseView.isEraseMode()?"擦除模式":"非擦除模式");
            if (!eraseView.isEraseMode()){
                eraseView.resetErasePath();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
