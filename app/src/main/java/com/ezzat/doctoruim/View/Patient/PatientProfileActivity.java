package com.ezzat.doctoruim.View.Patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ezzat.doctoruim.Control.DatabaseController;
import com.ezzat.doctoruim.Control.Utils.Utils;
import com.ezzat.doctoruim.Control.onEvent;
import com.ezzat.doctoruim.Model.Doctor;
import com.ezzat.doctoruim.Model.User;
import com.ezzat.doctoruim.R;
import com.ezzat.doctoruim.View.Doctor.DoctorProfileActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.ezzat.doctoruim.Control.Utils.Constants.DOCTOR_TABLE;
import static com.ezzat.doctoruim.Control.Utils.Constants.PLACEHOLDER_IMG;
import static com.ezzat.doctoruim.Control.Utils.Constants.USER_TABLE;
import static com.ezzat.doctoruim.Control.Utils.Utils.getList;
import static com.ezzat.doctoruim.Control.Utils.Utils.getStrings;

public class PatientProfileActivity extends AppCompatActivity {

    private ImageView photo;
    private TextView name, phone, address;
    private LinearLayout nameL, phoneL, addressL;
    private Button save;
    public static final int PICK_IMAGE = 3;
    private User currentUser = null;
    private StorageReference storageRef;
    private Uri uriProfileImage;      // to save  the image  type
    private String profileImageUrl;  // UrL of the  photo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);

        photo = findViewById(R.id.image);

        name = findViewById(R.id.nameTV);
        phone = findViewById(R.id.phoneTV);
        address = findViewById(R.id.addressTV);

        nameL = findViewById(R.id.name);
        phoneL = findViewById(R.id.phone);
        addressL = findViewById(R.id.address);

        save = findViewById(R.id.save);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://doctoruim.appspot.com/");
        final String phone1 = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        DatabaseController.getElement(USER_TABLE, phone1.substring(2, phone1.length()), User.class, new onEvent() {
            @Override
            public void onStart(Object object) {
                Utils.showLoading(PatientProfileActivity.this);
            }

            @Override
            public void onProgress(Object object) {

            }

            @Override
            public void onEnd(Object object) {
                Utils.hideDialog();
                currentUser = (User) object;
                name.setText(currentUser.getName());
                address.setText(currentUser.getAddress());
                phone.setText(currentUser.getPhone());
                try {
                    Glide.with(PatientProfileActivity.this).load(currentUser.getImage()).placeholder(getImage(PLACEHOLDER_IMG)).into(photo);
                } catch (Exception e) {
                    Glide.with(PatientProfileActivity.this).load(getImage(PLACEHOLDER_IMG)).into(photo);
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!attemptSave()){
                    currentUser.setAddress(String.valueOf(address.getText()));
                    currentUser.setName(String.valueOf(name.getText()));
                    currentUser.setPhone(String.valueOf(phone.getText()));
                    currentUser.updateUser();
                    onBackPressed();
                }

            }
        });
        phoneL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewDialog(phone, "phone");
            }
        });
        addressL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewDialog(address, "address");
            }
        });

        nameL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewDialog(name, "name");
            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewImageChooser(photo);
            }
        });
    }

    public int getImage(String imageName) {

        int drawableResourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());

        return drawableResourceId;
    }

    public void viewDialog(final TextView v, final String attribute) {

        SweetAlertDialog alertDialog = new SweetAlertDialog(PatientProfileActivity.this, SweetAlertDialog.NORMAL_TYPE);
        alertDialog.setTitle("Enter New " + attribute);
        alertDialog.setCancelable(true);
        alertDialog.setCancelText("Cancel");
        alertDialog.setConfirmText("Confirm");
        alertDialog.showCancelButton(true);
        final EditText input = new EditText(this);
        alertDialog.setContentText("Enter New " + attribute);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setCustomView(input);

        alertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog dialog) {

                v.setText(input.getText());
                dialog.dismiss();
            }
        });
        alertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void viewImageChooser(final ImageView v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT); // get the image
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), PICK_IMAGE); //save in CHOOSE_IMAGE
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check for the image selected
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();
            try {
                Glide.with(PatientProfileActivity.this).load(uriProfileImage).placeholder(getImage(PLACEHOLDER_IMG)).into(photo);
                uploadImageToFirebaseStorage(); // upload to firebase the picture

            } catch (Exception e) {
                Glide.with(PatientProfileActivity.this).load(getImage(PLACEHOLDER_IMG)).into(photo);
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebaseStorage() {
        // storage
        StorageReference profileImageRef =
                FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + ".jpg"); // in database folder profilepics
        //System.currentTimeMillis(), is random sequence done  by getting time in millis
        if (uriProfileImage != null) { // upload
            Utils.showLoading(this);
            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        // check uplod successful or not
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Utils.hideDialog();
                            Toast.makeText(PatientProfileActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                            profileImageUrl = taskSnapshot.getDownloadUrl().toString(); // get the url as user informations
                            currentUser.setImage(profileImageUrl);
                            currentUser.updateUser();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) { // fail to opload
                            Utils.hideDialog();
                            Toast.makeText(PatientProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private boolean attemptSave() {

        String nameText = String.valueOf(name.getText());
        String phoneText = String.valueOf(phone.getText());
        String addressText = String.valueOf(address.getText());


        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(nameText)) {
            name.setError("Name is required");
            focusView = name;
            cancel = true;
        }
        if (TextUtils.isEmpty(addressText)) {
            address.setError("Address is required");
            focusView = address;
            cancel = true;
        }

        if (TextUtils.isEmpty(phoneText)) {
            phone.setError("Phone is required");
            focusView = phone;
            cancel = true;
        }
        if (phoneText.length() != 11) {
            phone.setError("Phone is Incorrect");
            focusView = phone;
            cancel = true;
        }
        if (!TextUtils.isDigitsOnly(phoneText)) {
            phone.setError("Phone is only digits");
            focusView = phone;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }
        return cancel;
    }
}
