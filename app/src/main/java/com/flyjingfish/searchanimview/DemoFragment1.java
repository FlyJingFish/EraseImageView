package com.flyjingfish.searchanimview;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.flyjingfish.searchanimviewlib.DrawPathType;
import com.flyjingfish.searchanimviewlib.EraseView;

public class DemoFragment1 extends Fragment {
    private EraseView eraseView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_demo1,container,false);
        eraseView = view.findViewById(R.id.eraseView);
        Button eraseBtn = view.findViewById(R.id.btn_erase);
        Button repeatModeBtn = view.findViewById(R.id.btn_repeatMode);
        Spinner spinner = view.findViewById(R.id.spinner);
        eraseBtn.setOnClickListener(view1 -> {
            eraseView.setEraseMode(!eraseView.isEraseMode());
            eraseBtn.setText(eraseView.isEraseMode()?"擦除模式":"非擦除模式");
        });
        repeatModeBtn.setOnClickListener(view1 -> {
            eraseView.stopEraseAnim();
            if (eraseView.getRepeatMode() == EraseView.RESTART){
                eraseView.setRepeatMode(EraseView.REVERSE);
            }else {
                eraseView.setRepeatMode(EraseView.RESTART);
            }
            repeatModeBtn.setText(eraseView.getRepeatMode() == EraseView.RESTART?"RESTART":"REVERSE");
            eraseView.startEraseAnim();
        });

        spinner.setAdapter(new ArrayAdapter<String>(requireContext(),android.R.layout.simple_list_item_multiple_choice,mDrawPathTypes));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                eraseView.stopEraseAnim();
                switch (position){
                    case 0:
                        eraseView.setDrawPathType(DrawPathType.Circle);
                        break;
                    case 1:
                        eraseView.setDrawPathType(DrawPathType.Read);
                        break;
                    case 2:
                        eraseView.setDrawPathType(DrawPathType.Serpentine);
                        break;
                    case 3:
                        eraseView.setDrawPathType(DrawPathType.Lightning);
                        break;
                }
                eraseView.startEraseAnim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setSelection(2);
        spinner.setPrompt("动画样式");
        return view;
    }
    private final String[] mDrawPathTypes = new String[]{
            "Circle","Read","Serpentine","Lightning"
    };
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

}
