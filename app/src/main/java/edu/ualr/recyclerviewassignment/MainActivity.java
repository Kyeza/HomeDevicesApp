package edu.ualr.recyclerviewassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import edu.ualr.recyclerviewassignment.data.DataGenerator;
import edu.ualr.recyclerviewassignment.model.Device;

public class MainActivity extends AppCompatActivity implements
        DeviceAdaptor.DeviceAdapterOnClickHandler {

    private static final int NUM_LIST_DEVICES = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecyclerView();
    }

    private void initRecyclerView(){
        // TODO. Create and initialize the RecyclerView instance here
        RecyclerView mDeviceList = findViewById(R.id.rv_devices);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mDeviceList.setLayoutManager(layoutManager);

        mDeviceList.setHasFixedSize(true);

        DeviceAdaptor mDeviceAdaptor = new DeviceAdaptor(this);
        mDeviceAdaptor.setDeviceData(DataGenerator.getDevicesDataset(NUM_LIST_DEVICES));

        mDeviceList.setAdapter(mDeviceAdaptor);
    }

    @Override
    public void onClick(Device device) {
        Context context = this;
        String message = device.getName();

        if (device.getDeviceStatus() == Device.DeviceStatus.Connected) {
            message = message + " Connected";
        } else {
            message = message + " Ready";
        }

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
