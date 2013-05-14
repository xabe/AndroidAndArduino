package com.tutorial.bluetooth.util;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface.OnDismissListener;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;

/**
 * Clase que contiene valores fijos de la aplicación
 * @author Chabir Atrahouch
 *
 */
public final class Constants {
	private Constants(){};
	
	private static Object obj=new Object();
	
	public static final int REQUEST_ENABLE_BT = 1;
	
    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 4;
    public static final int MESSAGE_TOAST = 3;
    
    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device
    
    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    
    // Name for the SDP record when creating server socket
    public static final String NAME = "BluetoothService";

    // Unique UUID for this application
    public static UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");	
	
	public static final String PROTOCOL_SCHEME_L2CAP = "btl2cap";
	public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
	public static final String PROTOCOL_SCHEME_BT_OBEX = "btgoep";
	public static final String PROTOCOL_SCHEME_TCP_OBEX = "tcpobex";
	
	public static final String END_LINE = "\n";
	
	public static final String YELLOW_ON = "1"+END_LINE;
	public static final String YELLOW_OFF = "2"+END_LINE;
	
	public static final String RED_ON = "3"+END_LINE;
	public static final String RED_OFF = "4"+END_LINE;
	
	public static final String LEFT = "L"+END_LINE;
	public static final String RIGHT = "R"+END_LINE;
	public static final String CENTER = "C"+END_LINE;
	
	
	public static final int POSITION_D = 1;
	public static final int POSITION_N = 0;
	public static final int POSITION_R = -1;
	
	public static final String FORWARD = "F"+END_LINE;
	public static final String BACKkWARD = "B"+END_LINE;
	public static final String STOP = "S"+END_LINE;
	
	public final static int MAX_FPS = 100;	
	public final static int	MAX_FRAME_SKIPS = 5;	
	public final static int	FRAME_PERIOD = 1000 / MAX_FPS;	
	
	public final static int ADD_VELOCITY = 5;
	
	public final static int INIT_VELOCITY_DEGREES = 135;
	public final static int MARGIN_VELOCITY = 100;
	public final static int MARGIN_GEARBOX = 150;
	public final static int MARGIN_TEXT_GEARBOX = 20;
	
	public final static int POS_AZIMUTH = 0;
	public final static int POS_PITH = 1;
	public final static int POS_ROLL = 2;
	
	public static final int DIRECTION_RIGHT = 1;
	public static final int DIRECTION_LEFT = -1;
	public static final int DIRECTION_UP = -1;
	public static final int DIRECTION_DOWN = 1;
	
	
	public static final int STATE_LOSE = 1;
	public static final int STATE_PAUSE = 2;
	public static final int STATE_READY = 3;
	public static final int STATE_RUNNING = 4;
	public static final int STATE_WIN = 5;
	
	public static final int SIZE_TEXT = 30;
	public static final int SIZE_TEXT_2 = 40;
	public static final int SIZE_TEXT_3 = 120;
	
    /**
	 * Método para crear un el emparejamiento 
	 * @param bdDevice dispositivo
	 * @return 
	 */
	public static Boolean createBond (BluetoothDevice bdDevice) { 
        Boolean bool = false;
        try 
        {
            Class<?> cl = Class.forName("android.bluetooth.BluetoothDevice");
            Class<?>[] par = {};
            Method method = cl.getMethod("createBond", par);
            bool = (Boolean) method.invoke(bdDevice);
        } catch (Exception e) {
            Log.i("Log", "Inside catch of serviceFromDevice Method");
            e.printStackTrace();
        }
        return bool.booleanValue();
    }
	
