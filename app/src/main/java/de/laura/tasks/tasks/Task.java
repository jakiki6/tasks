package de.laura.tasks.tasks;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import de.laura.tasks.steps.Step;
import de.laura.tasks.steps.Steps;

public class Task {
    public Step rootStep;
    public GlobalState globalState;
    public ReentrantLock lock = new ReentrantLock();

    public Task(Step root_step, GlobalState globalState) {
        this.rootStep = root_step;
        this.globalState = globalState;
    }

    public void run() {
        this.lock.lock();
        this.rootStep.run();
        this.lock.unlock();
    }

    public String serialize() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("root_step", this.rootStep.serialize());

        return object.toString();
    }

    public static Task deserialize(JSONObject object, GlobalState globalState) throws JSONException {
        Task task = new Task(null, null);
        task.globalState = globalState;
        task.rootStep = Steps.deserialize(object.getJSONObject("root_step"), task);

        return task;
    }
}
