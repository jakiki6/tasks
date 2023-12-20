package de.laura.tasks;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Messenger;

import de.laura.tasks.service.TasksService;

public class TasksApp extends Application {
    public Messenger messenger;

    @Override
    public void onCreate() {
        super.onCreate();

        Intent intent = new Intent(this, TasksService.class);
        startService(intent);
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                TasksApp.this.messenger = new Messenger(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                TasksApp.this.messenger = null;
            }
        }, Context.BIND_AUTO_CREATE);
    }
}
