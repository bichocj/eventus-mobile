package app.num.barcodescannerproject;

import android.content.Intent;
import android.database.CursorJoiner;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.zxing.Result;

import java.util.ArrayList;

import Adapters.ActivityAdapter;
import Adapters.RegisterAdapter;
import Objects.ActivityResponse;
import Objects.RegisterResponse;
import dbapp.SqlliteConsulter;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Administrador on 20/07/2016.
 */
public class ListAsistansActivity extends AppCompatActivity  {
    private String pkEvent;
    private String pkActivity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras= getIntent().getExtras();
        if(extras != null){
            pkEvent = extras.getString("pkEvent");
            pkActivity=extras.getString("pkActivity");
        }
        setContentView(R.layout.v_asistan_list);
        showAssits();
    }

    private void showAssits() {
        SqlliteConsulter MDB= new SqlliteConsulter(ListAsistansActivity.this.getApplicationContext());
        ArrayList<RegisterResponse> activityResponses =MDB.getRegisters(pkEvent,pkActivity);
        final ListView lv= (ListView)findViewById(R.id.Asistants_lv);
        if(activityResponses!=null) {
            RegisterAdapter adapter= new RegisterAdapter(ListAsistansActivity.this,0,activityResponses);
            lv.setAdapter(adapter);
        }
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*RegisterResponse item = (RegisterResponse) lv.getItemAtPosition(position);
                Intent intent = new Intent(ListAsistansActivity.this, EventActivity.class);
                intent.putExtra("pkActivity",item.getPk());
                intent.putExtra("pkEvent",pkEvent);
                startActivity(intent);*/
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_asistant_list,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.back:
                this.finish();
                intent = new Intent(this, EventActivity.class);
                intent.putExtra("pkActivity",pkActivity);
                intent.putExtra("pkEvent",pkEvent);
                startActivity(intent);
                break;
            case R.id.scanqr:
                this.finish();
                intent= new Intent(this,ScanQrActivity.class);
                intent.putExtra("pkActivity",pkActivity);
                intent.putExtra("list",true);
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
