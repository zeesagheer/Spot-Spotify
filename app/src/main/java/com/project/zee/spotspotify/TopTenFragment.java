package com.project.zee.spotspotify;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;


/**
 * A placeholder fragment containing a simple view.
 */
public class TopTenFragment extends Fragment {

    public static CustomAdapter mTrackAdapter;
    public static ArrayList<String> mAlbumIcon = new ArrayList<>();
    public static ArrayList<String> mAlbumName = new ArrayList<>();

    public TopTenFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_top_ten, container, false);
        mTrackAdapter = new CustomAdapter(getActivity(),new ArrayList<String>(), mAlbumIcon, mAlbumName);

        ListView listView = (ListView) rootView.findViewById(R.id.list_id);
        listView.setAdapter(mTrackAdapter);

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_REFERRER_NAME)) {
            String artistId = intent.getStringExtra(Intent.EXTRA_REFERRER_NAME);
            new SpotNetwork().execute(artistId);
        }
        return rootView;
    }
    class SpotNetwork extends AsyncTask<String, Void, Tracks> {
        private final String LOG_TAG = SpotNetwork.class.getSimpleName();

        protected Tracks doInBackground(String... top10) {
            if (top10.length == 0) {
                return null;
            }
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            Map<String, Object> map = new HashMap<>();
            map.put("country","SE");
            Tracks track = spotify.getArtistTopTrack(top10[0], map);
            
            return track;
        }
        @Override
        protected void onPostExecute(Tracks results) {
            mTrackAdapter.customClear();
            for (Track track : results.tracks) {
                try {
                    if (track.album.images.size() == 0) {
                        mTrackAdapter.add(track.name, null, track.album.name);
                    }
                    else {
                        mTrackAdapter.add(track.name,track.album.images.get(2).url,track.album.name);
                    }
                } catch (IndexOutOfBoundsException e) {
                    Log.v("MainActivityFragment", e + ": No Image");
                }
            }
        }
    }
}
