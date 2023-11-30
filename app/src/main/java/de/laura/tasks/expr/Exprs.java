package de.laura.tasks.expr;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import de.laura.tasks.steps.Step;
import de.laura.tasks.steps.StepSequence;
import de.laura.tasks.tasks.Task;

public class Exprs {
    public static final HashMap<String, Class<? extends Expr>> types = new HashMap<>();

    static {
        types.put(LitExpr.key, LitExpr.class);
        types.put(UnaryExpr.key, UnaryExpr.class);
        types.put(BinaryExpr.key, BinaryExpr.class);
    }

    public static Expr deserialize(JSONObject data) throws JSONException {
        String type = data.getString("type");
        if (!types.containsKey(type) || types.get(type) == null) {
            throw new IllegalArgumentException("unknown type '" + type + "'");
        }

        Expr expr = null;
        try {
            expr = types.get(type).getDeclaredConstructor().newInstance();
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException |
                 InstantiationException e) {
            throw new RuntimeException(e);
        }
        expr.deserialize(data);
        return expr;
    }
}
