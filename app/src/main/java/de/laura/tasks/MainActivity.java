package de.laura.tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import de.laura.tasks.service.ServiceHandler;
import de.laura.tasks.service.TasksService;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(view -> {
            if (getMessenger() != null) {
                Message msg = new Message();
                msg.what = 0;
                msg.obj = "Hello from service!";
                try {
                    getMessenger().send(msg);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    Messenger getMessenger() {
        return ((TasksApp) getApplication()).messenger;
    }
}