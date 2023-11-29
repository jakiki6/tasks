package de.laura.tasks.steps;

import org.json.JSONException;
import org.json.JSONObject;

import de.laura.tasks.tasks.Task;

public abstract class Step {
    Task root;

    public Step(Task root) {
        this.root = root;
    }

    public abstract void run();
    public abstract JSONObject serialize() throws JSONException;
    public abstract void deserialize(JSONObject data) throws JSONException;
}
