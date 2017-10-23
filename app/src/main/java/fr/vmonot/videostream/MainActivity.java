package fr.vmonot.videostream;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.net.Uri;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {
	private VideoView vidView;
	private MediaController vidControl;
	private MediaPlayer mediaPlayer;
	private int pos = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Get a reference to the VideoView instance as follows, using the id we set in the XML layout.
		vidView = (VideoView)findViewById(R.id.video);
		
		// Add playback controls.
		vidControl = new MediaController(this);
		// Set it to use the VideoView instance as its anchor.
		vidControl.setAnchorView(vidView);
		// Set it as the media controller for the VideoView object.
		vidView.setMediaController(vidControl);
		
		// Prepare the URI for the endpoint.
		String vidAddress = "android.resource://" + getPackageName() + "/" + R.raw.video;
		Uri vidUri = Uri.parse(vidAddress);
		// Parse the address string as a URI so that we can pass it to the VideoView object.
		vidView.setVideoURI(vidUri);
		// Start playback.
		vidView.start();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		vidView.pause();
		pos = vidView.getCurrentPosition();
		Log.d("MainActivity", "Pause at "+pos+"/"+vidView.getDuration());
	}
	
	@Override
	public void onResume() {
		super.onResume();
		vidView.seekTo(pos);
		Log.d("MainActivity", "Restart at "+pos);
	}
}
