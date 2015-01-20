package com.devoxx.espresso.samples.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.devoxx.espresso.samples.DevoxxApplication;
import com.devoxx.espresso.samples.R;
import com.devoxx.espresso.samples.model.Data;
import com.devoxx.espresso.samples.model.Picture;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PicturesFragment extends Fragment {

    private static final String ARG_PICTURES_TYPE = "com.devoxx.espresso.samples.ARG_PICTURES_TYPE";

    @InjectView(R.id.progress) ProgressBar progressBar;
    @InjectView(R.id.pictures_grid_view) GridView picturesGridView;

    private AsyncTask<Void, Void, Void> loadDataTask;
    private int mPicturesType;

    public PicturesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPicturesType = getArguments().getInt(ARG_PICTURES_TYPE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pictures_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
        //loadFromAsyncTask();
        loadFromRetrofit();
    }

    private void loadFromAsyncTask() {
        loadDataTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                long startTime = System.currentTimeMillis();
                while (System.currentTimeMillis() < startTime + 2000) ;
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Picture[] pictures = Data.getPicturesForType(mPicturesType);
                showPictures(pictures);
            }
        };
        loadDataTask.execute();
    }

    private void loadFromRetrofit() {
        DevoxxApplication.getPicturesApi().getPicturesForType(mPicturesType, new Callback<Picture[]>() {
            @Override
            public void success(Picture[] pictures, Response response) {
                showPictures(pictures);
            }

            @Override
            public void failure(RetrofitError error) {
                // No failure can occur
            }
        });

    }

    private void showPictures(Picture[] pictures) {
        picturesGridView.setAdapter(new PicturesAdapter(getActivity(), pictures));
        progressBar.setVisibility(View.GONE);
        picturesGridView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (loadDataTask != null) {
            loadDataTask.cancel(true);
        }
    }

    @OnItemClick(R.id.pictures_grid_view)
    public void onPictureItemClicked(View clickedView, int position) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_PICTURES_TYPE, mPicturesType);
        intent.putExtra(DetailActivity.EXTRA_POSITION, position);
        String transitionName = getString(R.string.transition_from_grid_to_detail);
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                        clickedView,
                        transitionName
                );
        ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
    }

    public static PicturesFragment newInstance(final int picturesType) {
        PicturesFragment picturesFragment = new PicturesFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARG_PICTURES_TYPE, picturesType);
        picturesFragment.setArguments(arguments);
        return picturesFragment;
    }
}
