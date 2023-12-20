package de.laura.tasks.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

import java.util.Objects;

import de.laura.tasks.R;
import de.laura.tasks.TasksApp;
import de.laura.tasks.service.ServiceHandler;
import de.laura.tasks.service.TasksService;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent().hasExtra("action")) {
            Toast.makeText(this, getIntent().getStringExtra("action"), Toast.LENGTH_SHORT).show();
        }

        if (getIntent().hasExtra("action") && Objects.equals(getIntent().getStringExtra("action"), "stop")) {
            stopService(new Intent(this, TasksService.class));
            finish();
        }

        findViewById(R.id.button).setOnClickListener(view -> {
            if (getMessenger() != null) {
                Message msg = new Message();
                msg.what = ServiceHandler.MSG_PUSHCODE;
                msg.obj = "console.log('test');";
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