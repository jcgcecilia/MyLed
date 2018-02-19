package com.example.jcc.myled;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    Button btnPaired;
    ListView deviceList;


    private BluetoothAdapter myBlueTooth = null;
    private Set<BluetoothDevice> pairedDevices;
    public static String EXTRA_ADDRESS = "device_address";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Calling widgets
        btnPaired = (Button)findViewById(R.id.button_paired_devices);
        deviceList = (ListView)findViewById(R.id.listView);

        myBlueTooth = BluetoothAdapter.getDefaultAdapter();


        if(myBlueTooth == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
            finish();
        } else if (!myBlueTooth.isEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 1);
        }

        btnPaired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pairedDevicesList();
            }
        });
    }


    private void pairedDevicesList() {
        pairedDevices = myBlueTooth.getBondedDevices();
        List<String> list = new ArrayList<String>();
        if(pairedDevices.size() > 0 ) {
            for(BluetoothDevice bt: pairedDevices) {
                list.add(bt.getName() + "\n" + bt.getAddress());
            }
        } else {
            Toast.makeText(getApplicationContext(), "No paired Bluetooth device found", Toast.LENGTH_SHORT).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        deviceList.setAdapter(adapter);
        deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Get the device MAC address, the last 17 chars in the View
                String info = ((TextView) view).getText().toString();//((TextView) v).getText().toString();
                String address = info.substring(info.length() - 17);

                // Make an intent to start next activity.
                Intent intent = new Intent(MainActivity.this, LedControlActivity.class);

                //Change the activity.
                intent.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
                startActivity(intent);
            }
        });
    }
}
