package edu.ualr.recyclerviewassignment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.ualr.recyclerviewassignment.model.Device;

public class DeviceAdaptor extends RecyclerView.Adapter<DeviceAdaptor.DeviceViewHolder>{

    private static final String TAG = DeviceAdaptor.class.getSimpleName();

    private List<Device> mDevicesData;

    private List<Device> connectionHistory;

    private final DeviceAdapterOnClickHandler mClickHandler;

    public interface DeviceAdapterOnClickHandler {
        void onClick(Device device);
    }

    DeviceAdaptor(DeviceAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
        connectionHistory = new ArrayList<>();
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.device_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);

        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mDevicesData.size();
    }

    class DeviceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView deviceNameTV;
        private TextView deviceConnectionTimestampTV;
        private ImageView deviceConnectionButton;
        private ImageView deviceStatusIV;
        private ImageView deviceImageIV;

        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceNameTV = itemView.findViewById(R.id.tv_device_name);
            deviceConnectionTimestampTV = itemView.findViewById(R.id.tv_connection_timestamp);
            deviceConnectionButton = itemView.findViewById(R.id.iv_device_connection);
            deviceStatusIV = itemView.findViewById(R.id.iv_device_status);
            deviceImageIV = itemView.findViewById(R.id.iv_device_img);

            deviceConnectionButton.setOnClickListener(this);
        }

        private String dateToString(Date date) {
            String string_date;

            @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
            string_date = dateFormat.format(date);

            return string_date;
        }

        @SuppressLint("DefaultLocale")
        void bind(int position) {
            Device device = mDevicesData.get(position);
            deviceNameTV.setText(device.getName());
            Log.d(TAG, device.getName() + " = " + device.getLastConnection());
            deviceConnectionTimestampTV.setText(R.string.linked_status);

            switch (device.getDeviceType()) {
                case Laptop:
                    deviceImageIV.setImageResource(R.drawable.ic_laptop);
                    break;
                case Tablet:
                    deviceImageIV.setImageResource(R.drawable.ic_tablet_android);
                    break;
                case Desktop:
                    deviceImageIV.setImageResource(R.drawable.ic_pc);
                    break;
                case SmartTV:
                    deviceImageIV.setImageResource(R.drawable.ic_tv);
                    break;
                case Unknown:
                    deviceImageIV.setImageResource(R.drawable.ic_unknown_device);
                    break;
                case Smartphone:
                    deviceImageIV.setImageResource(R.drawable.ic_phone_android);
                    break;
                case GameConsole:
                    deviceImageIV.setImageResource(R.drawable.ic_gameconsole);
                    break;
            }

            if (device.getDeviceStatus() == Device.DeviceStatus.Ready) {
                deviceStatusIV.setImageResource(R.drawable.status_mark_ready);
                deviceConnectionButton.setImageResource(R.drawable.ic_btn_connect);
                if (connectionHistory.contains(device)) {
                    deviceConnectionTimestampTV.setText(R.string.recently);
                } else {
                    deviceConnectionTimestampTV.setText(R.string.never_connected);
                }
            } else if (device.getDeviceStatus() == Device.DeviceStatus.Connected) {
                deviceStatusIV.setImageResource(R.drawable.status_mark_connected);
                deviceConnectionButton.setImageResource(R.drawable.ic_btn_disconnect);
                deviceConnectionTimestampTV.setText(R.string.connected);
                connectionHistory.add(device);
            } else {
                deviceConnectionButton.setEnabled(false);
            }


        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Device device = mDevicesData.get(adapterPosition);

            if (device.getDeviceStatus() == Device.DeviceStatus.Ready) {
                deviceConnectionButton.setImageResource(R.drawable.ic_btn_disconnect);
                device.setDeviceStatus(Device.DeviceStatus.Connected);
                deviceStatusIV.setImageResource(R.drawable.status_mark_connected);
                if (!connectionHistory.contains(device)) { connectionHistory.add(device); }
                deviceConnectionTimestampTV.setText(R.string.connected);

            } else if (device.getDeviceStatus() == Device.DeviceStatus.Connected ||
                    device.getDeviceStatus() == Device.DeviceStatus.Linked) {
                deviceConnectionButton.setImageResource(R.drawable.ic_btn_connect);
                device.setDeviceStatus(Device.DeviceStatus.Ready);
                deviceStatusIV.setImageResource(R.drawable.status_mark_ready);
                if (connectionHistory.contains(device)) {
                    deviceConnectionTimestampTV.setText(R.string.recently);
                }
            }

            mClickHandler.onClick(device);
        }
    }

    void setDeviceData(List<Device> devicesData) {
        mDevicesData = devicesData;
        notifyDataSetChanged();
    }
}
