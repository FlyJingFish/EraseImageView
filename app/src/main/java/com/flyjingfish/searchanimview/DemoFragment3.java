package com.flyjingfish.searchanimview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.flyjingfish.searchanimviewlib.EraseImageView;
import com.flyjingfish.searchanimviewlib.ScratchCardLayout;

public class DemoFragment3 extends Fragment {
    private EraseImageView eraseImageView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_demo3,container,false);
        eraseImageView = view.findViewById(R.id.eraseView);
        Button resetBtn = view.findViewById(R.id.btn_reset);
        CheckBox visibleBtn = view.findViewById(R.id.btn_visible);
        TextView giveTv = view.findViewById(R.id.tv_give);
        ScratchCardLayout scratchCardLayout = view.findViewById(R.id.fl_give);
        eraseImageView.setHandMode(true);
        if (resetBtn != null){
            resetBtn.setOnClickListener(view1 -> {
                eraseImageView.resetErasePath();
            });
        }
        visibleBtn.setOnCheckedChangeListener((buttonView, isChecked) -> scratchCardLayout.setEraseAllAreaAfterScratchOff(isChecked));
        scratchCardLayout.setScratchView(giveTv);
        scratchCardLayout.setOnScratchListener(new ScratchCardLayout.OnScratchListener() {
            @Override
            public void onScratchOff() {
                Toast.makeText(requireContext(),"擦开了",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
