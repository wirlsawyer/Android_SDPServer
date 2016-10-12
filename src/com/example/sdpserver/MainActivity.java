package com.example.sdpserver;

import java.io.IOException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	private BluetoothAdapter mBtAdapter;
	private SDPServer mServerSDP;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				TextView TextAre = (TextView) findViewById(R.id.editText2);
				String str = msg.obj.toString();
				
				TextAre.setText(String.format("Recv=%s", str.toString()));
			}

		};
		
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		
		mServerSDP = new SDPServer(mBtAdapter, "PWAccessP", handler);
		mServerSDP.start();

		

		Button button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent discoverableIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				discoverableIntent.putExtra(
						BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
				startActivity(discoverableIntent);
				
				if (!mServerSDP.isAlive())
				{
					mServerSDP.start();
				}//end if
				
			}
		});

		Button button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String Msg = ((EditText) findViewById(R.id.editText1))
						.getText().toString();
				
				try {
					mServerSDP.localBluetoothSocket.getOutputStream().write(
							Msg.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
