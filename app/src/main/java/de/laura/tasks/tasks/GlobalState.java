package de.laura.tasks.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

import de.laura.tasks.service.TasksService;

public class GlobalState {
    HashMap<String, Object> variables = new HashMap<>();
    TasksService service;

    public GlobalState(TasksService service) {
        this.service = service;
    }

    public Object getVariable(String name, Object defaultValue) {
        if (variables.containsKey(name)) {
            return variables.get(name);
        } else {
            variables.put(name, defaultValue);
            save();
            return defaultValue;
        }
    }

    public void setVariable(String name, Object value) {
        variables.put(name, value);
        save();
    }

    private void save() {
        SharedPreferences prefs = service.getSharedPreferences("tasks", Context.MODE_PRIVATE);
        prefs.edit().putString("global_state", serialize().toString()).apply();
    }

    public JSONObject serialize() {
        try {
            JSONObject obj = new JSONObject();

            JSONObject vars = new JSONObject();
            for (String name : variables.keySet()) {
                vars.put(name, variables.get(name));
            }
            obj.put("variables", vars);

            return obj;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static GlobalState deserialize(JSONObject data, TasksService service) throws JSONException {
        GlobalState state = new GlobalState(service);

        JSONObject vars = data.getJSONObject("vars");
        for (Iterator<String> it = vars.keys(); it.hasNext(); ) {
            String name = it.next();
            state.setVariable(name, vars.get(name));
        }

        return state;
    }
}
