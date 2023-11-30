package de.laura.tasks.expr;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Expr {
    public abstract Object eval() throws ExprException;
    public abstract JSONObject serialize() throws JSONException;
    public abstract void deserialize(JSONObject data) throws JSONException;
}
