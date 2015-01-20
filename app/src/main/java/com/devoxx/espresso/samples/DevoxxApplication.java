package com.devoxx.espresso.samples;

import android.app.Application;
import android.net.Uri;

import com.devoxx.espresso.samples.api.PicturesApi;
import com.devoxx.espresso.samples.core.JacksonConverter;
import com.devoxx.espresso.samples.model.Data;

import java.io.IOException;
import java.util.Collections;

import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class DevoxxApplication extends Application {

    private static PicturesApi picturesApi;

    public static PicturesApi getPicturesApi() {
        return picturesApi;
    }

    public static void setPicturesApi(PicturesApi picturesApi) {
        DevoxxApplication.picturesApi = picturesApi;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if(picturesApi == null){
            picturesApi = new RestAdapter.Builder().setEndpoint("http://www.mockserver")
                    .setConverter(new JacksonConverter())
                    .setClient(new Client() {
                        @Override
                        public Response execute(Request request) throws IOException {
                            Uri uri = Uri.parse(request.getUrl());
                            long startTime = System.currentTimeMillis();
                            while (System.currentTimeMillis() < startTime + 2000) ;
                            String responseString = JacksonConverter.MAPPER.writeValueAsString(Data.getPicturesForType(Integer.valueOf(uri.getLastPathSegment())));
                            return new Response(request.getUrl(), 200, "nothing", Collections.EMPTY_LIST, new TypedByteArray("application/json", responseString.getBytes()));
                        }
                    }).build().create(PicturesApi.class);
        }
    }


}
