package com.ezzat.doctoruim.View.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dpro.widgets.WeekdaysPicker;
import com.ezzat.doctoruim.Control.DatabaseController;
import com.ezzat.doctoruim.Control.Utils.Utils;
import com.ezzat.doctoruim.Control.onEvent;
import com.ezzat.doctoruim.Model.Doctor;
import com.ezzat.doctoruim.Model.Reservation;
import com.ezzat.doctoruim.Model.User;
import com.ezzat.doctoruim.R;
import com.ezzat.doctoruim.View.Doctor.AddClinicActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.StorageReference;
import com.pepperonas.materialdialog.MaterialDialog;

import java.util.ArrayList;
import java.util.Calendar;

import static com.ezzat.doctoruim.Control.Utils.Constants.ARG_USR;
import static com.ezzat.doctoruim.Control.Utils.Constants.DOCTOR_TABLE;
import static com.ezzat.doctoruim.Control.Utils.Constants.PLACEHOLDER_IMG;
import static com.ezzat.doctoruim.Control.Utils.Utils.getImage;

public class DoctorDetailsActivity extends AppCompatActivity {

    private ImageView photo;
    private TextView name, spec, phone, address;
    private RatingBar rate;
    private Button reserve;
    private FloatingActionButton ratebt;
    private User currentUser = null;
    private Doctor currentDoctor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        photo = findViewById(R.id.image);

        name = findViewById(R.id.nameTV);
        spec = findViewById(R.id.specTV);
        phone = findViewById(R.id.phoneTV);
        address = findViewById(R.id.addressTV);
        rate = findViewById(R.id.rate);

        reserve = findViewById(R.id.res);
        ratebt = findViewById(R.id.ratebt);

        currentUser = (User) getIntent().getExtras().getSerializable(ARG_USR);
        name.setText(currentUser.getName());
        address.setText(currentUser.getAddress());
        phone.setText(currentUser.getPhone());
        try {
            Glide.with(DoctorDetailsActivity.this).load(currentUser.getImage()).placeholder(getImage(DoctorDetailsActivity.this, PLACEHOLDER_IMG)).into(photo);
        } catch (Exception e) {
            Glide.with(DoctorDetailsActivity.this).load(getImage(DoctorDetailsActivity.this, PLACEHOLDER_IMG)).into(photo);
        }
        DatabaseController.getElement(DOCTOR_TABLE, currentUser.getPhone(), Doctor.class, new onEvent() {
            @Override
            public void onStart(Object object) {
                Utils.showLoading(DoctorDetailsActivity.this);
            }

            @Override
            public void onProgress(Object object) {

            }

            @Override
            public void onEnd(Object object) {
                Utils.hideDialog();
                currentDoctor = (Doctor) object;
                spec.setText(Utils.getStrings(currentDoctor.getSps()));
                rate.setRating(Float.parseFloat(currentDoctor.getRate()));
            }
        });

        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReservationDialog();
            }
        });

        ratebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRateDialog();
            }
        });
    }

    private void showRateDialog() {
        new MaterialDialog.Builder(this)
                .title("Rate Doctor")
                .customView(R.layout.view_rate)
                .positiveText("OK")
                .negativeText("CANCEL")
                .positiveColor(R.color.green)
                .negativeColor(R.color.red)
                .buttonCallback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        RatingBar rateBar = dialog.findViewById(R.id.rate);
                        int total = currentDoctor.getNum();
                        float oldRate = Float.parseFloat(currentDoctor.getRate());
                        float newRate = ((oldRate * total) + rateBar.getRating()) / (total + 1);
                        rateBar.setRating(newRate);
                        currentDoctor.setRate(newRate + "");
                        currentDoctor.setNum(currentDoctor.getNum() + 1);
                        currentDoctor.updateDoctor();
                    }
                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                        super.onNeutral(dialog);
                        Toast.makeText(DoctorDetailsActivity.this, "Not now", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                    }
                })
                .show();
    }

    private void showReservationDialog() {
        new MaterialDialog.Builder(this)
                .title("Reserve an apointment")
                .customView(R.layout.view_reserve)
                .positiveText("OK")
                .negativeText("CANCEL")
                .positiveColor(R.color.green)
                .negativeColor(R.color.red)
                .showListener(new MaterialDialog.ShowListener() {
                    @Override
                    public void onShow(AlertDialog dialog) {
                        super.onShow(dialog);
                        TextView selectTime = dialog.findViewById(R.id.select_time);
                        Button time = dialog.findViewById(R.id.time);
                        TextView selectDay = dialog.findViewById(R.id.select_day);
                        Button day = dialog.findViewById(R.id.day);
                        setTimeListner(time, selectTime);
                        setDateListner(day, selectDay);
                    }
                })
                .buttonCallback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        boolean pass = true;
                        TextView selectDay = dialog.findViewById(R.id.select_day);
                        TextView selectTime = dialog.findViewById(R.id.select_time);
                        String t = selectTime.getText().toString();
                        String d = selectDay.getText().toString();

                        if (TextUtils.isEmpty(t)) {
                            Utils.showError(DoctorDetailsActivity.this, "Selected Time", "Please Select a time");
                            pass &= false;
                        }

                        if (TextUtils.isEmpty(d)) {
                            Utils.showError(DoctorDetailsActivity.this, "Selected Day", "Please Select a day");
                            pass &= false;
                        }

                        if (pass) {
                            Reservation reservation = new Reservation(Utils.getPhone(), currentDoctor.getPhone(), d +" " + t);
                            reservation.addReservation();
                        }
                    }
                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                        super.onNeutral(dialog);
                        Toast.makeText(DoctorDetailsActivity.this, "Not now", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                    }
                })
                .show();
    }

    private void setTimeListner(Button time, final TextView textV) {
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(DoctorDetailsActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                textV.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
    }

    private void setDateListner(Button button, final TextView textV) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(DoctorDetailsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                textV.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }
}
