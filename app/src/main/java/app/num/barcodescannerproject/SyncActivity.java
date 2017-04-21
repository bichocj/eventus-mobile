package app.num.barcodescannerproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import settings.Global_Variables;

/**
 * Created by Administrador on 20/04/2017.
 */
public class SyncActivity extends AppCompatActivity {
    private String static_url;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (Global_Variables.DEV){
            static_url=Global_Variables.DEV_STATIC_URL;
        }
        else{
            static_url=Global_Variables.PROD_STATIC_URL;
        }
        Bundle extras= getIntent().getExtras();
        if(extras != null){
            token=extras.getString("token");
        }

        setContentView(R.layout.activity_sync);
    }
    public  void syncbtnClick(View view){
        Intent intent= new Intent(SyncActivity.this,SyncronizationActivity.class);
        startActivity(intent);
        SyncActivity.this.finish();
    }

}
