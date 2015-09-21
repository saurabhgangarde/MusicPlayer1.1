package com.example.root.musicplayer.Fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.root.musicplayer.Adapter.PlayListAdapter;
import com.example.root.musicplayer.MusicPlayer;
import com.example.root.musicplayer.modelClass.Media;
import com.example.root.musicplayer.playlistDB.PlaylistDbAdapter;

import java.util.ArrayList;

public class PlayListFragment extends ListFragment {
    private ArrayList<Media> playlistArrayList = new ArrayList<Media>();
    private PlayListAdapter playListAdapter;
    private PlaylistDbAdapter dbHelper;
    private MusicPlayer musicPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new PlaylistDbAdapter(getActivity());
        addDataTolist();
    }

    private void addDataTolist() {
        playlistArrayList.clear();
        playlistArrayList.addAll(dbHelper.readPlaylist());

        playListAdapter = new PlayListAdapter(getActivity(), playlistArrayList);
        playListAdapter.notifyDataSetChanged();
        setListAdapter(playListAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        musicPlayer = (MusicPlayer) activity;
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        long rowid = playlistArrayList.get(position).get_id();
        int pos = position;
//        Cursor cursor = dbHelper.fetchSongPath(rowid);
//        String songpath = cursor.getString(cursor.getColumnIndex(PlaylistDbAdapter.KEY_SONG_PATH));
//        String songTitle = cursor.getString(cursor.getColumnIndex(PlaylistDbAdapter.KEY_SONG_TITLe));
//        int duration = cursor.getInt(cursor.getColumnIndex(PlaylistDbAdapter.KEY_MAX_DURATION));
        PlayerFragment playerFragmentFragment = new PlayerFragment();
        FragmentManager fragmentm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentm.beginTransaction();
        Bundle bundle = new Bundle();
//        bundle.putString("mp3Path", songpath);
//        bundle.putString("Mp3Name", songTitle);
//        bundle.putInt("duration", duration);
        bundle.putInt("position", pos);

        bundle.putParcelableArrayList("playlistarray", playlistArrayList);
        playerFragmentFragment.setArguments(bundle);
//        Fragment player = getFragmentManager().findFragmentByTag(MusicPlayer.FILEBROWSER_FRAGMENT);
//        if (player!= null) {
//            fragmentTransaction.remove(getFragmentManager().findFragmentByTag(MusicPlayer.FILEBROWSER_FRAGMENT));
//            fragmentTransaction.replace(R.id.relativeLayout, playerFragmentFragment, MusicPlayer.FILEBROWSER_FRAGMENT);
//            fragmentTransaction.commit();
//        } else {
//            fragmentTransaction.replace(R.id.relativeLayout, playerFragmentFragment, MusicPlayer.FILEBROWSER_FRAGMENT);
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();
//        }
        musicPlayer.updateUi(position);
        FragmentManager fragmentManagerforPlaylist = getFragmentManager();
        FragmentTransaction fragmentTransactionforPlaylist = fragmentManagerforPlaylist.beginTransaction();
        fragmentTransactionforPlaylist.remove(getFragmentManager().findFragmentByTag(MusicPlayer.PLAYLIST_FRAGMENT));
        fragmentTransactionforPlaylist.commit();
        super.onListItemClick(l, v, position, id);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().setBackgroundColor(Color.WHITE);
        getView().setClickable(true);
    }

}
