package de.laura.tasks.service;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.caoccao.javet.exceptions.JavetException;

import java.util.Random;

public class ServiceHandler extends Handler {
    TasksService service;
    public static final int MSG_PUSHCODE = 0;
    public static final int RMSG_ERROR = -1;

    public ServiceHandler(Looper looper, TasksService service) {
        super(looper);

        this.service = service;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        if (msg.what == MSG_PUSHCODE) {
            try {
                this.service.reload((String) msg.obj);
            } catch (JavetException e) {
                e.printStackTrace();
            }
        }
    }
}
