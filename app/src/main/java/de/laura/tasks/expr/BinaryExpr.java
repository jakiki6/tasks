package de.laura.tasks.expr;

import org.json.JSONException;
import org.json.JSONObject;

public class BinaryExpr extends Expr {
    public static final String key = "binary";
    Expr left, right;
    String op;

    @Override
    public Object eval() throws ExprException {
        Object l = left.eval();
        Object r = right.eval();

        try {
            switch (op) {
                case "add":
                    if (l instanceof Double) {
                        return (double) l + (double) r;
                    } else if (l instanceof String) {
                        return l + (String) r;
                    }
                    break;
                case "sub":
                    if (l instanceof Double) {
                        return (double) l - (double) r;
                    }
                    break;
                case "mul":
                    if (l instanceof Double) {
                        return (double) l * (double) r;
                    }
                    break;
                case "div":
                    if (l instanceof Double) {
                        return (double) l / (double) r;
                    }
                    break;
                case "mod":
                    if (l instanceof Double) {
                        return (double) l % (double) r;
                    }
                    break;
                case "get_char":
                    if (l instanceof String) {
                        return ((String) l).charAt((int) (double) r);
                    }
                    break;
                default:
                    throw new ExprException("Unknown operation '" + op + "'");
            }

            throw new Exception("No valid type");
        } catch (ExprException e) {
            throw e;
        } catch (Exception e) {
            throw new ExprException("Invalid types for '" + op + "': " + l.getClass().getSimpleName() + " and " + r.getClass().getSimpleName());
        }
    }

    @Override
    public JSONObject serialize() throws JSONException {
        JSONObject data = new JSONObject();
        data.put("type", key);
        data.put("left", left.serialize());
        data.put("right", right.serialize());
        data.put("op", op);
        return data;
    }

    @Override
    public void deserialize(JSONObject data) throws JSONException {
        left = Exprs.deserialize(data.getJSONObject("left"));
        right = Exprs.deserialize(data.getJSONObject("right"));
        op = data.getString("op");
    }
}
