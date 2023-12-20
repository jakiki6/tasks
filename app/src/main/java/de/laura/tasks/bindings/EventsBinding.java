package de.laura.tasks.bindings;

import com.caoccao.javet.annotations.V8Function;
import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.values.reference.V8ValueFunction;

import java.util.ArrayList;

import de.laura.tasks.service.TasksService;

public class EventsBinding extends Binding {
    public EventsBinding(V8Runtime runtime, TasksService service) throws JavetException {
        super(runtime, service);
    }

    @V8Function
    public void register(String type, V8ValueFunction function) {
        if (!service.events.containsKey(type)) {
            service.events.put(type, new ArrayList<>());
        }

        service.events.get(type).add(function);
    }

    @Override
    String getObjectName() {
        return "events";
    }
}
