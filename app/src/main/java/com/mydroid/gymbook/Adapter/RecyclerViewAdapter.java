package com.mydroid.gymbook.Adapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mydroid.gymbook.DBHelper;
import com.mydroid.gymbook.ImageHandler;
import com.mydroid.gymbook.Model.AdapterModel;
import com.mydroid.gymbook.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter <RecyclerViewAdapter.ViewHolder> {
    Context context;
    ArrayList<AdapterModel> arrayBook;
    DBHelper helper;
    ImageView img;
    TextView date;
    String firebase_member_id = "";

    public RecyclerViewAdapter(Context context, ArrayList<AdapterModel> arrayBook) {
        this.context = context;
        this.arrayBook = arrayBook;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        helper = new DBHelper(context);
        AdapterModel model = arrayBook.get(position);

        String path = model.pic_path;
        Bitmap bitmap = ImageHandler.loadImageFromStorage(path);

        if (bitmap != null) {
            holder.pic.setImageBitmap(bitmap);
        }

//        Picasso.get().load(model.pic_path).placeholder(R.drawable.baseline_person_24).into(holder.pic);
        holder.name.setText(model.name);
        holder.phone.setText(model.phone);
        holder.amount.setText(model.amount);
        holder.date.setText(model.date);
        firebase_member_id = model.firebase_id;

        // Change background color of parent CardView dynamically
        String joiningDate = model.date.toString();
        if (hasCompletedOneMonth(joiningDate)) {
            holder.cardForColor.setCardBackgroundColor(Color.parseColor("#FFA9A9")); // Red for expired
        } else {
            holder.cardForColor.setCardBackgroundColor(Color.parseColor("#D4FFA9")); // green for non expired
        }

        holder.linearTap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context, R.style.CustomDialogTheme);
                dialog.setContentView(R.layout.add_member_view);

                img = dialog.findViewById(R.id.addPic);
                EditText name = dialog.findViewById(R.id.addName);
                EditText phone = dialog.findViewById(R.id.addPhone);
                EditText amount = dialog.findViewById(R.id.addAmount);
                date = dialog.findViewById(R.id.addDate);

                Bitmap bitmap = ImageHandler.loadImageFromStorage(model.pic_path);

                if (bitmap != null ) {
                    img.setImageBitmap(bitmap);
                }else {
                    img.setImageResource(R.drawable.baseline_person_24);
                }
                name.setText(model.name);
                phone.setText(model.phone);
                amount.setText(model.amount);
                date.setText(model.date);

                Button addMember = dialog.findViewById(R.id.addMember);
                addMember.setText("Update");

                addMember.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int member_id;
                        String member_pic_path, member_name, member_Phone, member_amount, member_date;
                        member_id = model.id;
                        member_pic_path = model.pic_path;
                        member_name = name.getText().toString();
                        member_Phone = phone.getText().toString();
                        member_amount = amount.getText().toString();
                        member_date = date.getText().toString();
                        if (!member_name.equals("") && !member_Phone.equals("") && !member_amount.equals("") && !member_date.equals("")) {
                            int res = helper.updateMember(member_id, member_pic_path, member_name, member_Phone, member_amount, member_date, firebase_member_id);
                            if (res == 1) {
//                                Toast.makeText(context, "Member updated successfully", Toast.LENGTH_SHORT).show();
                                updateRecyclerViewData();
                                dialog.dismiss();
                            }
                        } else {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Button renewbtn = dialog.findViewById(R.id.renew);
                renewbtn.setVisibility(View.VISIBLE);
                renewbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        date.setText(dategetter("dd/MM/YYYY"));
                        Toast.makeText(context, "Date renewed", Toast.LENGTH_SHORT).show();
                    }
                });

                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDatePickerDialog();
                    }
                });


                if (hasCompletedOneMonth(date.getText().toString()))
                {
                    Button reminderbtn = dialog.findViewById(R.id.reminder);
                    reminderbtn.setVisibility(View.VISIBLE);

                    reminderbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String ph = phone.getText().toString();
                            String date_extended = date_extender(date.getText().toString());
                            String REMINDER_SMS = "Dear "+name.getText().toString() + " , your GYM membership has expired on " + date_extended
                                    + ". If you want to continue your GYM membership then please renew it";

                            sendSMS(ph, REMINDER_SMS);
                        }
                    });
                }
                dialog.show();
            }
        });

        holder.linearTap.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Delete Member")
                        .setMessage("Are you sure want to delete this member")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                helper.deleteMember(model.id, model.firebase_id);
                                updateRecyclerViewData();
                                Toast.makeText(context, "Member removed Successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;        // the event has been handled and should not be carried further.
            }
        });

        holder.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.full_img_view);
                ImageView fullImg = dialog.findViewById(R.id.fullImg);

                Bitmap bitmap = ImageHandler.loadImageFromStorage(model.pic_path);
                if (bitmap != null ) {
                    fullImg.setImageBitmap(bitmap);
                }else {
                    fullImg.setImageResource(R.drawable.baseline_person_24);
                }
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayBook.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView pic;
        TextView name, date, amount, phone;
        LinearLayout linearTap;
        CardView cardForColor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.pic);
            name = itemView.findViewById(R.id.txtname);
            phone = itemView.findViewById(R.id.txtPhone);
            amount = itemView.findViewById(R.id.txtamount);
            date = itemView.findViewById(R.id.txtdate);
            linearTap = itemView.findViewById(R.id.linearTap);
            cardForColor = itemView.findViewById(R.id.cardForColor);

        }
    }

    public void updateRecyclerViewData() {
        arrayBook.clear();
        arrayBook.addAll(new DBHelper(context).getMember());
        notifyDataSetChanged();
    }

    public static boolean hasCompletedOneMonth(String joiningDate) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate joinDate = LocalDate.parse(joiningDate, formatter);
            LocalDate oneMonthAfterJoining = joinDate.plusMonths(1);
            return LocalDate.now().isAfter(oneMonthAfterJoining);
        } else {
            return false; // Default fallback value
        }
    }

    public String dategetter(String date)
    {
        // Get current date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(date, Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());
        return currentDate;
    }


    private void sendSMS(String ph, String SMS) {
        String phoneNumber = ph; // Replace with the recipient's phone number
        String message = SMS; // Replace with your message

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        Toast.makeText(context, "Reminder SMS sent!", Toast.LENGTH_SHORT).show();
    }

    private void showDatePickerDialog() {
        // Get current date
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return the selected date
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    // Update the EditText with the selected date
                    String selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year1);
                    date.setText(selectedDate);
                }, year, month, day);

        // Show the date picker dialog
        datePickerDialog.show();
    }

    private String date_extender(String original_date) {
        try {
            // Parse the given date string
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse(original_date);

            // Create a Calendar instance and set the parsed date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            // Increase the date by 1 month
            calendar.add(Calendar.MONTH, 1);

            // Format the new date and print it
            Date newDate = calendar.getTime();
            String newDateString = sdf.format(newDate);
            return newDateString;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}

