package app.num.barcodescannerproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import Objects.ActivityResponse;
import dbapp.SqlliteConsulter;

/**
 * Created by Administrador on 18/07/2016.
 */
public class EventActivity extends AppCompatActivity {
    private String pkEvent;
    private String pkActivity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SqlliteConsulter MDB= new SqlliteConsulter(EventActivity.this.getApplicationContext());
        Bundle extras= getIntent().getExtras();
        if(extras != null){
            pkEvent=extras.getString("pkEvent");
            pkActivity=extras.getString("pkActivity");
        }
        setContentView(R.layout.v_event);
        ActivityResponse activityResponse=MDB.getActivity(pkActivity);
        TextView titleTv=(TextView)findViewById(R.id.Activity_title);
        TextView dateStartTv=(TextView) findViewById(R.id.dateStart_tv);
        TextView dateEndTv=(TextView) findViewById(R.id.dateEnd_tv);
        TextView AsistantTv=(TextView) findViewById(R.id.asistants_tv);
        titleTv.setText(activityResponse.getName());
        Date startAt;
        String startDate="";
        try {
            startAt=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(activityResponse.getStarts_at());
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
             startDate= df.format(startAt);
        } catch (ParseException e) {
            startAt=null;
        }
        Date endAt;
        String endDate="";
        try {
            endAt=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(activityResponse.getEnds_at());
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            endDate= df.format(endAt);
        } catch (ParseException e) {
            endAt=null;
        }
        dateStartTv.setText(startDate);
        dateEndTv.setText(endDate);
        MDB= new SqlliteConsulter(EventActivity.this.getApplicationContext());
        int attendaceQ=MDB.getAttendanceQuantity(pkActivity);
        int registers=MDB.getRegisterQuantity(pkActivity);
        String quantityAttendance=attendaceQ>=0?String.valueOf(attendaceQ):"--";
        String quantityRegister=registers>=0?String.valueOf(registers):"--";
        AsistantTv.setText(quantityAttendance+"/"+quantityRegister);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_event_view,menu);
        return true;
    }
    public void mxScanQr(View view)
    {
        this.finish();
        Intent intent=new Intent(this,ScanQrActivity.class);
        intent.putExtra("pkActivity",pkActivity);
        startActivity(intent);
    }
    public void mxListAsistants(View view)
    {
        this.finish();
        Intent intent= new Intent(this,ListAsistansActivity.class);
        intent.putExtra("pkActivity",pkActivity);
        intent.putExtra("pkEvent",pkEvent);
        startActivity(intent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back:
                this.finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
