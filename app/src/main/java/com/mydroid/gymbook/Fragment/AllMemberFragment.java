package com.mydroid.gymbook.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mydroid.gymbook.Adapter.RecyclerViewAdapter;
import com.mydroid.gymbook.DBHelper;
import com.mydroid.gymbook.ImageHandler;
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
    ImageView img;
    String path=null;
    TextView date;
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
        adapter = new RecyclerViewAdapter(getContext(), arrayBook);

        binding.allRecyclerView.setAdapter(adapter);
        binding.allRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext(), R.style.CustomDialogTheme);
                dialog.setContentView(R.layout.add_member_view);

                img = dialog.findViewById(R.id.addPic);
                EditText name = dialog.findViewById(R.id.addName);
                EditText phone = dialog.findViewById(R.id.addPhone);
                EditText amount = dialog.findViewById(R.id.addAmount);
                date = dialog.findViewById(R.id.addDate);

                amount.setText("200");
                date.setText(dategetter("dd/MM/YYYY"));

                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePicture.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivityForResult(takePicture, REQUEST_CAMERA);
                        }
                    }
                });

                Button addMember = dialog.findViewById(R.id.addMember);
                addMember.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String member_pic_path, member_name, member_Phone, member_amount, member_date;
                        member_pic_path = path;
                        member_name = name.getText().toString();
                        member_Phone = phone.getText().toString();
                        member_amount = amount.getText().toString();
                        member_date = date.getText().toString();
                        if (!member_name.equals("") && !member_Phone.equals("") && !member_amount.equals("") && !member_date.equals("")) {
                            int res = helper.addMember(member_pic_path, member_name, member_Phone, member_amount, member_date);
                            if (res == 1) {
                                String WELCOME_SMS = "Hi "+member_name+" thanks for joining and welcome to our GYM , Today is " + member_date +
                                        " and from now you can transform your body ! ";
                                sendSMS(member_Phone,WELCOME_SMS);
//                                Toast.makeText(getContext(), "Member added successfully", Toast.LENGTH_SHORT).show();
                                updateRecyclerViewData();
                                dialog.dismiss();
                            }
                        }
                        else {
                            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDatePickerDialog();
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

                path = ImageHandler.saveImageToInternalStorage(getContext(), imgBitmap);
                // Load the Bitmap from the image path
                Bitmap bitmap = ImageHandler.loadImageFromStorage(path);

                // Set the fetched image Bitmap to the ImageView
                if (bitmap != null) {
                    img.setImageBitmap(bitmap);
                }
            }
        }
    }
    private void sendSMS(String ph, String SMS) {
        String phoneNumber = ph; // Replace with the recipient's phone number
        String message = SMS; // Replace with your message

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        Toast.makeText(getContext(), "Welcome SMS sent!", Toast.LENGTH_SHORT).show();
    }
    private void showDatePickerDialog() {
        // Get current date
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return the selected date
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year1, monthOfYear, dayOfMonth) -> {
                    // Update the EditText with the selected date
                    String selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year1);
                    date.setText(selectedDate);
                }, year, month, day);

        // Show the date picker dialog
        datePickerDialog.show();
    }

}