package com.example.drice.fuzzchallenge.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.drice.fuzzchallenge.R;
import com.example.drice.fuzzchallenge.adapter.ListviewAdapter;
import com.example.drice.fuzzchallenge.model.ContentListSingleton;
import com.example.drice.fuzzchallenge.model.ListviewContent;
import com.example.drice.fuzzchallenge.util.Constants;
import com.example.drice.fuzzchallenge.util.JsonParser;
import com.example.drice.fuzzchallenge.util.MyVolleySingleton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by DrIce on 4/29/15.
 */
public class BaseFragment extends ListFragment {

    private ListviewAdapter mAdapter;
    protected ListView listView;
    protected ArrayList<ListviewContent> itemList;
    protected String listType;
    private Callbacks callbacks;

    RequestQueue queue;

    //For hosting activities
    public interface Callbacks {
        void onListItemSelected(ListviewContent item);
    }

    public BaseFragment() {

    }

    /**
     * New instance for fragment. Param type used to determine the contents of the listview
     * @param type
     * @return fragment
     */
    public static BaseFragment newInstance(String type) {
        BaseFragment fragment = new BaseFragment();

        Bundle args = new Bundle();
        args.putString(Constants.TYPE_KEY, type);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        listType = getArguments().getString(Constants.TYPE_KEY);

        startFetchJson();
        if(listType.equals(Constants.ALL_KEY) || listType==null) {
            itemList = ContentListSingleton.get().getCompleteList();
        } else {
            itemList = ContentListSingleton.get().getListFromMap(listType);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        listView = (ListView)rootView.findViewById(android.R.id.list);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbacks = (Callbacks)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("BaseFragment", "tab - " + listType + " onresume ");
        if(getListAdapter() != null) {
            ((ListviewAdapter) getListAdapter()).notifyDataSetChanged();
            Log.d("BaseFragment", " notifydata set changed ");
        }
    }

    @Override
    public void onListItemClick(ListView list, View view, int position, long id) {
        ListviewContent item = ((ListviewAdapter)getListAdapter()).getItem(position);

        callbacks.onListItemSelected(item);
    }

    /**
     * Fetches latest JSON data using volley and updates ContentListSingleton once it is retrieved
     */
    public void startFetchJson() {
        //Commented code below would be used instead if using asynctask instead of volley to fetch json
        //new ListViewFetchTask().execute();

        queue = Volley.newRequestQueue(getActivity());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Constants.ENDPOINT_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            Gson gson = new Gson();
                            ArrayList<ListviewContent> items = new ArrayList<>();
                            for(int ind = 0; ind < jsonArray.length(); ind++) {
                                ListviewContent item;
                                item = gson.fromJson(jsonArray.get(ind).toString(), ListviewContent.class);
                                items.add(item);
                            }
                            ContentListSingleton.get().addListviewItemList(items);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        updateAdapter();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.getMessage());

            }
        });

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(10*1000, 5, 1.0f));
        MyVolleySingleton.getInstance(getActivity()).addToRequestQueue(jsonArrayRequest);
    }

    /**
     * updates listadapter with latest itemList by retrieving it from singleton
     */
    public void updateAdapter() {
        if (listView == null) {
            Log.d("TAG", "list view null");
            return;
        }

        //if listtype is all or null, then get entire content list (defaulting to this if null)
        if(listType.equals("all") || listType == null) {
            itemList = ContentListSingleton.get().getCompleteList();
        } else {
            itemList = ContentListSingleton.get().getListFromMap(listType);
        }
        if(itemList == null) {
            setListAdapter(null);
        } else {
            mAdapter = new ListviewAdapter(this.getActivity(), R.layout.listitem, itemList);
            setListAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }



    /**
     * Fetch Task to get content for listview. Not needed anymore because of volley, just including this in
     * here to show how to do it without volley.
    */
    @Deprecated
    private class ListViewFetchTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        private ListViewFetchTask() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (isCancelled()) {
                return null;
            }
            ContentListSingleton contentListSingleton = ContentListSingleton.get();
            ArrayList<ListviewContent> newList = new JsonParser().fetchItems();
            contentListSingleton.addListviewItemList(newList);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            updateAdapter();
            progressDialog.dismiss();
        }

    }


}



