package com.example.drice.fuzzchallenge.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.drice.fuzzchallenge.R;
import com.example.drice.fuzzchallenge.model.ListviewContent;
import com.example.drice.fuzzchallenge.util.MyVolleySingleton;
import com.example.drice.fuzzchallenge.view.AnimateInNetworkImageView;

import java.util.ArrayList;

/**
 * Custom adapter for listview
 * Created by DrIce on 4/29/15.
 */
public class ListviewAdapter extends ArrayAdapter<ListviewContent> {

    Context mContext;
    ArrayList<ListviewContent> mItems;
    ImageLoader mImageLoader;

    public ListviewAdapter(Context context, int resource, ArrayList<ListviewContent> items) {
        super(context, resource);
        mItems = items;
        mContext = context;
        mImageLoader = MyVolleySingleton.getInstance(context).getImageLoader();
    }

    public class ViewHolder {
        AnimateInNetworkImageView networkImageView;
        TextView textView;
        ProgressBar spinner;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.listitem, null);
            holder.networkImageView = (AnimateInNetworkImageView) convertView
                    .findViewById(R.id.networkImageView);
            holder.textView = (TextView) convertView.findViewById(R.id.text);
            holder.spinner = (ProgressBar) convertView.findViewById(R.id.progressSpinner);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        final ListviewContent item = mItems.get(position);

        //Setting for values networkimageview (error image, view bounds)
        holder.networkImageView.setErrorImageResId(R.drawable.error);
        holder.networkImageView.setAdjustViewBounds(true);

        //Handling what to display based on the "type" value of current item
        if(item.getType().equals("image")) {
            holder.spinner.setVisibility(View.VISIBLE);
            mImageLoader.get(item.getData(), new ImageLoader.ImageListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // handle errors
                }

                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    if (response.getBitmap() != null) {
                        // code to switch out placeholder animation
                        // progress wheel with received response
                        holder.spinner.setVisibility(View.GONE);
                        holder.networkImageView.setVisibility(View.VISIBLE);
                    }
                }
            });
            holder.networkImageView.setImageUrl(item.getData(), mImageLoader);
            holder.textView.setVisibility(View.GONE);
            //holder.networkImageView.setVisibility(View.VISIBLE);

        }
        else if(item.getType().equals("text")) {
            holder.textView.setText(item.getData());
            holder.networkImageView.setVisibility(View.GONE);
            holder.textView.setVisibility(View.VISIBLE);
            holder.spinner.setVisibility(View.GONE);
        }
        Log.d("ADAPTER", "position is " + position);
        return convertView;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public ListviewContent getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
