package fr.vmonot.videostream;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static android.R.attr.host;
import static android.R.attr.inputType;

/**
 * Created by antoine on 25/10/17.
 */

public class StreamBTClientAsync extends AsyncTask<String , Integer , Boolean>   {
     private Socket socket;

    private BluetoothActivity app;
    private String destPath;

    byte[] BUFF = new byte[512];
    BluetoothDevice mdevice;
    public StreamBTClientAsync(BluetoothActivity app, BluetoothDevice mdevice , String destPath ){
        this.socket  = new Socket();

		this.destPath = destPath;
		this.app = app;
         this.mdevice = mdevice;
    }

    @Override
    protected Boolean doInBackground(String... params) {

        try {
            BluetoothSocket serverSocket = null;

            // Create a new listening server socket
            try {
                serverSocket = mdevice.createInsecureRfcommSocketToServiceRecord(MainActivity.uuid);
            } catch (IOException e) {
                Log.e("BluetoothActivity", "Socket Type: "  + "listen() failed", e);
            }
			Log.d("WifiDirectActivity", "after connect");
            InputStream  is = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            WiFiTransferModal  wifiinfo = (WiFiTransferModal) ois.readObject();
			Log.d("WifiDirectActivity", "fileinfo acquired");
            app.OnFileInfoAvailable(wifiinfo);
			Log.d("WifiDirectActivity", "after callback");
            File f = new File(destPath + File.separator+ wifiinfo.getFileName()+wifiinfo.getExtension());
            if(f.exists() && !f.isDirectory()) {
                f.delete();
            }
            FileOutputStream fos = new FileOutputStream( destPath + File.separator+ wifiinfo.getFileName()+wifiinfo.getExtension(), true);
            int len;

            while((len = is.read(BUFF)) != -1){
              fos.write(BUFF, 0, len);
            }
			
        } catch (IOException | ClassNotFoundException  e  ){
			Log.d("WifiDirectActivity", e.toString());
        }


        return null;
    }
}
