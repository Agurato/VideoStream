package fr.vmonot.videostream;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class VideoStreamSettingsActivity extends AppCompatActivity {
	Switch enableServerSwitch;
	Button chooseDirButton;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_stream_settings);
	
		// Switch to turn on/off the server
		enableServerSwitch = (Switch) findViewById(R.id.enableServerSwitch);
		enableServerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				SharedPreferences settings = getSharedPreferences("Settings", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("enableServer", b);
				editor.apply();
			}
		});
	
		// Button to change the server directory
		chooseDirButton = (Button) findViewById(R.id.chooseDirButton);
		chooseDirButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Changes the server directory
				SharedPreferences settings = getSharedPreferences("Settings", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("serverDir", ((TextView) findViewById(R.id.directoryTextView)).getText().toString());
				editor.apply();
			}
		});
		
		/* Récupération settings:
		// We get the saved serverDir, or a default one
		SharedPreferences settings = getSharedPreferences("Settings", Context.MODE_PRIVATE);
		String dir = settings.getString("serverDir", some default dir here);
		 */
    }
}
