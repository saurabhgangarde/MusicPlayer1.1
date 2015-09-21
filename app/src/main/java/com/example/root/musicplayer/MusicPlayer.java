package com.example.root.musicplayer;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.root.musicplayer.Fragment.FileBrowsingFragment;
import com.example.root.musicplayer.Fragment.PlayListFragment;
import com.example.root.musicplayer.Fragment.PlayerFragment;
import com.example.root.musicplayer.modelClass.Media;
import com.example.root.musicplayer.playlistDB.PlaylistDbAdapter;

import java.util.ArrayList;

public class MusicPlayer extends Activity {

    public static String PLAYER_FRAGMENT = "playerFragment";
    public static String PLAYLIST_FRAGMENT = "playlist";
    public static String FILEBROWSER_FRAGMENT = "filebrowser";
    private ArrayList<Media> playlistArrayList = new ArrayList<>();
    private PlaylistDbAdapter dbHelper;

    // handler for received Intents for the "my-event" event
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            int currentPosition = intent.getIntExtra("message", 0);
            Log.e("s", "" + currentPosition);
            int maxDuration = intent.getIntExtra("duration", 0);
            PlayerFragment playerFragment = (PlayerFragment) getFragmentManager().findFragmentById(R.id.FragmentContainer);
            playerFragment.getCurrentDuration(currentPosition);

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_browsing_file);
        dbHelper = new PlaylistDbAdapter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_file_browsing, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.gotoList:
                PlayListFragment playListFragment = new PlayListFragment();
                FragmentManager fragmentManagerforPlaylist = getFragmentManager();
                if (getFragmentManager().findFragmentByTag(PLAYLIST_FRAGMENT) != null) {
                    FragmentTransaction fragmentTransactionforPlaylist = fragmentManagerforPlaylist.beginTransaction();
                    fragmentTransactionforPlaylist.remove(getFragmentManager().findFragmentByTag(PLAYLIST_FRAGMENT));
                    fragmentTransactionforPlaylist.replace(R.id.relativeLayout, playListFragment, PLAYLIST_FRAGMENT);
//                    fragmentTransactionforPlaylist.addToBackStack(null);
                    fragmentTransactionforPlaylist.commit();

                } else {
                    FragmentTransaction fragmentTransactionforPlaylist = fragmentManagerforPlaylist.beginTransaction();
                    fragmentTransactionforPlaylist.replace(R.id.relativeLayout, playListFragment, PLAYLIST_FRAGMENT);
                    fragmentTransactionforPlaylist.addToBackStack(null);
                    fragmentTransactionforPlaylist.commit();

                }

                break;

            case R.id.fileOpen:
                FileBrowsingFragment fileBrowsingFragment = new FileBrowsingFragment();
                FragmentManager fm = getFragmentManager();
                if (getFragmentManager().findFragmentByTag(FILEBROWSER_FRAGMENT) != null) {
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.remove(getFragmentManager().findFragmentByTag(FILEBROWSER_FRAGMENT));
                    fragmentTransaction.replace(R.id.relativeLayout, fileBrowsingFragment, FILEBROWSER_FRAGMENT);
//                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.relativeLayout, fileBrowsingFragment, FILEBROWSER_FRAGMENT);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                break;
            case R.id.addToPlaylist:

                dbHelper.open();

                dbHelper.addToPlayList(playlistArrayList);
//                PlayListFragment playlistfragment = new PlayListFragment();
//                FragmentManager manager = getFragmentManager();
//                if(getFragmentManager().findFragmentById(R.id.playlist)!=null)
//                {
//                FragmentTransaction transaction = manager.beginTransaction();
//                transaction.replace(R.id.relativeLayout, playlistfragment,PLAYLIST_FRAGMENT);
//
//                transaction.addToBackStack(null);
//                transaction.commit();
//                Toast.makeText(MusicPlayer.this,"Songs Added Successfully",Toast.LENGTH_SHORT).show();


                break;
        }
        return true;

    }

    public void updateUi(int position) {
        PlayerFragment playerFragment = (PlayerFragment) this.getFragmentManager().findFragmentById(R.id.FragmentContainer);
        playerFragment.setPosition(position);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Register mMessageReceiver to receive messages.
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("my-event"));
    }

    @Override
    public void onBackPressed() {

        Log.e("Count :::", "" + getFragmentManager().getBackStackEntryCount());
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            moveTaskToBack(true);
        } else {
            getFragmentManager().popBackStack();
        }
    }

    public void playlist(ArrayList<Media> modelClases) {
        playlistArrayList = modelClases;

    }


}