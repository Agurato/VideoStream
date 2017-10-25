package fr.vmonot.videostream;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String folderPath;
    ListView filelist;
    ArrayAdapter<String>adapter;
     SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings = getSharedPreferences("Settings", Context.MODE_PRIVATE);

        folderPath =  settings.getString("serverDir", Environment.getExternalStorageState());
        filelist = (ListView) findViewById(R.id.FileList);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);
        filelist.setAdapter(adapter);
        filelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myintent = new Intent(MainActivity.this,VideoPlayerActivity.class);
                myintent.putExtra("VideoPath" , folderPath + File.separator+ adapter.getItem(position) );
                startActivity(myintent);
            }
        });
        refreshFileView();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onResume(){
        super.onResume();
        folderPath =  settings.getString("serverDir", Environment.getExternalStorageState());
        refreshFileView();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // When the user clicks FETCH, fetch the first 500 characters of
            // raw HTML from www.google.com.
            case R.id.fetch_action:

                return true;
            // Clear the text and cancel download.
            case R.id.action_settings:
                startSettings();
                return true;
        }
        return false;
    }

    private void startSettings()
    {
        startActivity(new Intent(this,VideoStreamSettingsActivity.class));
    }

    private void refreshFileView(){
        adapter.clear();
        File folder=new File(folderPath);
        for (final File fileEntry : folder.listFiles()) {
            if (! fileEntry.isDirectory())
                adapter.add(fileEntry.getName());
            }
        }


}
