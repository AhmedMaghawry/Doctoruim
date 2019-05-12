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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.ezzat.doctoruim.Control.DatabaseController;
import com.ezzat.doctoruim.Control.Utils.Utils;
import com.ezzat.doctoruim.Control.onEvent;
import com.ezzat.doctoruim.Model.Request;
import com.ezzat.doctoruim.Model.User;
import com.ezzat.doctoruim.R;
import com.ezzat.doctoruim.View.Doctor.AddClinicActivity;
import com.ezzat.doctoruim.View.Doctor.DoctorProfileActivity;
import com.ezzat.doctoruim.View.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static com.ezzat.doctoruim.Control.Utils.Constants.PLACEHOLDER_IMG;
import static com.ezzat.doctoruim.Control.Utils.Constants.USER_TABLE;

public class SettingsActivity extends AppCompatActivity {

    private Button send, delete;
    private ImageView ass;
    private EditText cover;
    boolean edited = false;
    public static final int PICK_IMAGE = 3;
    private Uri uriProfileImage;      // to save  the image  type
    private String profileImageUrl;  // UrL of the  photo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        send = findViewById(R.id.send);
        delete = findViewById(R.id.delete);
        ass = findViewById(R.id.ass);
        cover = findViewById(R.id.cover);

        ass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewImageChooser(ass);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseController.getElement(USER_TABLE, Utils.getPhone(), User.class, new onEvent() {
                    @Override
                    public void onStart(Object object) {
                        Utils.showLoading(SettingsActivity.this);
                    }

                    @Override
                    public void onProgress(Object object) {

                    }

                    @Override
                    public void onEnd(Object object) {
                        Utils.hideDialog();
                        User u = (User) object;
                        u.deleteUser();
                        FirebaseAuth.getInstance().signOut();
                        Utils.launchActivity(SettingsActivity.this, MainActivity.class, null);
                        finish();
                    }
                });
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
            }
        });
    }

    private void sendRequest() {
        final String co = cover.getText().toString();

        View focusView = null;

        Boolean pass = true;

        if (TextUtils.isEmpty(co)) {
            cover.setError("Cover letter is required");
            focusView = cover;
            pass &= false;
        }

        if (!edited) {
            Utils.showError(SettingsActivity.this, "Association Card", "Please add Association card photo");
            focusView = ass;
            pass &= false;
        }

        if (pass) {
            DatabaseController.getElement(USER_TABLE, Utils.getPhone(), User.class, new onEvent() {
                @Override
                public void onStart(Object object) {
                    Utils.showLoading(SettingsActivity.this);
                }

                @Override
                public void onProgress(Object object) {

                }

                @Override
                public void onEnd(Object object) {
                    Utils.hideDialog();
                    User u = (User) object;
                    Request request = new Request(u.getPhone(), co, profileImageUrl);
                    request.addRequest();
                    Utils.launchActivity(SettingsActivity.this, MainActivity.class, null);
                    finish();
                }
            });
        } else
            focusView.requestFocus();
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
            edited = true;
            try {
                Glide.with(SettingsActivity.this).load(uriProfileImage).placeholder(getImage(PLACEHOLDER_IMG)).into(ass);
                uploadImageToFirebaseStorage(); // upload to firebase the picture

            } catch (Exception e) {
                Glide.with(SettingsActivity.this).load(getImage(PLACEHOLDER_IMG)).into(ass);
                e.printStackTrace();
            }
        }
    }

    public int getImage(String imageName) {

        int drawableResourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());

        return drawableResourceId;
    }


    private void uploadImageToFirebaseStorage() {
        // storage
        StorageReference profileImageRef =
                FirebaseStorage.getInstance().getReference("asso/" + System.currentTimeMillis() + ".jpg"); // in database folder profilepics
        //System.currentTimeMillis(), is random sequence done  by getting time in millis
        if (uriProfileImage != null) { // upload
            Utils.showLoading(this);
            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        // check uplod successful or not
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Utils.hideDialog();
                            Toast.makeText(SettingsActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                            profileImageUrl = taskSnapshot.getDownloadUrl().toString(); // get the url as user informations
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) { // fail to opload
                            Utils.hideDialog();
                            Toast.makeText(SettingsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
