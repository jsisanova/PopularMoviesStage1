package com.example.android.popularmoviesstage1.database;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Global executor pools for the whole application.
 *
 * Grouping tasks like this avoids the effects of task starvation (e.g. disk reads don't wait behind
 * webservice requests).
 */
public class AppExecutors {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static AppExecutors sInstance;
    // We will use only diskIO executor
    private final Executor diskIO;
    private final Executor mainThread;
    private final Executor networkIO;

    private AppExecutors (Executor diskIO, Executor networkIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    //    Singleton
    public static AppExecutors getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                // diskIO is single thread executor, ensured DB transactions are done in order and we don't have race conditions
                sInstance = new AppExecutors (Executors.newSingleThreadExecutor (),
                // network IO executor is a pool of 3 threads, allows to run network calls simultaneously
                Executors.newFixedThreadPool (3),
                // mainThread executor uses MainThreadExacutor class
                // when we are in activity, we don't need it, bcs we can use the runOnUiThread() method
                // when we are in different class and we don't have runOnUiThread() method, we can access the main thread using this executor
                new MainThreadExecutor());
            }
        }
        return sInstance;
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    public Executor networkIO() {
        return networkIO;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper ());
        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post (command);
        }
    }
}