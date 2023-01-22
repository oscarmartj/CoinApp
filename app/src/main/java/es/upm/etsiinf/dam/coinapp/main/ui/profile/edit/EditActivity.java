package es.upm.etsiinf.dam.coinapp.main.ui.profile.edit;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import es.upm.etsiinf.dam.coinapp.main.ui.profile.ProfileViewModel;
import es.upm.etsiinf.dam.coinapp.main.ui.profile.ProfileViewModelFactory;
import es.upm.etsiinf.dam.coinapp.modelos.User;
import es.upm.etsiinf.dam.coinapp.utils.DataManager;
import es.upm.etsiinf.dam.coinapp.utils.ImageManager;
import es.upm.etsiinf.dam.coinapp.utils.Usernames;

public class EditActivity extends AppCompatActivity {

    private UserDB userDB;

    private String usuarioFinal;
    private String emailFinal;
    private Drawable imageFinal;
    private ImageManager im;

    private ActivityResultLauncher<Intent> launcher;
    private ActivityResultLauncher<Intent> launcherCamera;

    private Uri cam_uri;
    private boolean changePhoto=false;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        im = new ImageManager();
        userDB = new UserDB(this);

        setFlagNoEdit(false); //de inicio la foto no esta cambiada

        Context contextapp = getApplicationContext();
        SharedPreferences sp = this.getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
        ProfileViewModel profileViewModel = new ViewModelProvider(this,new ProfileViewModelFactory(contextapp,sp)).get(ProfileViewModel.class);

        TextInputEditText user = findViewById(R.id.tiet_user_edit);
        TextInputEditText email = findViewById(R.id.tiet_email_edit);
        TextInputLayout user_layout = findViewById(R.id.til_user_edit);
        TextInputLayout email_layout = findViewById(R.id.til_email_edit);

        ShapeableImageView ip_profile = findViewById(R.id.iv_circle_profile_edit);

        usuarioFinal = getIntent().getStringExtra("username");
        emailFinal = getIntent().getStringExtra("email");
        Drawable imageProfile = im.getDrawableFromByte(getIntent().getByteArrayExtra("imageProfile"));

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

        //RESULTS FOR CHOOSING AND TAKE PHOTO

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri selectedImageUri = result.getData().getData();
                Bitmap selectedImageBitmap = null;
                try {
                    selectedImageBitmap
                            = MediaStore.Images.Media.getBitmap(
                            this.getContentResolver(),
                            selectedImageUri);
                    selectedImageBitmap = im.zoomFace(selectedImageBitmap);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }finally{
                    if(selectedImageBitmap != null){
                        try {
                            imageFinal = im.getDrawableFromByte(im.getBytesFromBitmap(selectedImageBitmap));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try {

                        setFlagNoEdit(true);
                        ip_profile.setImageDrawable(im.getDrawableFromByte(
                                im.getBytesFromBitmap(
                                        BitmapFactory.decodeFile(
                                                im.profileImageWithFilter(
                                                        EditActivity.this,selectedImageBitmap,usuarioFinal)
                                                        .getAbsolutePath()))));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        launcherCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Bitmap selectedImageBitmap = null;
                try {
                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), cam_uri);
                    selectedImageBitmap = im.zoomFace(selectedImageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(selectedImageBitmap != null){
                        try {
                            imageFinal = im.getDrawableFromByte(im.getBytesFromBitmap(selectedImageBitmap));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        setFlagNoEdit(true);
                        ip_profile.setImageDrawable(im.getDrawableFromByte(
                                im.getBytesFromBitmap(
                                        BitmapFactory.decodeFile(
                                                im.profileImageWithFilter(
                                                                EditActivity.this,selectedImageBitmap,usuarioFinal)
                                                        .getAbsolutePath()))));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        });


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
                            !userDB.getUserByUsername(user.getText().toString()).getEmail().equals(getIntent().getStringExtra("email"))

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
                            !userDB.getUserByEmail(email.getText().toString()).getUsername().equals(getIntent().getStringExtra("username"))
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


        ip_profile.setOnClickListener(view -> {
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
                            takePhoto(launcherCamera);
                        }

                       bottomSheetDialog.dismiss();
                    }else{
                        if (ContextCompat.checkSelfPermission(EditActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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
                           chooseExisting(launcher);
                        }
                        bottomSheetDialog.dismiss();
                    }
                }
            });


        });



        MaterialToolbar toolbar = findViewById(R.id.topAppBar_editprofile);

        toolbar.setNavigationOnClickListener(view -> {
            setFlagNoEdit(false);
            Intent resultIntent = new Intent();
            try {
                resultIntent.putExtra("imageProfileNoEdit",im.getBytesFromBitmap(im.getBitmapFromDrawable(imageProfile)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            setResult(RESULT_CANCELED,resultIntent);
            finish();
        });

        toolbar.setOnMenuItemClickListener(menuItem -> {
            if(menuItem.getItemId() == R.id.editprofile_menu_option) {
                if(user_layout.getError() != null || email_layout.getError() != null){
                    Toast.makeText(this, "Changes could not be saved.", Toast.LENGTH_SHORT).show();
                    setFlagNoEdit(false);
                    Intent resultIntent = new Intent();
                    try {
                        resultIntent.putExtra("imageProfileNoEdit",im.getBytesFromBitmap(im.getBitmapFromDrawable(imageProfile)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    setResult(RESULT_CANCELED,resultIntent);
                    finish();
                }else{
                    User user_to_db;
                    try {
                        //si ha cambiado la foto, mete la nuevai imagen
                        if(DataManager.getFlagNoEdit(this)){
                            user_to_db = new User(usuarioFinal,emailFinal,im.getBytesFromBitmap(im.getBitmapFromDrawable(imageFinal)));
                        //si no ha cambiado la foto, mete la antigua imagen sin el filtro gris ni el simbolo de la camara
                        }else{
                            user_to_db = new User(usuarioFinal,emailFinal,im.getBytesFromBitmap(im.getBitmapFromDrawable(imageProfile)));
                        }
                        
                        String last_email = getIntent().getStringExtra("email");
                        userDB.updateUser(user_to_db,last_email);
                        //Actualizar sharedPreferences
                        Usernames.updateUserPreferences(EditActivity.this,user_to_db);
                        profileViewModel.setChanges(user_to_db);

                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("username",user_to_db.getUsername());
                        resultIntent.putExtra("email",user_to_db.getEmail());
                        resultIntent.putExtra("imageProfile",user_to_db.getProfileImage());
                        resultIntent.putExtra("imageProfileNoEdit",im.getBytesFromBitmap(im.getBitmapFromDrawable(imageProfile)));
                        Toast.makeText(this, "Changes saved.", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
            return false;
        });
    }



    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case DataManager.CAMERA_PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto(launcherCamera);
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
                    chooseExisting(launcher);
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

    private void takePhoto(ActivityResultLauncher<Intent> launcher){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, usuarioFinal+"_CoinAppProfile");
        cam_uri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, cam_uri);

        launcher.launch(takePictureIntent);

    }

    private void chooseExisting (ActivityResultLauncher<Intent> launcher){

        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_PICK);
        launcher.launch(i);

    }

    private void setFlagNoEdit (boolean flag) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("PHOTO_NO_EDIT",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("flag",flag);
        editor.apply();
    }

}