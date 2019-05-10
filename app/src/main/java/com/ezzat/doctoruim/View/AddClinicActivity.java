package com.ezzat.doctoruim.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.dpro.widgets.WeekdaysPicker;
import com.ezzat.doctoruim.Control.Utils.Utils;
import com.ezzat.doctoruim.Model.Clinic;
import com.ezzat.doctoruim.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

import static com.ezzat.doctoruim.Control.Utils.Constants.CLINIC_TABLE;

public class AddClinicActivity extends AppCompatActivity {

    private EditText clinic_name, phone1, phone2, address;
    private Button create;
    private FloatingActionButton startTime, endTime;
    private TextView startTimeTV, endTimeTV;
    private AddClinicActivity self;
    private WeekdaysPicker weekdaysPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clinic);

        clinic_name = findViewById(R.id.clinic_name);
        phone1 = findViewById(R.id.clinic_phone);
        phone2 = findViewById(R.id.clinic_phone_);
        address = findViewById(R.id.clinic_address);
        weekdaysPicker = findViewById(R.id.weekdays);
        weekdaysPicker.setSelectedDays(new ArrayList<Integer>());
        create = findViewById(R.id.create);
        self = this;
        startTime = findViewById(R.id.time);
        endTime = findViewById(R.id.time_);
        startTimeTV = findViewById(R.id.start);
        endTimeTV = findViewById(R.id.end);
        setTimeListner(startTime, startTimeTV);
        setTimeListner(endTime, endTimeTV);
        create.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    createClinic();
                }
            }
        });
    }

    private void setTimeListner(FloatingActionButton time, final TextView textV) {
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddClinicActivity.this,
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

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void createClinic() {
        String name = clinic_name.getText().toString();
        String phone_1 = phone1.getText().toString();
        String phone_2 = phone2.getText().toString();
        String addressT = address.getText().toString();
        String startTime = startTimeTV.getText().toString();
        String endTime = endTimeTV.getText().toString();

        View focusView = null;

        Boolean pass = true;

        if (TextUtils.isEmpty(name)) {
            clinic_name.setError("Name is required");
            focusView = clinic_name;
            pass &= false;
        }

        if (TextUtils.isEmpty(startTime)) {
            startTimeTV.setError("Please Select Start Time");
            focusView = startTimeTV;
            pass &= false;
        }

        if (TextUtils.isEmpty(endTime)) {
            endTimeTV.setError("Please Select End Time");
            focusView = endTimeTV;
            pass &= false;
        }

        if (weekdaysPicker.noDaySelected()) {
            Utils.showError(AddClinicActivity.this, "Selected Days", "Please Selecet at least 1 day of week");
            focusView = weekdaysPicker;
            pass &= false;
        }

        if (pass) {

            ArrayList<String> phones = new ArrayList();
            phones.add(phone_1);
            phones.add(phone_2);

            ArrayList<String> doctors = new ArrayList<>();
            String phone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
            doctors.add(phone.substring(2, phone.length()));

            DatabaseReference base = FirebaseDatabase.getInstance().getReference(CLINIC_TABLE);
            DatabaseReference d = base.push();

            Clinic clinic = new Clinic(d.getKey(), name, addressT, phones, doctors);
            clinic.setDays(weekdaysPicker.getSelectedDaysText());
            clinic.setStartTime(startTime);
            clinic.setEndTime(endTime);
            clinic.addClinic();
            Utils.launchActivity(AddClinicActivity.this, HomeDoctorActivity.class, null);
        } else {
            focusView.requestFocus();
        }
    }
}
