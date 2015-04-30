package com.example.drice.fuzzchallenge.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.drice.fuzzchallenge.R;
import com.example.drice.fuzzchallenge.util.Constants;
import com.example.drice.fuzzchallenge.util.MyVolleySingleton;

/**
 * Activity to display full screen of image clicked in listview
 * Created by DrIce on 4/29/15.
 */
public class FullImageActivity extends Activity {

    private NetworkImageView imageView;
    private ProgressBar spinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullimage);

        String url = getIntent().getStringExtra(Constants.DATA_KEY);

        imageView = (NetworkImageView) findViewById(R.id.imageView);
        spinner = (ProgressBar) findViewById(R.id.progressSpinnerFullImage);

        imageView.setErrorImageResId(R.drawable.error);
        ImageLoader mImageLoader = MyVolleySingleton.getInstance(this).getImageLoader();
        mImageLoader.get(url, new ImageLoader.ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                spinner.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    // code to switch out placeholder animation
                    // progress wheel with received response
                    spinner.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                }
            }
        });

        imageView.setImageUrl(url, mImageLoader);

    }

}
