package com.mydroid.gymbook.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mydroid.gymbook.Adapter.RecyclerViewAdapter;
import com.mydroid.gymbook.DBHelper;
import com.mydroid.gymbook.Model.AdapterModel;
import com.mydroid.gymbook.R;
import com.mydroid.gymbook.databinding.FragmentAllMemberBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AllMemberFragment extends Fragment {
    DBHelper helper;
    ArrayList<AdapterModel> arrayBook;
    RecyclerViewAdapter adapter;
    int REQUEST_CAMERA = 101;
    ImageView pic;
    AdapterModel model;

    FragmentAllMemberBinding binding;
    public AllMemberFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        binding = FragmentAllMemberBinding.inflate(inflater, container, false);
        helper = new DBHelper(getContext());
        arrayBook = helper.getMember();
        adapter = new RecyclerViewAdapter(getContext(),arrayBook);

        binding.allRecyclerView.setAdapter(adapter);
        binding.allRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext(), R.style.CustomDialogTheme);
                dialog.setContentView(R.layout.add_member_view);

                pic = dialog.findViewById(R.id.addPic);
                EditText name = dialog.findViewById(R.id.addName);
                EditText phone = dialog.findViewById(R.id.addPhone);
                EditText amount = dialog.findViewById(R.id.addAmount);
                EditText date = dialog.findViewById(R.id.addDate);

                amount.setText("200");
                date.setText(dategetter("dd/MM/YYYY"));

                pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setView(R.layout.image_option_view);

                        AlertDialog dialog1 = builder.create();
//                        dialog1.getWindow().setGravity(Gravity.BOTTOM);
                        Button btnCamera = dialog1.findViewById(R.id.btnCamera);
                        Button btnGallery = dialog1.findViewById(R.id.btnGallery);

                        btnCamera.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (takePicture.resolveActivity(getActivity().getPackageManager()) != null)
                                {
                                    startActivityForResult(takePicture, REQUEST_CAMERA);
                                }
                            }
                        });
                    }
                });

                Button addMember = dialog.findViewById(R.id.addMember);
                addMember.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String member_name, member_Phone, member_amount, member_date;
                        member_name = name.getText().toString();
                        member_Phone = phone.getText().toString();
                        member_amount = amount.getText().toString();
                        member_date = date.getText().toString();
                        if (!member_name.equals("") && !member_Phone.equals("") && !member_amount.equals("") && !member_date.equals("")) {
                            int res = helper.addMember(member_name, member_Phone, member_amount, member_date);
                            if (res == 1) {
                                Toast.makeText(getContext(), "Member added successfully", Toast.LENGTH_SHORT).show();
                                updateRecyclerViewData();
                                dialog.dismiss();
                            }
                        }
                        else {
                            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    public void updateRecyclerViewData() {
        arrayBook.clear();
        arrayBook.addAll(new DBHelper(getContext()).getMember());
        adapter.notifyDataSetChanged();
    }

    public String dategetter(String date)
    {
        // Get current date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(date, Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());
        return currentDate;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA && data != null) {
                Bundle extras = data.getExtras();
                Bitmap imgBitmap = (Bitmap) extras.get("data");
                model.setPic(imgBitmap.toString());
                pic.setImageBitmap(imgBitmap);
                // store it into internal storage
            }
        }
    }
}