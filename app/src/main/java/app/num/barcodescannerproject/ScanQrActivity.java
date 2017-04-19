package app.num.barcodescannerproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import Objects.RegisterResponse;
import dbapp.SqlliteConsulter;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Administrador on 19/07/2016.
 */
public class ScanQrActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private Button scanBtn;
    private TextView formatTxt, contentTxt;
    private ZXingScannerView mScannerView;
    private String pkActivity;
    private Boolean pklist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras= getIntent().getExtras();
        if(extras != null){

            pkActivity=extras.getString("pkActivity");
            pklist=extras.getBoolean("list");
        }
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();

    }
    public void QrScanner(View view){

              // Start camera
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();   // Stop camera on pause
    }
    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here

        Log.e("handler", rawResult.getText()); // Prints scan results
        Log.e("handler", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode)
        /*if (rawResult != null) {
            String scanContent = rawResult.getText();
            String scanFormat = rawResult.getBarcodeFormat().toString();
            formatTxt.setText("FORMAT: " + scanFormat);
            contentTxt.setText("CONTENT: " + scanContent);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }*/
        // show the scanner result into dialog box.
        /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setMessage(rawResult.getText());
        AlertDialog alert1 = builder.create();
        alert1.show();*/

        mScannerView.stopCamera();
        String scanContent = rawResult.getText();
        String scanFormat = rawResult.getBarcodeFormat().toString();
        setContentView(R.layout.v_scan_qr);
        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt = (TextView)findViewById(R.id.scan_content);
        formatTxt.setText("FORMAT: " + scanFormat);
        contentTxt.setText("CONTENT: " + scanContent);
        SqlliteConsulter MDB= new SqlliteConsulter(ScanQrActivity.this.getApplicationContext());
        String register=MDB.TakeAssistance(scanContent,pkActivity);
        if(register != null) {
            Toast.makeText(ScanQrActivity.this, register, Toast.LENGTH_LONG).show();
            this.finish();

            Intent intent= new Intent(this,ListAsistansActivity.class);
            intent.putExtra("pkActivity",pkActivity);
            startActivity(intent);

        }
        else
        {
            Toast.makeText(ScanQrActivity.this, "No existe registro, Intente sincronizar nuevamente...", Toast.LENGTH_LONG).show();
            this.finish();
            Intent intent= new Intent(this,ScanQrActivity.class);
            intent.putExtra("pkActivity",pkActivity);
            startActivity(intent);
        }



        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
    }
    /*@Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_scan_qr);
        scanBtn = (Button)findViewById(R.id.scan_button);
        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt = (TextView)findViewById(R.id.scan_content);
        scanBtn.setOnClickListener(this);
    }
    public void onClick(View v){
        if(v.getId()==R.id.scan_button){
            IntentIntegrator scanIntegrator=new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }

    }*/
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            formatTxt.setText("FORMAT: " + scanFormat);
            contentTxt.setText("CONTENT: " + scanContent);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_scan_qr,menu);
        return true;
    }

    @Override
    public boolean  onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back:
                this.finish();
                Intent intent= new Intent(this,ListAsistansActivity.class);
                intent.putExtra("pkActivity",pkActivity);
                startActivity(intent);
                break;

            case R.id.flash:
                if(mScannerView.getFlash()){
                    item.setIcon(getResources().getDrawable(R.drawable.ic_flash_on_white_24dp));
                    mScannerView.setFlash(false);
                }
                else{
                    item.setIcon(getResources().getDrawable(R.drawable.ic_flash_off_white_24dp));
                     mScannerView.setFlash(true);
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

}
