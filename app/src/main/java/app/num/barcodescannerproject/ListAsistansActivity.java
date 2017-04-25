package app.num.barcodescannerproject;

import android.content.Intent;
import android.database.CursorJoiner;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.Result;

import java.util.ArrayList;

import Adapters.ActivityAdapter;
import Adapters.RegisterAdapter;
import Objects.ActivityResponse;
import Objects.RegisterResponse;
import Utils.OnSwipeTouchListener;
import dbapp.SqlliteConsulter;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Administrador on 20/07/2016.
 */
public class ListAsistansActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String pkEvent;
    private String pkActivity;
    private String nameActivity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras= getIntent().getExtras();
        if(extras != null){
            pkEvent = extras.getString("pkEvent");
            pkActivity=extras.getString("pkActivity");
            nameActivity=extras.getString("nameActivity");
            //getSupportActionBar().setTitle(nameActivity);
        }
        setContentView(R.layout.v_list_asistants);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_asistant);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_tool);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent= new Intent(ListAsistansActivity.this,ScanQrActivity.class);
                intent.putExtra("pkActivity",pkActivity);
                intent.putExtra("list",true);
                startActivity(intent);

            }
        });
        showAssits();
    }

    private void showAssits() {
        SqlliteConsulter MDB= new SqlliteConsulter(ListAsistansActivity.this.getApplicationContext());
        ArrayList<RegisterResponse> registerResponses =MDB.getRegisters(pkEvent,pkActivity);
        for (RegisterResponse register : registerResponses){
            if(MDB.have_attendance(pkActivity,register.getPk()))
                register.setHave_attendance("true");
            else
                register.setHave_attendance("false");
        }
        final ListView lv= (ListView)findViewById(R.id.Asistants_lv);
        if(registerResponses!=null) {
            RegisterAdapter adapter= new RegisterAdapter(ListAsistansActivity.this,0,registerResponses);
            lv.setAdapter(adapter);
        }
        lv.setOnTouchListener(new OnSwipeTouchListener(ListAsistansActivity.this){
            public void onSwipeRight() {
                Toast.makeText(ListAsistansActivity.this, "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                Toast.makeText(ListAsistansActivity.this, "left", Toast.LENGTH_SHORT).show();
            }
        });
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
            /*case R.id.scanqr:
                this.finish();
                intent= new Intent(this,ScanQrActivity.class);
                intent.putExtra("pkActivity",pkActivity);
                intent.putExtra("list",true);
                startActivity(intent);
                break;*/
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }
}
