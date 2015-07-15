package com.project.zee.spotspotify;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public static CustomAdapter mArtistAdapter;
    private static String[] mArtistId;
    public static ArrayList<String> mArtistIcon = new ArrayList<>();
    View rootView;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) rootView.findViewById(R.id.search_body);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(false);

        mArtistAdapter = new CustomAdapter(getActivity(),new ArrayList<String>(), mArtistIcon);
        ListView listview = (ListView) rootView.findViewById(R.id.list_id_main);
        listview.setAdapter(mArtistAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getActivity(), TopTen.class).
                        putExtra(Intent.EXTRA_TEXT, adapterView.getItemAtPosition(i).toString()).
                        putExtra(Intent.EXTRA_REFERRER_NAME, mArtistId[i]));
            }
        });
        return rootView;
    }

    class SpotNetwork extends AsyncTask<String, Void, ArtistsPager>{
        private final String LOG_TAG = SpotNetwork.class.getSimpleName();

        protected ArtistsPager doInBackground(String... search){
            if (search.length == 0) {
                return null;
            }
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            ArtistsPager results = spotify.searchArtists(search[0]);
            return results;
        }
        @Override
        protected void onPostExecute(ArtistsPager results) {
            mArtistId = new String[results.artists.items.size()];
            mArtistAdapter.clear();
            int i = 0;
            for (Artist artist : results.artists.items) {

                try {
                    if (artist.images.size() == 0) {
                        mArtistAdapter.add(artist.name,null);
                    }
                    else {
                        mArtistAdapter.add(artist.name, artist.images.get(2).url);
                    }
                    mArtistId[i++] = artist.id;

                } catch (IndexOutOfBoundsException e) {
                    Log.v("MainActivityFragment", e + ": No Image");
                }
            }
        }
    }

    public void search(String query){
        new SpotNetwork().execute(query);
    }
}
