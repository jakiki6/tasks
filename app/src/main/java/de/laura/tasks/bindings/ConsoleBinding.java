package de.laura.tasks.bindings;

import android.widget.Toast;

import com.caoccao.javet.annotations.V8Function;
import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.values.reference.V8ValueObject;

import de.laura.tasks.service.TasksService;

public class ConsoleBinding extends Binding {
    public ConsoleBinding(V8Runtime runtime, TasksService service) throws JavetException {
        super(runtime, service);
    }

    @V8Function
    public void log(String msg) {
        Toast.makeText(this.service, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    String getObjectName() {
        return "console";
    }
}