	/**
	 * Método para quitar el emparejamiento 
	 * @param bdDevice dispositivo
	 * @return 
	 */
    public static boolean removeBond(BluetoothDevice btDevice) throws Exception  
    {  
        Class<?> btClass = Class.forName("android.bluetooth.BluetoothDevice");
        Method removeBondMethod = btClass.getMethod("removeBond");  
        Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice);  
        return returnValue.booleanValue();  
    }

	
	//In SDK15 (4.0.3) this method is now public as
	//BluetoothDevice.getUuids()
	public static ParcelUuid[] getUUIDs(BluetoothDevice device) {
	    try {
	        Class<?> cl = Class.forName("android.bluetooth.BluetoothDevice");
	        Class<?>[] par = {};
	        Method method = cl.getMethod("getUuids", par);
	        Object[] args = {};
	        ParcelUuid[] retval = (ParcelUuid[]) method.invoke(device, args);
	        return retval;
	    } catch (Exception e) {
	    	Log.e("Constants", "error get UUID", e);
	        return new ParcelUuid[] {};
	    }
	}
	
	//In SDK15 (4.0.3) this method is now public as
	//Bluetooth.fetchUuisWithSdp() llamar despues de BluetoothAdapter.ACTION_DISCOVERY_FINISHED
	public static boolean fetchUuisWithSdp(BluetoothDevice device) {
		    try {
		    	Class<?> cl = Class.forName("android.bluetooth.BluetoothDevice");
		    	Class<?>[] par = {};
		    	Method method = cl.getMethod("fetchUuidsWithSdp", par);
		    	Object[] args = {};
		    	Boolean returnValue = (Boolean) method.invoke(device, args);
		        return returnValue.booleanValue();
		    } catch (Exception e) {
		    	Log.e("Constants", "error get UUID", e);
		        return false;
		    }
		}

	
	public static ProgressDialog createProgressDialog(Context context, String message)
	{

		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setIndeterminate(false);
		dialog.setMessage(message);
		return dialog;
	}
	
	public static void indeterminateInternal(Context context, final Handler handler, String message, final Runnable runnable,OnDismissListener dismissListener, boolean cancelable){

			final ProgressDialog dialog = createProgressDialog(context, message);
			dialog.setCancelable(cancelable);

			if (dismissListener != null)
			{

				dialog.setOnDismissListener(dismissListener);
			}

			dialog.show();

			new Thread() {

				@Override
				public void run()
				{
					runnable.run();

					handler.post(new Runnable() {

						public void run()
						{

							try
							{

								dialog.dismiss();
							}
							catch (Exception e)
							{

								; // nop.
							}

						}
					});
				};
			}.start();
		}
	
	
	/**
	 * String -> Hex
	 * 
	 * @param s
	 * @return
	 */
	public static String stringToHex(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String s4 = Integer.toHexString(ch);
			if (s4.length() == 1) {
				s4 = '0' + s4;
			}
			str = str + s4 + " ";
		}
		return str;
	}

	/**
	 * Hex -> String
	 * 
	 * @param s
	 * @return
	 */
	public static String hexToString(String s) {
		String[] strs = s.split(" ");
		byte[] baKeyword = new byte[strs.length];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(strs[i], 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "utf-8");// UTF-16le:Not
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}

	/**
	 * Hex -> Byte
	 * 
	 * @param s
	 * @return
	 * @throws Exception
	 */
	public static byte[] hexToByte(String s) throws Exception {
		if ("0x".equals(s.substring(0, 2))) {
			s = s.substring(2);
		}
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(
						i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
		return baKeyword;
	}

	/**
	 * Byte -> Hex
	 * 
	 * @param bytes
	 * @return
	 */
	public static String byteToHex(byte[] bytes, int count) {
		StringBuffer sb = new StringBuffer();
		synchronized (obj) {
			for (int i = 0; i < count; i++) {
				String hex = Integer.toHexString(bytes[i] & 0xFF);
				if (hex.length() == 1) {
					hex = '0' + hex;
				}
//				Log.d("MonitorActivity",i+":"+hex);
				sb.append(hex).append(" ");
			}
		}
		return sb.toString();
	}
	
	public static String byteToASCII(byte[] packetBytes, int bytesAvailable){
		final byte delimiter = 10; //This is the ASCII code for a newline character
		int readBufferPosition = 0;
        byte []readBuffer = new byte[1024];
        StringBuffer result = new StringBuffer();
        try
        {
			for (int i = 0; i < bytesAvailable; i++) {
				byte b = packetBytes[i];
				if (b == delimiter) 
				{
					byte[] encodedBytes = new byte[readBufferPosition];
					System.arraycopy(readBuffer, 0, encodedBytes, 0,encodedBytes.length);
					final String data = new String(encodedBytes, "US-ASCII");
					readBufferPosition = 0;
					result.append(data+Constants.END_LINE);
					
				} else {
					readBuffer[readBufferPosition++] = b;
				}
			}
			result.append(readBufferPosition);
        }catch (IOException e) {
			
		}
        return result.toString();
	}
}
