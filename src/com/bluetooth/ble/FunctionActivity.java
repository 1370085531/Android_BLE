package com.bluetooth.ble;

import java.text.SimpleDateFormat;

import com.bluetooth.ble.DateTimepickerDialog.OnDateTimeSetListener;
import com.bluetooth.ble.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FunctionActivity extends Activity implements OnClickListener {

	private TextView device_name, device_addres, connect_sate, now_rssi,goal_uuid;
	private TextView textView_1,textView_2,textView_3,textView_4,textView_5;
	private TextView receive_textview;
	private EditText number_edittext;
	private Button set_start_time_button,set_end_time_button,send_data_button,clear_received_data;
	
	protected static String EXTRAS_DEVICE_NAME = "DEVICE_NAME";;
	protected static String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
	private BluetoothLeService mBluetoothLeService;
	private String mDeviceAddress;
	private String control_data = "";
	private String control_data_frame = "";		//定义控制数据帧
	private String smsg = "";                 	//显示用数据缓存
	private int sendlenth=0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.function_activity);
		init();
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mDeviceAddress = bundle.getString(EXTRAS_DEVICE_ADDRESS).toString();
			device_addres.setText(mDeviceAddress);
			device_name.setText(bundle.getString(EXTRAS_DEVICE_NAME).toString());
			connect_sate.setText(bundle.getString("CONNET_SATE").toString());
			now_rssi.setText(bundle.getString("RSSI").toString());
			goal_uuid.setText(bundle.getString("UUID").toString());
			goal_uuid.setTextColor(Color.RED);
		}
		
		Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
		bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
		MyGattDetail.read();
		IntentFilter intentFilter = new IntentFilter(
				"com.example.bluetooth.le.ACTION_DATA_AVAILABLE");
		registerReceiver(myReceiver, intentFilter);//注册广播
		
	}
	
	private final ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName componentName,IBinder service) {
			mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
			if (!mBluetoothLeService.initialize()) {
				finish();
			}
			mBluetoothLeService.connect(mDeviceAddress);
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			mBluetoothLeService = null;
		}
	};

	private void init() {
		
		device_name = (TextView) findViewById(R.id.device_name);
		device_addres = (TextView) findViewById(R.id.device_addres);
		connect_sate = (TextView) findViewById(R.id.connect_sate);
		now_rssi = (TextView) findViewById(R.id.now_rssi);
		goal_uuid = (TextView) findViewById(R.id.goal_uuid);
		
		number_edittext = (EditText) findViewById(R.id.number_edittext);
		
		set_start_time_button = (Button) this.findViewById(R.id.set_start_time_button);
		set_start_time_button.setOnClickListener(this);
		
		set_end_time_button = (Button) this.findViewById(R.id.set_end_time_button);
		set_end_time_button.setOnClickListener(this);
		
		send_data_button = (Button) findViewById(R.id.send_data_button);
		send_data_button.setOnClickListener(this);
		
		textView_1 = (TextView) findViewById(R.id.textView_1);
		textView_1.setTextColor(Color.GREEN);
		textView_2 = (TextView) findViewById(R.id.textView_2);
		textView_2.setTextColor(Color.GRAY);
		textView_3 = (TextView) findViewById(R.id.textView_3);
		textView_3.setTextColor(Color.GRAY);
		textView_4 = (TextView) findViewById(R.id.textView_4);
		textView_4.setTextColor(Color.GRAY);
		textView_5 = (TextView) findViewById(R.id.textView_5);
		textView_5.setTextColor(Color.GRAY);
		
		receive_textview = (TextView) findViewById(R.id.receive_textview);
		
		clear_received_data = (Button) this.findViewById(R.id.clear_received_data);
		clear_received_data.setOnClickListener(this);
	}
	
	public void get_number_edittext_value() {
		control_data += number_edittext.getText().toString();
	}
	
	public void showDialog1() {
		DateTimepickerDialog datetimedialog = new DateTimepickerDialog (this,  
                System.currentTimeMillis());  
        /** 
         * 实现接口 
         */
		datetimedialog.setOnDateTimeSetListener(new OnDateTimeSetListener() {
			public void OnDateTimeSet(DialogInterface dialog,String datetimestr) {
				get_number_edittext_value();
				control_data += "-" + datetimestr.toString();
			}
			
		});
		datetimedialog.show();
	}
	
	public void showDialog2() {
		DateTimepickerDialog datetimedialog = new DateTimepickerDialog (this,  
                System.currentTimeMillis());  
        /** 
         * 实现接口 
         */
		datetimedialog.setOnDateTimeSetListener(new OnDateTimeSetListener() {
			public void OnDateTimeSet(DialogInterface dialog,String datetimestr) {
				
				control_data += "---" + datetimestr.toString();
				textView_1.setText(control_data);
				control_data_frame = control_data + "\r\n";
				control_data = "";
			}
			
		});
		datetimedialog.show();
	}
	
	private BroadcastReceiver myReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					BluetoothLeService.ACTION_DATA_AVAILABLE)) {
//				f1=0;
				smsg += intent.getExtras().getString(BluetoothLeService.EXTRA_DATA);
				receive_textview.setText(smsg);
			}
		}

	};
	
	//发送数据
	private void send_control_data() {
		MyGattDetail.write(control_data_frame);
		sendlenth += control_data.getBytes().length;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unbindService(mServiceConnection);
		unregisterReceiver(myReceiver);
	}
	
	private void received_data_clear()
	{
		smsg="";
		receive_textview.setText(smsg);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.clear_received_data:
				received_data_clear();
				break;
			case R.id.set_start_time_button:
				showDialog1();
				break;
			case R.id.set_end_time_button:
				showDialog2();
				break;
			case R.id.send_data_button:
				send_control_data();
				break;
			default:
				break;
		}

	}
	
	
	
}
