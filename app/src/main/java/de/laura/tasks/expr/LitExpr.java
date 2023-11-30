package de.laura.tasks.expr;

import org.json.JSONException;
import org.json.JSONObject;

public class LitExpr extends Expr {
    public static final String key = "literal";
    Object data;

    @Override
    public Object eval() throws ExprException {
        return data;
    }

    @Override
    public JSONObject serialize() throws JSONException {
        JSONObject data = new JSONObject();
        data.put("type", key);
        data.put("value", this.data);
        return data;
    }

    @Override
    public void deserialize(JSONObject data) throws JSONException {
        this.data = data.get("value");
    }
}
