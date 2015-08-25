package com.shushanfx;

import com.mongodb.*;

import javax.xml.transform.TransformerException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by shushanfx-home on 2015/8/8.
 */
public class DevDB {
    private MongoClient  mongoClient = null;

    public DevDB(){
        MongoClientOptions.Builder build = new MongoClientOptions.Builder();
        build.connectionsPerHost(50);   //与目标数据库能够建立的最大connection数量为50
        build.threadsAllowedToBlockForConnectionMultiplier(50); //如果当前所有的connection都在使用中，则每个connection上可以有50个线程排队等待
            /*
             * 一个线程访问数据库的时候，在成功获取到一个可用数据库连接之前的最长等待时间为2分钟
             * 这里比较危险，如果超过maxWaitTime都没有获取到这个连接的话，该线程就会抛出Exception
             * 故这里设置的maxWaitTime应该足够大，以免由于排队线程过多造成的数据库访问失败
             */
        build.maxWaitTime(1000*60*2);
        build.connectTimeout(1000*60*1);    //与数据库建立连接的timeout设置为1分钟

        MongoClientOptions myOptions = build.build();
        try {
            //数据库连接实例
            mongoClient = new MongoClient("10.134.81.191", myOptions);
        } catch (MongoException e){
            e.printStackTrace();
        }
    }
    private DBCollection getCollection(){
        return mongoClient.getDB("vr").getCollection("dev");
    }

    public boolean insert(Map<String, Object> map){
        DBCollection collection = getCollection();
        BasicDBObject insertObj = new BasicDBObject();
        for(Map.Entry<String, Object> entry : map.entrySet()){
            insertObj.put(entry.getKey(), entry.getValue());
        }
        WriteResult result = collection.insert(insertObj);

        return false;
    }

    public DBObject get(String tplID){
        DBCollection collection = getCollection();
        DBObject query = new BasicDBObject();
        query.put("tplID", tplID);
        return collection.findOne(query);
    }

    public boolean save(Map<String, Object> map) throws TransformerException {
        DBCollection collection = getCollection();
        Object tplID = map.get("tplID");
        DBObject query = new BasicDBObject();
        query.put("tplID", tplID);
        DBObject item = collection.findOne(query);
        boolean isFind = false;
        if(item!=null){
            isFind = true;
            for(Map.Entry<String, Object> entry : map.entrySet()){
                item.put(entry.getKey(), entry.getValue());
            }
        }
        else{
            isFind = false;
            item = new BasicDBObject();
            for(Map.Entry<String, Object> entry : map.entrySet()){
                item.put(entry.getKey(), entry.getValue());
            }
        }
        if(map.get("xml")!=null
                || map.get("xsl")!=null){
            Object xmlObj = item.get("xml");
            Object xslObj = item.get("xsl");
            if(xmlObj!=null && xslObj!=null){
                String xml = xmlObj.toString();
                Object classID = item.get("classID");
                xml = xml.replace("</display>", "</display><classid>" + classID + "</classid><tplid>" + tplID + "</tplid>");
                String html = DevUtils.toHtml(xslObj.toString(), xml);
                System.out.println("=========Start Save " + tplID + "============================");
                System.out.println("---------XSL------------------------------------------------\n" + xslObj);
                System.out.println("---------XML------------------------------------------------\n" + xml);
                System.out.println("---------HTML-----------------------------------------------\n" + html);
                System.out.println("=========End Save===========================================");
                item.put("html", html);
                if(isFind){
                    collection.update(query, item);
                }
                else{
                    collection.save(item);
                }
            }
            else {
                throw new NullPointerException("xml and xsl cannot be null.");
            }
        }
        return true;
    }

    public List<Map<String, Object>> find(Map<String, Object> map){
        DBCollection collection = getCollection();
        DBObject query = new BasicDBObject();
        for(Map.Entry<String, Object> entry : map.entrySet()){
            String key = entry.getKey();
            if("name".equals(key)
                    || "keyword".equals(key)){
                Pattern pattern = Pattern.compile("^.*" + entry.getValue() + ".*$", Pattern.CASE_INSENSITIVE);
                query.put(key, pattern);
            }
            else{
                query.put(key, entry.getValue());
            }
        }
        DBCursor cursor = collection.find(query);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        while (cursor.hasNext()){
            DBObject obj = cursor.next();
            list.add(obj.toMap());
        }
        return list;
    }

    public int delete(Map<String, Object> map){
        DBCollection collection = getCollection();
        DBObject query = new BasicDBObject();
        for(Map.Entry<String, Object> entry : map.entrySet()){
            query.put(entry.getKey(), entry.getValue());
        }
        WriteResult result = collection.remove(query);
        return result.getN();
    }
}
