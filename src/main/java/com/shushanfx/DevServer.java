package com.shushanfx;

import com.mongodb.DBObject;
import spark.*;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

/**
 * Created by shushanfx on 2015/8/8.
 */
public class DevServer {
    public static void main(String[] args) {
        DevDB db = new DevDB();
        staticFileLocation("/public");
        TemplateEngine engine = new VelocityTemplateEngine();
        TemplateViewRoute listRoute = new TemplateViewRoute() {
            public ModelAndView handle(Request request, Response response) {
                Map<String, Object> model = new HashMap<String, Object>();
                Map<String, Object> query = DevUtils.buildObject(request.queryMap().toMap());
                model.put("query", query);
                List<Map<String,Object>> list = db.find(query);
                model.put("list", list);
                return new ModelAndView(model, "list.vm");
            }
        };
        get("/", listRoute, engine);
        get("index", listRoute, engine);
        get("list", listRoute, engine);
        get("add", (req, res) ->  new ModelAndView(new HashMap<String, Object>(), "edit.vm") , engine);
        get("edit/:tplID", (req, res) -> {
            DBObject obj = db.get(req.params(":tplID"));
            Map<String, Object> model = new HashMap<String, Object>();
            if(obj!=null){
                obj.put("xml", DevUtils.escapeHtml(obj.get("xml")));
                obj.put("html", DevUtils.escapeHtml(obj.get("html")));
                obj.put("xsl", DevUtils.escapeHtml(obj.get("xsl")));
            }
            model.put("item", obj);
            return new ModelAndView(model, "edit.vm");
        }, engine);
        post("save", (req, res) -> {
            res.type("text/html");
            QueryParamsMap queryMap = req.queryMap();
            Map<String, Object> obj = DevUtils.buildObject(queryMap.toMap());
            if (obj != null) {
                boolean b = false;
                try{
                    b = db.save(obj);
                }
                catch (Exception e){
                    return "{\"status\": \"fail\", \"msg\": \"" + e.getMessage() +"\"}";
                }
                return "{\"status\": \"" + (b ? "ok" : "fail") + "\"}";
            } else {
                return "{\"status\": \"fail\", message:\"tplID is required.\"}";
            }
        });
        get("delete/:tplID", (req, res) -> {
            res.type("text/html");
            String tplID = req.params(":tplID");
            if (tplID != null && !"".equals(tplID)) {
                Map<String, Object> obj = new HashMap<String, Object>();
                obj.put("tplID", tplID);
                int n = db.delete(obj);
                return "{\"status\": \"ok\", \"delete\":" + n +"}";
            } else {
                return "{\"status\": \"fail\", message:\"tplID is required.\"}";
            }
        });
    }
}
