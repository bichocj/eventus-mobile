package app.num.barcodescannerproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.http.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import Objects.LogInResponse;
import Objects.User;
import dbapp.SqlliteConsulter;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import settings.Global_Variables;

////del ejm de post
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//qr

//
public class MainActivity extends AppCompatActivity{
    private ZXingScannerView mScannerView;
    private EditText inputEmail;
    private EditText inputPass;
    private ProgressDialog ringProgressDialog;
    private String static_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("CREATE Login","Login");
        if (Global_Variables.DEV){
            static_url=Global_Variables.DEV_STATIC_URL;
        }
        else{
            static_url=Global_Variables.PROD_STATIC_URL;
        }

        SqlliteConsulter MDB= new SqlliteConsulter(MainActivity.this.getApplicationContext());
        User user_logged=MDB.isLoggedUser();
        if(user_logged!=null){
            this.finish();
            Intent intent= new Intent(MainActivity.this,SyncActivity.class);
            startActivity(intent);
        }
        setContentView(R.layout.activity_login);
        //evento login
        Button milogin = (Button) findViewById(R.id.btnLogin);
        milogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (validate()) {
                    Toast.makeText(MainActivity.this,"Validacion",Toast.LENGTH_SHORT).show();
                    //onSignupFailed();
                   // return;
                }*/
                inputEmail=(EditText)findViewById(R.id.txtEmail);
                inputPass=(EditText)findViewById(R.id.txtPass);
                String email= inputEmail.getText().toString();
                String pass= inputPass.getText().toString();
                //Toast.makeText(MainActivity.this,"Presiono login",Toast.LENGTH_SHORT).show();
                //makePostRequest();
                new HttpRequestTask(email,pass).execute();

               // Intent intent = new Intent (v.getContext(),Main2ActivityCongre.class);
                //startActivityForResult(intent,0);

            }
        });
        if(ringProgressDialog != null) {
            ringProgressDialog.dismiss();
        }

    }

    public void forgotPassword(View view){
        Intent intent = new Intent(MainActivity.this, FogotPasswordActivity.class);
        startActivity(intent);

    }
    public void onSignupFailed() {
        Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_LONG).show();
        //_signupButton.setEnabled(true);
    }
    public void onSignupSuccess() {
      //  _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }
    public void Login(View view){
        inputEmail = (EditText)findViewById(R.id.txtEmail);
        inputPass = (EditText)findViewById(R.id.txtPass);
    }
    @Override
    protected void onStart() {
        super.onStart();

       // new HttpRequestTask().execute();
    }


    public boolean validate() {
        boolean valid = true;
        //final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,R.style.AppTheme_Dark_Dialog);
               // progressDialog.setMessage("comenzando validacion");
        Log.d("CREATE VALIDATION","Validacion");
        String email = inputEmail.getText().toString();
        String password = inputPass.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("enter a valid email address");
            valid = false;
        } else {
            inputEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            inputPass.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            inputPass.setError(null);
        }

        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_event_view,menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.back:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.dialog_exit)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                                System.exit(0);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        }).show();

                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, LogInResponse> {
        private String inputEmail;
        private String inputPass;
        private MainActivity activity;
        private String ErrorMessage;
        private ProgressDialog progressBar;

        HttpRequestTask(String email,String pass) {
            this.inputEmail=email;
            this.inputPass=pass;
            progressBar = new ProgressDialog(MainActivity.this);

        }

        @Override
        protected LogInResponse doInBackground(Void... params) {
            try {
                activity=MainActivity.this;
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        progressBar = ProgressDialog.show(MainActivity.this, "Please wait ...", "Logueando ...", true);
                        progressBar.setCancelable(false);

                    }
                });

                final String url = static_url+"services/auth/login/";

                RestTemplate restTemplate = new RestTemplate();
                // Add the Jackson and String message converters
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                //restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                String username = inputEmail;
                String pass= inputPass;
                User user= new User();
                user.setPassword(pass);
                user.setUsername(username);
                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setContentType(new MediaType("application","json"));
                org.springframework.http.HttpEntity<User> requestEntity = new org.springframework.http.HttpEntity<User>(user, requestHeaders);
                ResponseEntity<LogInResponse> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, LogInResponse.class);
                LogInResponse result = responseEntity.getBody();
                return result;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
                ErrorMessage=e.getMessage();


                return null;
            }


        }

        @Override
        protected void onPostExecute(LogInResponse greeting)
        {
            if(greeting!=null) {
                Toast.makeText(MainActivity.this, "Se logeo. . .", Toast.LENGTH_SHORT).show();
                SqlliteConsulter MDB= new SqlliteConsulter(MainActivity.this.getApplicationContext());
                MDB.insertUser(new User("",inputEmail,inputPass,"true","false","false",greeting.getToken()));
                Intent intent = new Intent(MainActivity.this, SyncActivity.class);
                intent.putExtra("token",greeting.getToken());
                startActivity(intent);
                MainActivity.this.finish();
            }
            else
            {
                Toast.makeText(activity,"Error: Ingrese user y contraseña correcto. . .",Toast.LENGTH_SHORT).show();
            }
            progressBar.dismiss();


        }

    }


}
