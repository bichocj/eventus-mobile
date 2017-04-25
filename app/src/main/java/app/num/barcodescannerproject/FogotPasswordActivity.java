package app.num.barcodescannerproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.fasterxml.jackson.databind.SerializationFeature;

import Objects.ForgotPassResponse;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import Objects.LogInResponse;
import Objects.User;
import dbapp.SqlliteConsulter;
import settings.Global_Variables;

/**
 * Created by Administrador on 20/04/2017.
 */
public class FogotPasswordActivity extends AppCompatActivity {
    private EditText inputEmail;
    private String static_url;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Button forgot= (Button) findViewById(R.id.btnForgot);
        if (Global_Variables.DEV){
            static_url=Global_Variables.DEV_STATIC_URL;
        }
        else{
            static_url=Global_Variables.PROD_STATIC_URL;
        }
        getSupportActionBar().hide();
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (validate()) {
                    Toast.makeText(MainActivity.this,"Validacion",Toast.LENGTH_SHORT).show();
                    //onSignupFailed();
                   // return;
                }*/
                inputEmail=(EditText)findViewById(R.id.txtEmail);
                String email= inputEmail.getText().toString();
                new HttpRequestTask(email).execute();

                // Intent intent = new Intent (v.getContext(),Main2ActivityCongre.class);
                //startActivityForResult(intent,0);

            }
        });
    }
    public  void goLogin(View view){
        Intent intent = new Intent(this,MainActivity.class);
        this.finish();
        startActivity(intent);
    }
    private class HttpRequestTask extends AsyncTask<Void, Void, Boolean> {
        private String inputEmail;
        private FogotPasswordActivity activity;
        private ProgressDialog progressBar;

        HttpRequestTask(String email) {
            this.inputEmail = email;
            progressBar = new ProgressDialog(FogotPasswordActivity.this);

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                activity = FogotPasswordActivity.this;
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        progressBar = ProgressDialog.show(FogotPasswordActivity.this, "Espere por favor ...", "Verificando datos ...", true);
                        progressBar.setCancelable(false);

                    }
                });

                final String url = static_url + "services/reset-password/";

                /*DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpRequest = new HttpPost(url);
                httpRequest.setEntity(new StringEntity("{\"email\":"+inputEmail+"}"));
                httpRequest.setHeader("content-type", "application/json");
                HttpResponse result=httpClient.execute(httpRequest);*/
                RestTemplate restTemplate = new RestTemplate();
                // Add the Jackson and String message converters
                MappingJackson2HttpMessageConverter jsonHttpMessageConverter = new MappingJackson2HttpMessageConverter();
                jsonHttpMessageConverter.getObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

                restTemplate.getMessageConverters().add(jsonHttpMessageConverter);
                //restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                //MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
                //body.add("email","aj.dalmao.aqp@gmail.com");
                ForgotPassResponse forgotPassResponse = new ForgotPassResponse();
                forgotPassResponse.setEmail(inputEmail);
                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setContentType(MediaType.APPLICATION_JSON);
                requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));


                org.springframework.http.HttpEntity requestEntity = new org.springframework.http.HttpEntity(forgotPassResponse, requestHeaders);
                try {
                    ResponseEntity<ForgotPassResponse> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, ForgotPassResponse.class);
                    if (responseEntity.getStatusCode().value()== 200) {
                        return true;
                    }
                } catch (HttpStatusCodeException exception) {
                    int statusCode = exception.getStatusCode().value();
                    return false;
                }


            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
                e.getMessage();
                return false;


            }
            return false;
        }




        @Override
        protected void onPostExecute(Boolean greeting)
        {
            if(greeting) {
                Toast.makeText(FogotPasswordActivity.this, "Se envio un correo con un link para actualizar su contraseña. . .", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FogotPasswordActivity.this, MainActivity.class);
                startActivity(intent);
                FogotPasswordActivity.this.finish();
            }
            else
            {
                Toast.makeText(activity,"Error: Ingrese un correo valido. . .",Toast.LENGTH_SHORT).show();
            }
            progressBar.dismiss();


        }

    }
}
