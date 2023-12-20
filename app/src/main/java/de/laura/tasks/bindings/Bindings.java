package de.laura.tasks.bindings;

import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interop.V8Runtime;

import de.laura.tasks.service.TasksService;

public class Bindings {
    V8Runtime runtime;
    TasksService service;
    ConsoleBinding consoleBinding;

    public Bindings(V8Runtime runtime, TasksService service) throws JavetException {
        this.runtime = runtime;
        this.service = service;

        this.consoleBinding = new ConsoleBinding(runtime, service);
    }
}
