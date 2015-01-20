package com.devoxx.espresso.samples.test;

import android.net.Uri;
import android.support.test.espresso.IdlingResource;

import com.devoxx.espresso.samples.DevoxxApplication;
import com.devoxx.espresso.samples.api.PicturesApi;
import com.devoxx.espresso.samples.core.JacksonConverter;
import com.devoxx.espresso.samples.model.Data;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.android.MainThreadExecutor;
import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

public class TestUtils {

    public static class EspressoThreadPool extends ThreadPoolExecutor implements IdlingResource {

        private int threadCount = 0;
        private ResourceCallback resourceCallback;

        public EspressoThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                  BlockingQueue<Runnable> workQueue,
                                  ThreadFactory threadFactory) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        }

        @Override
        public synchronized void execute(Runnable r) {
            threadCount++;
            super.execute(r);
        }

        @Override
        protected synchronized void afterExecute(Runnable r, Throwable t) {
            super.afterExecute(r, t);
            threadCount--;
            if(resourceCallback != null){
                resourceCallback.onTransitionToIdle();
            }
        }

        @Override
        public String getName() {
            return "EspressoThreadPool";
        }

        @Override
        public boolean isIdleNow() {
            return threadCount == 0;
        }

        @Override
        public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
            this.resourceCallback = resourceCallback;
        }
    }

    /* Thread pool to execute network operations */
    static EspressoThreadPool espressoThreadPool = newAndroidCachedThreadPool();

    /* Thread executor that executes callbacks on main thread  */
    static MainThreadExecutor mainThreadExecutor = new MainThreadExecutor();

    private static EspressoThreadPool newAndroidCachedThreadPool() {
        return new EspressoThreadPool(5, 5, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(final Runnable r) {
                return new Thread(new Runnable() {
                    @Override
                    public void run() {
                        android.os.Process.setThreadPriority(THREAD_PRIORITY_BACKGROUND);
                        r.run();
                    }
                }, "Idle");
            }
        }
        );
    }

    public static void injectPicturesApi() {
        RestAdapter.Builder restAdapterBuilder = new RestAdapter.Builder()
                .setClient(new OkClient())
                .setExecutors(espressoThreadPool, mainThreadExecutor)
                .setEndpoint("http://www.my-mock-server")
                .setClient(new Client() {
                    @Override
                    public Response execute(Request request) throws IOException {
                        Uri uri = Uri.parse(request.getUrl());
                        long startTime = System.currentTimeMillis();
                        while (System.currentTimeMillis() < startTime + 2000) ;
                        String responseString = JacksonConverter.MAPPER.writeValueAsString(Data.getPicturesForType(Integer.valueOf(uri.getLastPathSegment())));
                        return new Response(request.getUrl(), 200, "nothing", Collections.EMPTY_LIST, new TypedByteArray("application/json", responseString.getBytes()));
                    }
                })
                .setConverter(new JacksonConverter());

        DevoxxApplication.setPicturesApi(restAdapterBuilder.build().create(PicturesApi.class));
    }
}
