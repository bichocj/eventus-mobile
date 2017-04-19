package app.num.barcodescannerproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import Adapters.EventAdapter;
import Objects.ActivityResponse;
import Objects.EventResponse;
import Objects.LogInResponse;
import Objects.User;
import dbapp.SqlliteConsulter;

/**
 * Created by Administrador on 18/07/2016.
 */
public class ListEventActivity extends AppCompatActivity {
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_list_events);
        showEvent();
        Toast.makeText(ListEventActivity.this, "List Event", Toast.LENGTH_LONG).show();
    }
    public void showEvent(){
        final SqlliteConsulter MDB= new SqlliteConsulter(ListEventActivity.this.getApplicationContext());
        final ListView lv= (ListView)findViewById(R.id.Event_lstview);
        ArrayList<EventResponse> eventResponseList= MDB.showEvent();
        if(eventResponseList!=null) {
            EventAdapter adapter= new EventAdapter(ListEventActivity.this,0,eventResponseList);
            lv.setAdapter(adapter);
        }
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventResponse item = (EventResponse) lv.getItemAtPosition(position);
                Intent intent = new Intent(ListEventActivity.this, ListActivitiesActivity.class);
                intent.putExtra("pkEvent",item.getPk());
                startActivity(intent);
            }
        });

    }
    /* Modificar lista con lo devuelto por la consulta
     if(products!=null) {
            List<String> list = new ArrayList<String>();
            for (Product product : products) {
                list.add("Name: "+product.getName()+"\nPrice: "+product.getCost());
            }
            ArrayAdapter<String> adapter= new ArrayAdapter<String>(MainActivity.this.getApplicationContext(),android.R.layout.simple_list_item_1,list);
            lstProcducts.setAdapter(adapter);
        }
     */
    public void mxonclic(View view)
    {
        Intent intent= new Intent(this,EventActivity.class);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_event_list,menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        Intent intent = new Intent(ListEventActivity.this, SyncronizationActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back:
                this.finish();
                Intent intent = new Intent(ListEventActivity.this, SyncronizationActivity.class);
                startActivity(intent);
                break;
            case R.id.reload:
                setContentView(R.layout.v_list_events);
                showEvent();
                Toast.makeText(ListEventActivity.this,"Refrescado",Toast.LENGTH_LONG).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

}
