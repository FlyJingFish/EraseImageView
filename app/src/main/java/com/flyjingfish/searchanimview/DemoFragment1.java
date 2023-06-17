package com.flyjingfish.searchanimview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.flyjingfish.searchanimviewlib.DrawPathType;
import com.flyjingfish.searchanimviewlib.EraseImageView;

public class DemoFragment1 extends Fragment {
    private EraseImageView eraseImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_demo1,container,false);
        eraseImageView = view.findViewById(R.id.eraseView);
        Button eraseBtn = view.findViewById(R.id.btn_erase);
        Button repeatModeBtn = view.findViewById(R.id.btn_repeatMode);
        Spinner spinner = view.findViewById(R.id.spinner);
        eraseBtn.setOnClickListener(view1 -> {
            eraseImageView.setEraseMode(!eraseImageView.isEraseMode());
            eraseBtn.setText(eraseImageView.isEraseMode()?"擦除模式":"非擦除模式");
        });
        repeatModeBtn.setOnClickListener(view1 -> {
            eraseImageView.stopEraseAnim();
            if (eraseImageView.getRepeatMode() == EraseImageView.RESTART){
                eraseImageView.setRepeatMode(EraseImageView.REVERSE);
            }else {
                eraseImageView.setRepeatMode(EraseImageView.RESTART);
            }
            repeatModeBtn.setText(eraseImageView.getRepeatMode() == EraseImageView.RESTART?"RESTART":"REVERSE");
            eraseImageView.startEraseAnim();
        });

        spinner.setAdapter(new ArrayAdapter<String>(requireContext(),android.R.layout.simple_list_item_multiple_choice,mDrawPathTypes));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                eraseImageView.stopEraseAnim();
                switch (position){
                    case 0:
                        eraseImageView.setDrawPathType(DrawPathType.Circle);
                        break;
                    case 1:
                        eraseImageView.setDrawPathType(DrawPathType.Read);
                        break;
                    case 2:
                        eraseImageView.setDrawPathType(DrawPathType.Serpentine);
                        break;
                    case 3:
                        eraseImageView.setDrawPathType(DrawPathType.Lightning);
                        break;
                }
                eraseImageView.startEraseAnim();
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
