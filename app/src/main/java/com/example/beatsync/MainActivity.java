package com.example.beatsync;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listview;
    String[] items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listview = findViewById(R.id.listViewSong);
        runtimePermission();
    }
    public void runtimePermission()
    {
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        displaysongs();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                        permissionToken.continuePermissionRequest();

                    }
                }).check();
    }


    public ArrayList<File> findsong(File file)
    {
        ArrayList<File> arrayList = new ArrayList<>();

        File[] files = file.listFiles();

        for(File singlefile : files)
        {
            if(singlefile.isDirectory() && !singlefile.isHidden())
            {
                arrayList.addAll(findsong(singlefile));
            }
            else{
                if(singlefile.getName().endsWith(".mp3") || singlefile.getName().endsWith(".wav")){
                    arrayList.add(singlefile);
                }
            }
        }

        return arrayList;

    }

    void displaysongs()
    {
        final ArrayList<File> mysongs = findsong(Environment.getExternalStorageDirectory());

        items = new String[mysongs.size()];

        for(int i=0;i<mysongs.size();i++){

            items[i] = mysongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");

        }

      /*  ArrayAdapter<String> myadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);
        listview.setAdapter(myadapter);
            */
        customAdapter customAdapter = new customAdapter();
        listview.setAdapter(customAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String songName =  (String) listview.getItemAtPosition(i);
                startActivity(new Intent(getApplicationContext(),PlayerActivity.class)
                .putExtra("songs", mysongs)
                .putExtra("songname",songName)
                .putExtra("pos",i));
            }
        });

    }

    class customAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            View myview = getLayoutInflater().inflate(R.layout.list_item,null);
            TextView textsong = myview.findViewById(R.id.txtsongname);
            textsong.setSelected(true);
            textsong.setText(items[i]);

            return myview;
        }
    }





}