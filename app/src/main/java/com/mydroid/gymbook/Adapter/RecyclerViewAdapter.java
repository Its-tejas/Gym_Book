package com.mydroid.gymbook.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
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
import com.mydroid.gymbook.Model.AdapterModel;
import com.mydroid.gymbook.Model.Users;
import com.mydroid.gymbook.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter <RecyclerViewAdapter.ViewHolder> {
    Context context;
    ArrayList<AdapterModel> arrayBook;
    DBHelper helper;

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
        Picasso.get().load(model.pic).placeholder(R.drawable.baseline_person_24).into(holder.pic);
        holder.name.setText(model.name);
        holder.phone.setText(model.phone);
        holder.amount.setText(model.amount);
        holder.date.setText(model.date);
//        holder.cardForColor.setCardBackgroundColor(getR);
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

                EditText name = dialog.findViewById(R.id.addName);
                EditText phone = dialog.findViewById(R.id.addPhone);
                EditText amount = dialog.findViewById(R.id.addAmount);
                EditText date = dialog.findViewById(R.id.addDate);

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
                        String member_name, member_Phone, member_amount, member_date;
                        member_id = model.id;
                        member_name = name.getText().toString();
                        member_Phone = phone.getText().toString();
                        member_amount = amount.getText().toString();
                        member_date = date.getText().toString();
                        if (!member_name.equals("") && !member_Phone.equals("") && !member_amount.equals("") && !member_date.equals("")) {
                            int res = helper.updateMember(member_id, member_name, member_Phone, member_amount, member_date);
                            if (res == 1) {
                                Toast.makeText(context, "Member updated successfully", Toast.LENGTH_SHORT).show();
                                updateRecyclerViewData();
                                dialog.dismiss();
                            }
                        } else {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
                                helper.deleteMember(model.id);
                                updateRecyclerViewData();
                                Toast.makeText(context, "Member removed Successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;        // the event has been handled and should not be carried further.
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
            // Fallback implementation using SimpleDateFormat and Calendar for older Android versions
            // Add your implementation here
            return false; // Default fallback value
        }
    }
}
