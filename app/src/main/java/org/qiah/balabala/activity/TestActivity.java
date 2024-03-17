package org.qiah.balabala.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.qiah.balabala.databinding.Activity1Binding;

public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity1Binding view = Activity1Binding.inflate(getLayoutInflater());
        setContentView(view.getRoot());
    }
}
