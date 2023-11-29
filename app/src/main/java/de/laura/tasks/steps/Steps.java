package de.laura.tasks.steps;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import de.laura.tasks.tasks.Task;

public class Steps {
    public static final HashMap<String, Class<? extends Step>> types = new HashMap<>();

    static {
        types.put(StepSequence.key, StepSequence.class);
    }

    public static Step deserialize(JSONObject data, Task root) throws JSONException {
        String type = data.getString("type");
        if (!types.containsKey(type) || types.get(type) == null) {
            throw new IllegalArgumentException("unknown type '" + type + "'");
        }

        Step step = null;
        try {
            step = types.get(type).getDeclaredConstructor(Task.class).newInstance(root);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException |
                 InstantiationException e) {
            throw new RuntimeException(e);
        }
        step.deserialize(data);
        return step;
    }
}
