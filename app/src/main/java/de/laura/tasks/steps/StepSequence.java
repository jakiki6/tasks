package de.laura.tasks.steps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.laura.tasks.tasks.Task;

public class StepSequence extends Step {
    ArrayList<Step> steps;
    public static final String key = "sequence";

    public StepSequence(Task root) {
        super(root);
    }

    @Override
    public void run() {
        for (Step step : this.steps) {
            step.run();
        }
    }

    @Override
    public JSONObject serialize() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("type", "");

        JSONArray steps = new JSONArray();
        for (Step step : this.steps) {
            steps.put(step.serialize());
        }

        return object;
    }

    @Override
    public void deserialize(JSONObject data) throws JSONException {
        this.steps = new ArrayList<>();
        JSONArray steps = data.getJSONArray("steps");
        for (int i = 0; i < steps.length(); i++) {
            this.steps.add(Steps.deserialize(steps.optJSONObject(i), this.root));
        }
    }
}
