package es.upm.etsiinf.dam.coinapp.main.ui.profile.edit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;

import es.upm.etsiinf.dam.coinapp.R;
import es.upm.etsiinf.dam.coinapp.database.functions.UserDB;
import es.upm.etsiinf.dam.coinapp.databinding.ActivityEditBinding;
import es.upm.etsiinf.dam.coinapp.utils.DataManager;
import es.upm.etsiinf.dam.coinapp.utils.ImageManager;

public class EditActivity extends AppCompatActivity {

    private UserDB userDB;

    private String usuarioFinal;
    private String emailFinal;
    private Drawable imageFinal;
    private ImageManager im;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ImageManager imageManager = new ImageManager();
        userDB = new UserDB(this);

        TextInputEditText user = findViewById(R.id.tiet_user_edit);
        TextInputEditText email = findViewById(R.id.tiet_email_edit);
        TextInputLayout user_layout = findViewById(R.id.til_user_edit);
        TextInputLayout email_layout = findViewById(R.id.til_email_edit);

        ShapeableImageView ip_profile = findViewById(R.id.iv_circle_profile_edit);

        String userText = getIntent().getStringExtra("username");
        usuarioFinal = userText;
        String emailText = getIntent().getStringExtra("email");
        emailFinal = emailText;
        Drawable imageProfile = imageManager.getDrawableFromByte(getIntent().getByteArrayExtra("imageProfile"));

        im = new ImageManager();
        File imageEditable = im.profileImageWithFilter(this,im.getBitmapFromDrawable(imageProfile),usuarioFinal);
        Drawable imageToInsert = null;
        try {
            imageToInsert = im.getDrawableFromByte(im.getBytesFromBitmap(BitmapFactory.decodeFile(imageEditable.getAbsolutePath())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageFinal = imageToInsert;

        user.setText(usuarioFinal);
        email.setText(emailFinal);
        ip_profile.setImageDrawable(imageFinal);


        user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged (CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged (Editable editable) {
                if(DataManager.isUserNameValid(user.getText().toString())){
                    //Si existe el nombre de usuario y tiene un correo diferente al mio => error, ya existe user
                    //Si existe el nombre de usuario y tiene el mismo correo que yo => ok, modifico mi propio nombre
                    if(userDB.countUsersByUsername(user.getText().toString())>0 &&
                            !userDB.getUserByUsername(user.getText().toString()).getEmail().equals(emailFinal)

                    ){
                        user_layout.setError("Already exist user with this username");

                    }else{
                        user_layout.setError(null);
                        usuarioFinal=user.getText().toString();
                    }
                }else{
                    if(user.getText().toString().length()<3){
                        user_layout.setError("Minimum 3 characters.");
                    }else{
                        user_layout.setError("Invalid username format (a-z, A-Z,0-9,._-).");
                    }
                }
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged (CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged (Editable editable) {
                if(DataManager.isEmailValid(email.getText().toString())){
                    if(userDB.countUsersByEmail(email.getText().toString())>0 &&
                            !userDB.getUserByEmail(email.getText().toString()).getUsername().equals(usuarioFinal)
                    ){
                        email_layout.setError("Already exist user with this email");
                    }else{
                        email_layout.setError(null);
                        emailFinal=email.getText().toString();
                    }
                }else{
                    email_layout.setError("Invalid email format.");
                }

            }
        });

        //
        ip_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(EditActivity.this);
                View view_bsd = getLayoutInflater().inflate(R.layout.content_dialog_change_photo, null);
                bottomSheetDialog.setContentView(view_bsd);
                bottomSheetDialog.show();

                ListView listView = view_bsd.findViewById(R.id.lw_choosephoto);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick (AdapterView<?> adapterView, View view, int i, long l) {
                        if(i == 0){
                            if (ContextCompat.checkSelfPermission(EditActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                //ActivityCompat.requestPermissions(EditActivity.this, new String[] { Manifest.permission.CAMERA }, DataManager.CAMERA_PERMISSION_REQUEST_CODE);
                                //PERMISO DENEGADO
                                if(ActivityCompat.shouldShowRequestPermissionRationale(EditActivity.this, Manifest.permission.CAMERA)){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this, R.style.MyAlertDialog);
                                    builder.setTitle("Camera permissions")
                                            .setMessage("Do you want to activate camera permissions to be able to take a photo for your profile?")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    ActivityCompat.requestPermissions(EditActivity.this, new String[] { Manifest.permission.CAMERA }, DataManager.CAMERA_PERMISSION_REQUEST_CODE);
                                                }
                                            })
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick (DialogInterface dialogInterface, int i) {

                                                }
                                            });
                                    builder.create().show();
                                }else{
                                    ActivityCompat.requestPermissions(EditActivity.this, new String[] { Manifest.permission.CAMERA }, DataManager.CAMERA_PERMISSION_REQUEST_CODE);
                                }
                            }else{
                                takePhoto();
                            }

                           bottomSheetDialog.dismiss();
                        }else{
                            if (ContextCompat.checkSelfPermission(EditActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                //PERMISO DENEGADO
                                if(ActivityCompat.shouldShowRequestPermissionRationale(EditActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this, R.style.MyAlertDialog);
                                    builder.setTitle("Gallery permissions")
                                            .setMessage("Would you like to grant access to your gallery to choose a photo for your profile?")
                                            .setPositiveButton("Go to app info", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    ActivityCompat.requestPermissions(EditActivity.this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, DataManager.READ_EXTERNAL_STORAGE_REQUEST_CODE);
                                                }
                                            })
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick (DialogInterface dialogInterface, int i) {

                                                }
                                            });
                                    builder.create().show();
                                }else{
                                    ActivityCompat.requestPermissions(EditActivity.this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, DataManager.READ_EXTERNAL_STORAGE_REQUEST_CODE);
                                }
                            }else{
                                chooseExisting();
                            }
                            bottomSheetDialog.dismiss();
                        }
                    }
                });


            }
        });



        MaterialToolbar toolbar = findViewById(R.id.topAppBar_editprofile);

        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });

        toolbar.setOnMenuItemClickListener(menuItem -> {
            if(menuItem.getItemId() == R.id.editprofile_menu_option) {
                Toast.makeText(this, " Icon Clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }

    private void takePhoto(){
        Toast.makeText(this, " option 0", Toast.LENGTH_SHORT).show();
    }

    private void chooseExisting(){
        Toast.makeText(this, " option 1", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case DataManager.CAMERA_PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this, R.style.MyAlertDialog);
                    builder.setTitle("Camera permissions")
                            .setMessage("Do you want to activate camera permissions to be able to take a photo for your profile?")
                            .setPositiveButton("Go to app info", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick (DialogInterface dialogInterface, int i) {
                                    Toast.makeText(EditActivity.this, "Permission denied, you can't take a photo.", Toast.LENGTH_SHORT).show();
                                }
                            });
                    builder.create().show();
                }
                break;
            case DataManager.READ_EXTERNAL_STORAGE_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseExisting();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this, R.style.MyAlertDialog);
                    builder.setTitle("Gallery permissions")
                            .setMessage("Would you like to grant access to your gallery to choose a photo for your profile?")
                            .setPositiveButton("Go to app info", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick (DialogInterface dialogInterface, int i) {
                                    Toast.makeText(EditActivity.this, "Permission denied, you can't choose a photo from gallery.", Toast.LENGTH_SHORT).show();
                                }
                            });
                    builder.create().show();
                }
                break;
        }
    }
}