package de.laura.tasks.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.widget.Toast;

import de.laura.tasks.MainActivity;
import de.laura.tasks.R;
import de.laura.tasks.tasks.GlobalState;
import de.laura.tasks.tasks.Task;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;

public class TasksService extends Service {
    ServiceHandler handler;
    GlobalState globalState;
    ArrayList<Task> tasks;

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("Tasks", Thread.NORM_PRIORITY);
        thread.start();

        handler = new ServiceHandler(thread.getLooper(), this);

        Intent notificationIntent = new Intent(this, MainActivity.class);

        NotificationChannel channel = new NotificationChannel("tasks", getString(R.string.channel_name), NotificationManager.IMPORTANCE_LOW);
        channel.setDescription(getString(R.string.channel_description));
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(1, new NotificationCompat.Builder(this,
                    "tasks") // don't forget create a notification channel first
                    .setOngoing(true)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Service is running background")
                    .setContentIntent(pendingIntent)
                    .build(), ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE);
        } else {
            startForeground(1, new NotificationCompat.Builder(this,
                    "tasks") // don't forget create a notification channel first
                    .setOngoing(true)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Service is running background")
                    .setContentIntent(pendingIntent)
                    .build());
        }

        reload();
        getSharedPreferences("tasks", MODE_PRIVATE).registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, @Nullable String key) {
                reload();
            }
        });

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Toast.makeText(this, "Tasks service killed!", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Messenger(handler).getBinder();
    }

    public void reload() {
        for (Task task : tasks) {
            task.lock.lock();
        }

        SharedPreferences prefs = this.getSharedPreferences("tasks", Context.MODE_PRIVATE);
        try {
            globalState = GlobalState.deserialize(new JSONObject(prefs.getString("global_state", "{'vars':{}}")), this);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        tasks = new ArrayList<>();
        for (String taskString : prefs.getStringSet("tasks", Set.of())) {
            try {
                tasks.add(Task.deserialize(new JSONObject(taskString), globalState));
            } catch (JSONException ignored) {}
        }
    }
}
