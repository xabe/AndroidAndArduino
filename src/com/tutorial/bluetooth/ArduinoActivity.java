package com.tutorial.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.tutorial.bluetooth.R;
import com.tutorial.bluetooth.connect.BluetoothService;
import com.tutorial.bluetooth.util.Constants;
import com.tutorial.bluetooth.view.ArduinoView;

/**
 * Clase que controla la actividad con arduino
 * @author Chabir Atrahouch 
 *
 */
public class ArduinoActivity extends Activity {

    private static final String TAG = "ArduinoActivity";
    private static final boolean D = true;
    
	private ArduinoView arduinoView;
	private BluetoothDevice device;
	private BluetoothService service;

	private Handler handler = new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
            case Constants.MESSAGE_STATE_CHANGE:
                if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case Constants.STATE_LISTEN:
                	break;
                case Constants.STATE_CONNECTING:
                    break;
                case Constants.STATE_CONNECTED:
                	Toast.makeText(ArduinoActivity.this, ArduinoActivity.this.getString(R.string.connectDevice),Toast.LENGTH_SHORT).show();
                    break;
                case Constants.STATE_NONE:
                    break;
                }
                break;
            case Constants.MESSAGE_READ:
            	byte[] readBuf = (byte[]) msg.obj;
            	arduinoView.reciveData(readBuf, msg.arg1);
                break;
            case Constants.MESSAGE_WRITE:
            	byte[] writeBuf = (byte[]) msg.obj;
            	service.write(writeBuf);
                break;
            case Constants.MESSAGE_TOAST:
            	Toast.makeText(ArduinoActivity.this, msg.getData().getString(Constants.TOAST),Toast.LENGTH_SHORT).show();
                break;
            }
			return false;
		}
	});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        this.arduinoView = new ArduinoView(this);
        this.arduinoView.setMhandler(handler);
        setContentView(this.arduinoView);        
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if (service == null)
		{
			service = new BluetoothService(this, handler);
		}
	}
	
	@Override
	protected void onResume() {
		super.onStart();
		SocketApplication app = (SocketApplication) getApplicationContext();
		if(app.getDevice() == null)
		{
			finish();
		}
		else
		{
			device = app.getDevice();
		}
		if (service != null) {
            // Sólo si el Estado es STATE_NONE, sabemos que no hemos empezado
            if (service.getState() == Constants.STATE_NONE) {
            	// iniciar los servicios Bluetooth
            	service.start();
            }
        }
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (service != null)
		{
			service.stop();
		}
		
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.MnuOpc1:
            	finish();
                return true;
            case R.id.MnuOpc2:
            	service.connect(device);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
