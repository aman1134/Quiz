package com.java.prj.quiz;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.ParcelFileDescriptor;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class FragmentProfile extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextInputEditText name, cno;
    TextView add_img;
    ImageView img;
    MaterialButton edit;
    String str_name, str_img;
    Bitmap final_img;
    int i_img = 0;
    int GET_IMAGE = 786;
    SharedPreferences sharedPreferences;

    public FragmentProfile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MainActivity.title.setText("Profile");
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        img = (ImageView) view.findViewById(R.id.img);
        add_img = (TextView) view.findViewById(R.id.add_image);
        name = (TextInputEditText) view.findViewById(R.id.name);
        cno = (TextInputEditText) view.findViewById(R.id.cno);
        edit = (MaterialButton) view.findViewById(R.id.edit);

        if(sharedPreferences.getString("img", "") != null && !sharedPreferences.getString("img", "").isEmpty())
            img.setImageBitmap(decodeFromFirebaseBase64(sharedPreferences.getString("img", "")));

        if(sharedPreferences.getString("name", "") != null && !sharedPreferences.getString("name", "").isEmpty())
            name.setText(sharedPreferences.getString("name", ""));

        if(sharedPreferences.getString("cno", "") != null && !sharedPreferences.getString("cno", "").isEmpty())
            cno.setText(sharedPreferences.getString("cno", ""));

        name.setEnabled(false);
        cno.setEnabled(false);
        add_img.setVisibility(View.INVISIBLE);

        edit.setOnClickListener(this);

        add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("onclick");
                checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, GET_IMAGE);
            }
        });
        return view;
    }

    public void checkPermission(String permission, int requestCode) {
        System.out.println("chkpermission");
        if (ContextCompat.checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_DENIED) {
            System.out.println("grated");
            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
        } else {
            System.out.println("not granted");
            startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI), GET_IMAGE);
        }
    }

    @Override
    public void onClick(View v) {
        MaterialButton edit = (MaterialButton) v.findViewById(v.getId());

        if (edit.getText().toString().equals("Edit Details")) {
            name.setEnabled(true);

            add_img.setVisibility(View.VISIBLE);
            name.setEnabled(true);

            Drawable icon = getResources().getDrawable(R.drawable.done);
            edit.setIcon(icon);
            edit.setText("Save Details");

        } else {
            Drawable icon = getResources().getDrawable(R.drawable.edit);
            edit.setIcon(icon);
            edit.setText("Edit Details");

            name.setEnabled(false);
            cno.setEnabled(false);

            str_name = name.getText().toString();

            if (str_name.isEmpty())
                name.setError("Please enter your Name");
            else {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/students/" + SplashScreen.user.getUid());
                ref.child("name").setValue(str_name);

                sharedPreferences.edit().putString("name", str_name).apply();
                sharedPreferences.edit().putString("img", str_img).apply();

                if (i_img == 1)
                    saveDataToStorage();

                Toast.makeText(getActivity(), "Deltails are saved", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveDataToStorage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final_img.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://quizapp-2389e.appspot.com");
        StorageReference logoRef =  storageRef.child("users/students/"+ SplashScreen.user.getUid() + "/user.jpg");

        UploadTask uploadTask = logoRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                exception.printStackTrace();
                System.out.println("\n Failure in storage exception = ");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                System.out.println("\n Image has been added to the storage");
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Checking whether user granted the permission or not.
        System.out.println("reqpermission");
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity(), "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI), GET_IMAGE);
        } else {
            Toast.makeText(getActivity(), "Storage Permission Denied", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();

            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            img.setImageBitmap(bmp);
            final_img = bmp;
            str_img = changeBitmapToStrinng(bmp);
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getActivity().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        return image;
    }

    private String changeBitmapToStrinng(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        return imageEncoded;
    }

    public static Bitmap decodeFromFirebaseBase64(String image){
        byte[] decodedByteArray = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

}