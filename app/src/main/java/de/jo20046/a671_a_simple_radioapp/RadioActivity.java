package de.jo20046.a671_a_simple_radioapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RadioActivity extends AppCompatActivity {

    MediaPlayer mp;
    final String[] items = {"1live", "dradio", "fusion101"};
    final String[] sources = {"https://1liveuni-lh.akamaihd.net/i/1LIVE_HDS@179577/index_1_a-p.m3u8",
            "https://st01.sslstream.dlf.de/dlf/01/128/mp3/stream.mp3", "http://hazel.torontocast.com:2910/;?type=http"};
    final String[] urls = {"https://www1.wdr.de/radio/1live/", "https://www.deutschlandradio.de/", "http://fusion101radio.com/"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.list_menu);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items));
        registerForContextMenu(listView);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            startMediaPlayer(position);
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(ContextMenu.NONE, 4711, 0, "Start");
        menu.add(ContextMenu.NONE, 4712, 1, "Stop");
        menu.add(ContextMenu.NONE, 4713, 2, "View Webpage");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 4711:
                startMediaPlayer(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
                break;
            case 4712:
                stopMediaPlayer();
                break;
            case 4713:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(urls[((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position]));
                startActivity(intent);
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 1, 0, "Stop Player");
        menu.add(Menu.NONE, 2, 1, "Exit App");
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                stopMediaPlayer();
                return true;
            case 2:
                exitApp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void startMediaPlayer(int position) {
        stopMediaPlayer();
        mp = new MediaPlayer();
        try {
            mp.setDataSource(sources[position]);

            // Version "blocking"
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void stopMediaPlayer() {
        if (mp != null) mp.release();
    }

    public void exitApp() {
        stopMediaPlayer();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mp != null) mp.release();
    }
}