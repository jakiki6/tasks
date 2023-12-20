package de.laura.tasks.bindings;

import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.values.reference.V8ValueObject;

import de.laura.tasks.service.TasksService;

public abstract class Binding {
    V8Runtime runtime;
    TasksService service;
    V8ValueObject rootObject;


    public Binding(V8Runtime runtime, TasksService service) throws JavetException {
        this.runtime = runtime;
        this.service = service;

        rootObject = runtime.createV8ValueObject();
        rootObject.bind(this);
        runtime.getGlobalObject().set(getObjectName(), rootObject);
    }

    abstract String getObjectName();
}
