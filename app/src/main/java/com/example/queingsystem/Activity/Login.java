package com.example.queingsystem.Activity;

//import androidx.support.v7.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.queingsystem.Model_User;
import com.example.queingsystem.R;
import com.example.queingsystem.RequestHandler;
import com.example.queingsystem.SharedPrefManager;
import com.example.queingsystem.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Login extends AppCompatActivity {
    EditText editTextUsername, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            //finish();
            Model_User user = SharedPrefManager.getInstance(this).getUser();
            if(user.getRole().equals("Admin")){
                Intent intent = new Intent(getApplicationContext(), Admin_Handler.class);
                startActivity(intent);
            }else if(user.getRole().equals("Driver")) {
                Intent intent = new Intent(getApplicationContext(), Driver_Handler.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(getApplicationContext(), User_Handler.class);
                startActivity(intent);
            }
        }

        editTextUsername = findViewById(R.id.editTxtUsername);
        editTextPassword = findViewById(R.id.editTxtPassword);

        //if user presses on login
        //calling the method login
        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });
        //if user presses on not registered
        findViewById(R.id.textViewRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });
    }

    private void userLogin() {
        //first getting the values
        final String username = editTextUsername.getText().toString();
        final String password = editTextPassword.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Please enter your username");
            editTextUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Please enter your password");
            editTextPassword.requestFocus();
            return;
        }
        //if everything is fine
        class UserLogin extends AsyncTask<Void, Void, String> {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(Login.this, "Checking user...", "Validating User!",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        JSONObject userJson = obj.getJSONObject("user");
                        int idint = userJson.getInt("id");
                        String username_res = userJson.getString("username");
                        String password = userJson.getString("password");

                        String email = userJson.getString("email");
                        String firstname = userJson.getString("firstname");
                        String image = userJson.getString("image").trim();
                        String role = userJson.getString("role").trim();
                        //String imageupload = getStringImage(image);
                        //creating a new user object
                        Model_User user = new Model_User(
                                idint,username_res,email,firstname,image,role,password
                        );

                        if(role.equals("Admin")){
                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                            Intent intent = new Intent(getApplicationContext(), Admin_Handler.class);
                            startActivity(intent);
                            progressDialog.dismiss();

                        }else if (role.equals("Driver")){
                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                            Intent intent = new Intent(getApplicationContext(), Driver_Handler.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                        }else{
                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                            Intent intent = new Intent(getApplicationContext(), User_Handler.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                        }
                        //progressBar.setVisibility(View.GONE);
                        //Toast.makeText(getApplicationContext(), image.trim(), Toast.LENGTH_SHORT).show();
                        //finish();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("username", username);
//                params.put("password", password);
                //returning the response
                return requestHandler.sendPostRequest(URLs.URL_LOGIN, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }
}
