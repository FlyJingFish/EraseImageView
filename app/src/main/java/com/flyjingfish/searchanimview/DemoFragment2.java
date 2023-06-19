package com.flyjingfish.searchanimview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.flyjingfish.searchanimviewlib.EraseImageView;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class DemoFragment2 extends Fragment {
    private EraseImageView eraseImageView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_demo2,container,false);
        eraseImageView = view.findViewById(R.id.eraseView);
        Button eraseBtn = view.findViewById(R.id.btn_erase);

        RequestOptions mRequestOptions = new RequestOptions().transform(new BlurTransformation(40));
        Glide.with(this).load(R.drawable.demo2).apply(mRequestOptions).into(eraseImageView);
        eraseBtn.setOnClickListener(view1 -> {
            eraseImageView.setEraseMode(!eraseImageView.isEraseMode());
            eraseBtn.setText(eraseImageView.isEraseMode()?"擦除模式":"非擦除模式");
            if (!eraseImageView.isEraseMode()){
                eraseImageView.resetErasePath();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
