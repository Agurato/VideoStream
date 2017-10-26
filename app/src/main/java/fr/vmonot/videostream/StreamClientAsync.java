package fr.vmonot.videostream;

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

public class StreamClientAsync  extends AsyncTask<String , Integer , Boolean>   {
     private Socket socket;
    private String host;
    private int port;
    private WifiDirectActivity app;
    private String destPath;
    private int timeout;
    byte[] BUFF = new byte[512];
    public StreamClientAsync(WifiDirectActivity app, String destPath ,String host, int port , int timeout){
        this.socket  = new Socket();
        this.host = host;
        this.port = port;
        this.timeout = timeout;
		this.destPath = destPath;
		this.app = app;
    }

    @Override
    protected Boolean doInBackground(String... params) {

        try {
			Log.d("WifiDirectActivity", host+":"+port);
            socket.bind(null);
            socket.connect((new InetSocketAddress(host, port)), timeout);
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