package de.laura.tasks.expr;

import org.json.JSONException;
import org.json.JSONObject;

public class UnaryExpr extends Expr {
    public static final String key = "unary";
    Expr child;
    String op;

    @Override
    public Object eval() throws ExprException {
        try {
            Object c = child.eval();

            switch (op) {
                case "neg":
                    if (c instanceof Double) {
                        return -(double) c;
                    }
                    break;
                case "not":
                    if (c instanceof Double) {
                        return -(double) c;
                    } else if (c instanceof String) {
                        return ((String) c).length() > 0 ? 1.0 : 0.0;
                    }
                    break;
                default:
                    throw new ExprException("Unknown operation '" + op + "'");
            }

            throw new ExprException("No valid type");
        } catch (ExprException e) {
            throw e;
        } catch (Exception e) {
            throw new ExprException("Invalid type for '" + op + "': " + child.getClass().getSimpleName());
        }
    }

    @Override
    public JSONObject serialize() throws JSONException {
        JSONObject data = new JSONObject();
        data.put("type", key);
        data.put("child", child.serialize());
        data.put("op", op);
        return null;
    }

    @Override
    public void deserialize(JSONObject data) throws JSONException {
        child = Exprs.deserialize(data.getJSONObject("child"));
    }
}
