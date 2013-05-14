package com.tutorial.bluetooth;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.tutorial.bluetooth.R;
import com.tutorial.bluetooth.broadReceiver.BroadcastReceiverChangeBond;
import com.tutorial.bluetooth.broadReceiver.BroadcastReceiverDiscovery;
import com.tutorial.bluetooth.broadReceiver.BroadcastReceiverFound;
import com.tutorial.bluetooth.event.ButtonClicked;
import com.tutorial.bluetooth.event.ListItemClicked;
import com.tutorial.bluetooth.event.ListItemClickedonPaired;
import com.tutorial.bluetooth.util.Constants;

/**
 * Activity principal donde se busca dispositivos bluettoh y se empareja y se pasar a otra activity para comnunicarse por blueetooh
 * @author Chabir Atrahouch
 *
 */
public class MainActivity extends Activity {
	
	private Button buttonSearch;
	private Button buttonOn;
	private Button buttonDesc;
	private Button buttonOff;
	private ButtonClicked clicked;
	
	/**
	 * Lista de dispositivos emparejado para poder conectarse con ellos
	 */
	private ListView listViewPaired;
	/**
	 * Lista de dispositivos que se encuentra
	 */
	private ListView listViewDetected;
	private ArrayAdapter<String> adapter;
	private ArrayAdapter<String> detectedAdapter;
	private ListItemClicked listItemClicked;
	private ListItemClickedonPaired listItemClickedonPaired;
	private List<BluetoothDevice> arrayListBluetoothDevices;
    private List<String> arrayListpaired;
    private List<BluetoothDevice> arrayListPairedBluetoothDevices;
    
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice bdDevice;
    private BluetoothClass bdClass;
    private BroadcastReceiverFound broadcastReceiverFound;
    private BroadcastReceiverDiscovery broadcastReceiverDiscovery;
    private BroadcastReceiverChangeBond broadcastReceiverChangeBond;
    private boolean waitingForBonding;
    private boolean start;
    
    
    @Override 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        listViewDetected = (ListView) findViewById(R.id.listViewDetected);
        listViewPaired = (ListView) findViewById(R.id.listViewPaired);
        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        buttonOn = (Button) findViewById(R.id.buttonOn);
        buttonDesc = (Button) findViewById(R.id.buttonDesc);
        buttonOff = (Button) findViewById(R.id.buttonOff); 
        arrayListpaired = new ArrayList<String>();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        clicked = new ButtonClicked(this);
        arrayListPairedBluetoothDevices = new ArrayList<BluetoothDevice>();

