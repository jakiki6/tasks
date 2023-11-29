package de.laura.tasks.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Set;

import de.laura.tasks.tasks.GlobalState;
import de.laura.tasks.tasks.Task;

public class ServiceHandler extends Handler {
    TasksService service;

    public ServiceHandler(Looper looper, TasksService service) {
        super(looper);

        this.service = service;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {

    }
}
