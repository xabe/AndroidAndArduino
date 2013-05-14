package com.tutorial.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
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
import com.tutorial.bluetooth.sensor.OrientationManager;
import com.tutorial.bluetooth.util.Constants;
import com.tutorial.bluetooth.view.CarView;

/**
 * Clase que gestina la actividad para controlar el coche
 * @author Chabir Atrahouch
 *
 */
public class CarActivity extends Activity {

    private static final String TAG = "CarActivity";
    private static final boolean D = true;
    
	private CarView carView;
	private BluetoothDevice device;
	private BluetoothService service = null;
	private OrientationManager orientationManager;
	
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
                	Toast.makeText(CarActivity.this, CarActivity.this.getString(R.string.connectDevice),Toast.LENGTH_SHORT).show();
                    break;
                case Constants.STATE_NONE:
                    break;
                }
                break;
            case Constants.MESSAGE_READ:
            	byte[] readBuf = (byte[]) msg.obj;
            	carView.reciveData(readBuf, msg.arg1);
                break;
            case Constants.MESSAGE_WRITE:
            	byte[] writeBuf = (byte[]) msg.obj;
            	service.write(writeBuf);
                break;
            case Constants.MESSAGE_TOAST:
            	Toast.makeText(CarActivity.this, msg.getData().getString(Constants.TOAST),Toast.LENGTH_SHORT).show();
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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        this.carView = new CarView(this);
        this.carView.setMhandler(handler);
        orientationManager = new OrientationManager(this);
        setContentView(this.carView);     
        this.carView.setMyTypeface(Typeface.createFromAsset(this.getAssets(),"DS-DIGIB.TTF"));
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
		if (orientationManager.isSupported()) {
        	orientationManager.startListening(carView);
        }
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (service != null)
		{
			service.stop();
		}
		if (orientationManager.isListening()) {
        	orientationManager.stopListening();
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
    
    
    public BluetoothService getServiceBluetooth() {
		return service;
	}
}