        listItemClickedonPaired = new ListItemClickedonPaired(this);
        listItemClicked = new ListItemClicked(this);
        arrayListBluetoothDevices = new ArrayList<BluetoothDevice>();
        adapter= new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, arrayListpaired);
        detectedAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_single_choice);
        listViewDetected.setAdapter(detectedAdapter);
        detectedAdapter.notifyDataSetChanged();
        listViewPaired.setAdapter(adapter);
        broadcastReceiverFound = new BroadcastReceiverFound(this);
        broadcastReceiverDiscovery = new BroadcastReceiverDiscovery(this);
        broadcastReceiverChangeBond = new BroadcastReceiverChangeBond(this);
        waitingForBonding = false;
        start = true;
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        getPairedDevices();
        buttonOn.setOnClickListener(clicked);
        buttonSearch.setOnClickListener(clicked);
        buttonDesc.setOnClickListener(clicked);
        buttonOff.setOnClickListener(clicked);
        listViewDetected.setOnItemClickListener(listItemClicked);
        listViewPaired.setOnItemClickListener(listItemClickedonPaired);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	 //Accion para detectar cambios en los emparejamiento
        IntentFilter changeFilter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(getBroadcastReceiverChangeBond(), changeFilter);
        
        if (this.start && !this.getBluetoothAdapter().isEnabled()) 
		{
			intentBluetooth();
		}
    }
    
    private void intentBluetooth(){
		if (!this.getBluetoothAdapter().isEnabled()) 
		{
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, Constants.REQUEST_ENABLE_BT);
		}
	}
    
    public void intentArduino(){
    	SocketApplication app = (SocketApplication) getApplicationContext();
		app.setDevice(getBdDevice());
    	Intent intent = new Intent();
		intent.setClass(MainActivity.this, ArduinoActivity.class);
		startActivity(intent);
    }
    
    public void intentCar(){
    	SocketApplication app = (SocketApplication) getApplicationContext();
		app.setDevice(getBdDevice());
    	Intent intent = new Intent();
		intent.setClass(MainActivity.this, CarActivity.class);
		startActivity(intent);
    }
    
    /**
	 * Comprobamos que el resultado de la activacion del bluetooth ha sido correcta
	 */
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
		start = false;
        if (resultCode == Activity.RESULT_CANCELED) 
        {
        	Toast.makeText(this, R.string.messageNoActiveBluetooth, Toast.LENGTH_SHORT).show();
        }  
        if(requestCode == Constants.REQUEST_ENABLE_BT)
        {
        	getPairedDevices();
        }
        
        switch (requestCode) {
        case Activity.RESULT_CANCELED:
        	Toast.makeText(this, R.string.messageNoActiveBluetooth, Toast.LENGTH_SHORT).show();
            break;
        case Constants.REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
            	getPairedDevices();
            } else {
                Toast.makeText(this, R.string.bt_not_enabled, Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    @Override
    protected void onDestroy() {
    	unregisterReceiver(getBroadcastReceiverChangeBond());
    	System.exit(0);
    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			dialog();
		}
		return false;
	}

	protected void dialog() {
		AlertDialog.Builder build = new AlertDialog.Builder(MainActivity.this);
		build.setTitle(R.string.messageExist);
		build.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (bluetoothAdapter.isEnabled()) {
							bluetoothAdapter.disable();
						}
						SocketApplication app = (SocketApplication) getApplicationContext();
						app.setDevice(null);
						MainActivity.this.finish();
					}
				});
		build.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		build.create().show();
	}
    
    /**
     * Obtenemos la lista de dispositivos emprarejados
     */
    public void getPairedDevices() {
        Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();            
        if(pairedDevice.size()>0)
        {
        	arrayListpaired.clear();
            for(BluetoothDevice device : pairedDevice)
            {
                arrayListpaired.add(device.getName()+"\n"+device.getAddress());
                arrayListPairedBluetoothDevices.add(device);
            }
        }
        adapter.notifyDataSetChanged();
    }
    
    /**
     * Metodos Get atributos
     */
    
	public Button getButtonSearch() {
		return buttonSearch;
	}
	public Button getButtonOn() {
		return buttonOn;
	}
	public Button getButtonDesc() {
		return buttonDesc;
	}
	public Button getButtonOff() {
		return buttonOff;
	}
	public ButtonClicked getClicked() {
		return clicked;
	}
	public ListView getListViewPaired() {
		return listViewPaired;
	}
	public ListView getListViewDetected() {
		return listViewDetected;
	}
	public ArrayAdapter<String> getAdapter() {
		return adapter;
	}
	public ArrayAdapter<String> getDetectedAdapter() {
		return detectedAdapter;
	}
	public ListItemClicked getListItemClicked() {
		return listItemClicked;
	}
	public ListItemClickedonPaired getListItemClickedonPaired() {
		return listItemClickedonPaired;
	}
	public List<BluetoothDevice> getArrayListBluetoothDevices() {
		return arrayListBluetoothDevices;
	}
	public List<String> getArrayListpaired() {
		return arrayListpaired;
	}
	public List<BluetoothDevice> getArrayListPairedBluetoothDevices() {
		return arrayListPairedBluetoothDevices;
	}
	public BluetoothAdapter getBluetoothAdapter() {
		return bluetoothAdapter;
	}
	public BluetoothDevice getBdDevice() {
		return bdDevice;
	}
	public void setBdDevice(BluetoothDevice device) {
		 this.bdDevice = device;
	}
	public BluetoothClass getBdClass() {
		return bdClass;
	}
	public BroadcastReceiverFound getBroadcastReceiverFound() {
		return broadcastReceiverFound;
	}
	public BroadcastReceiverDiscovery getBroadcastReceiverDiscovery() {
		return broadcastReceiverDiscovery;
	}
	public BroadcastReceiverChangeBond getBroadcastReceiverChangeBond() {
		return broadcastReceiverChangeBond;
	}
	public boolean isWaitingForBonding() {
		return waitingForBonding;
	}
	public void setWaitingForBonding(boolean waitingForBonding) {
		this.waitingForBonding = waitingForBonding;
	}

}
