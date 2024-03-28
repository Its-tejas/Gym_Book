package com.mydroid.gymbook;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayoutMediator;
import com.mydroid.gymbook.Adapter.VPAdapter;
import com.mydroid.gymbook.Fragments.AllMemberFragment;
import com.mydroid.gymbook.databinding.ActivityContentBinding;

public class ContentActivity extends AppCompatActivity {
    private static final String[] TAB_TITLES = {"All ", "Expired "};
    ActivityContentBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        VPAdapter VP = new VPAdapter(this);
        binding.VPcontainer.setAdapter(VP);

        new TabLayoutMediator(binding.tablayout,binding.VPcontainer,(tab, position) ->  tab.setText(TAB_TITLES[position])
        ).attach();
    }
}