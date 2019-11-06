package com.example.queingsystem.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.queingsystem.Model_User;
import com.example.queingsystem.R;
import com.example.queingsystem.RequestHandler;
import com.example.queingsystem.SharedPrefManager;
import com.example.queingsystem.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class Register extends AppCompatActivity {
    EditText editTextUsername, editTextEmail, editTextPassword, editTextCPassword, editImageValue;
    TextView textViewLogin;
    ImageView imgView;
    public  static final int RequestPermissionCode  = 1 ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        imgView = findViewById(R.id.imageView);
        editImageValue = findViewById(R.id.editImageValue);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextCPassword = findViewById(R.id.editTextCPassword);
        textViewLogin = findViewById(R.id.textViewLogin);
        textViewLogin.setText("Already Registered? \n Login Here");
        EnableRuntimePermission();

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(capture, 7);
            }
        });
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });


//        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
//            //finish();
//            Model_User user = SharedPrefManager.getInstance(this).getUser();
//            if(user.getRole().equals("Admin")){
//                Intent intent = new Intent(getApplicationContext(), Admin_Handler.class);
//                startActivity(intent);
//            }else if(user.getRole().equals("Driver")) {
//                Intent intent = new Intent(getApplicationContext(), Driver_Handler.class);
//                startActivity(intent);
//            }else{
//                Intent intent = new Intent(getApplicationContext(), User_Handler.class);
//                startActivity(intent);
//            }
//        }

        findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        final String username = editTextUsername.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String cpassword = editTextCPassword.getText().toString().trim();
        final String role = "User";

        //first we will do the validations
        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Please enter username");
            editTextUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Please enter your email");
            editTextEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Enter a password");
            editTextPassword.requestFocus();
            return;
        }

        if(!cpassword.equals(password)){
            editTextPassword.setError("Password not match");
            editTextPassword.requestFocus();
            return;
        }
        //if it passes all the validations
        class RegisterUser extends AsyncTask<Void, Void, String> {
            ProgressDialog progressDialog;
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                imgView.setDrawingCacheEnabled(true);
                Bitmap bitmapupload = imgView.getDrawingCache();
                String imageupload = getStringImage(bitmapupload);
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                params.put("email", email);
                params.put("role", role);
                params.put("image", imageupload);
                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_REGISTER, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
//                progressBar = findViewById(R.id.progressBar);
//                progressBar.setVisibility(View.VISIBLE);
                progressDialog = ProgressDialog.show(Register.this, "Loading...", "Processing User Details..",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
//                progressBar.setVisibility(View.GONE);

                try {
                    //converting response to json object
                    //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                    JSONObject obj = new JSONObject(s);
                    JSONObject userJson = obj.getJSONObject("user");
                    int idint = userJson.getInt("id");
                    String id = Integer.toString(idint);
                    String username = userJson.getString("username");
                    String password = userJson.getString("password");
                    String email = userJson.getString("email");
                    String firstname = userJson.getString("firstname");
                    String imageurl = userJson.getString("image");
                    String role = userJson.getString("role");

                    if (!obj.getBoolean("error")) {
                        //creating a new user object
                        Model_User user = new Model_User(
                                idint,username,email,firstname,imageurl,role,password
                        );

                        progressDialog.dismiss();
                        //storing the user in shared preferences
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                        Intent intent = new Intent(getApplicationContext(), User_Handler.class);
                        startActivity(intent);
                        //starting the profile activity
                    } else {
                        Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        RegisterUser ru = new RegisterUser();
        ru.execute();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 7 && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imgView.setImageBitmap(bitmap);
            editImageValue.setText("Value");
        }
    }
    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA))
        {

            Toast.makeText(this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }
    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {
        switch (RC) {
            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

//                    Toast.makeText(this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}
