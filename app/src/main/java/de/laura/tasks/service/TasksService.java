package de.laura.tasks.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Messenger;
import android.widget.Toast;

import de.laura.tasks.ui.MainActivity;
import de.laura.tasks.R;
import de.laura.tasks.bindings.Bindings;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interop.V8Host;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.values.reference.V8Script;
import com.caoccao.javet.values.reference.V8ValueFunction;

import java.util.ArrayList;
import java.util.HashMap;

public class TasksService extends Service {
    ServiceHandler handler;
    GlobalState globalState;

    V8Runtime runtime;
    V8Script scripts;
    Bindings bindings;
    public HashMap<String, ArrayList<V8ValueFunction>> events;

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("Tasks", Thread.NORM_PRIORITY);
        thread.start();

        handler = new ServiceHandler(thread.getLooper(), this);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        Intent stopIntent = new Intent(this, MainActivity.class);
        stopIntent.putExtra("action", "stop");

        NotificationChannel channel = new NotificationChannel("tasks", getString(R.string.channel_name), NotificationManager.IMPORTANCE_LOW);
        channel.setDescription(getString(R.string.channel_description));
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        PendingIntent pendingIntentOpen = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        PendingIntent pendingIntentStop = PendingIntent.getActivity(this, 0, stopIntent, PendingIntent.FLAG_IMMUTABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(1, new NotificationCompat.Builder(this,
                    "tasks") // don't forget create a notification channel first
                    .setOngoing(true)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Service is running background")
                    .setContentIntent(pendingIntentOpen)
                    .addAction(new NotificationCompat.Action(0, "Stop", pendingIntentStop))
                    .build(), ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE);
        } else {
            startForeground(1, new NotificationCompat.Builder(this,
                    "tasks") // don't forget create a notification channel first
                    .setOngoing(true)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Service is running background")
                    .setContentIntent(pendingIntentOpen)
                    .addAction(new NotificationCompat.Action(0, "Stop", pendingIntentStop))
                    .build());
        }

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Tasks service killed!", Toast.LENGTH_SHORT).show();

        try {
            runtime.close();
        } catch (JavetException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Messenger(handler).getBinder();
    }

    public void reload(String code) throws JavetException {
        if (scripts != null) {
            scripts.close();

            scripts = null;
        }

        if (runtime != null) {
            runtime.close();

            runtime = null;
        }

        runtime = V8Host.getV8Instance().createV8Runtime();
        events = new HashMap<>();
        bindings = new Bindings(runtime, this);
        scripts = runtime.getExecutor(code).compileV8Script();

        scripts.execute();
    }
}
