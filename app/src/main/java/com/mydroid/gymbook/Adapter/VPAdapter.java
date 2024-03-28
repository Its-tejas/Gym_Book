package com.mydroid.gymbook.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.mydroid.gymbook.Fragments.AllMemberFragment;
import com.mydroid.gymbook.Fragments.ExpiredMemberFragment;

public class VPAdapter extends FragmentStateAdapter {
    public VPAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new AllMemberFragment();
            case 1:
                return new ExpiredMemberFragment();
            default:
                return new AllMemberFragment(); // Default fragment
        }
    }
    @Override
    public int getItemCount() {
        return 2;
    }

}
