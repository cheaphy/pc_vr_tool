package com.shushanfx;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shushanfx-home on 2015/8/8.
 */
public final class DevUtils {
    public static Map<String, Object> buildObject(Map<String, String[]> params){
        Map<String, Object> map = new HashMap<String, Object>();
        for(Map.Entry<String, String[]> entry : params.entrySet()){
            String key = entry.getKey();
            String[] value = entry.getValue();
            if(value!=null && value.length > 0){
                String oneItem = value[0];
                if(oneItem==null || oneItem.trim().equals("")){
                    continue ;
                }
                if("type".equals(key)
                        || "rank".equals(key)
                        || "position".equals(key)){
                    int temp = Integer.parseInt(oneItem.trim(), 10);
                    map.put(key, temp);
                }
                else{
                    map.put(key, oneItem);
                }
            }
        }

        return map;
    }

    public static String toHtml(String xsl, String xml) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        StringReader xslReader = new StringReader(xsl);
        StringWriter writer = new StringWriter();
        StringReader xmlReader = new StringReader(xml);

        Transformer transformer = transformerFactory.newTransformer
                    (new StreamSource(xslReader));
        transformer.transform(new StreamSource(xmlReader), new StreamResult(writer));

        return writer.toString();
    }

    public static Object escapeHtml(Object value){
        if(value == null){
            return null;
        }
        if(!(value instanceof String)){
            return value;
        }
        String str = value.toString();
        StringBuilder sb = new StringBuilder(str.length() + 30);
        for(int i = 0, len = str.length(); i < len; i++){
            char c = str.charAt(i);
            switch(c)
            {
                case '<':
                    sb.append("&#60;");
                    break;
                case '>':
                    sb.append("&#62;");
                    break;
                case '&':
                    sb.append("&#38;");
                    break;
                case '"':
                    sb.append("&#34;");
                    break;
                case '\'':
                    sb.append("&#39;");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        str = null;
        return sb.toString();
    }
}
