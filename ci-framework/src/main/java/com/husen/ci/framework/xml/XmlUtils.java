package com.husen.ci.framework.xml;

import com.husen.ci.framework.net.bean.HttpResult;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/***
 @Author:MrHuang
 @Date: 2019/6/13 18:48
 @DESC: TODO
 @VERSION: 1.0
 ***/
public class XmlUtils {

    /**
     * 转换不带CDDATA的XML
     *
     * @return
     * @
     */
    private static XStream getXStream() {
        // 实例化XStream基本对象
        XStream xstream = new XStream(new DomDriver(StandardCharsets.UTF_8.name(), new NoNameCoder() {
            // 不对特殊字符进行转换，避免出现重命名字段时的“双下划线”
            @Override
            public String encodeNode(String name) {
                return name;
            }
        }));
        // 忽视XML与JAVABEAN转换时，XML中的字段在JAVABEAN中不存在的部分
        xstream.ignoreUnknownElements();
        return xstream;
    }

    /**
     * 转换带CDDATA的XML
     *
     * @return
     * @
     */
    private static XStream getXStreamWithCData() {
        // 实例化XStream扩展对象
        XStream xstream = new XStream(new XppDriver() {
            // 扩展xstream，使其支持CDATA块
            @Override
            public HierarchicalStreamWriter createWriter(Writer out) {
                return new PrettyPrintWriter(out) {
                    // 不对特殊字符进行转换，避免出现重命名字段时的“双下划线”
                    @Override
                    public String encodeNode(String name) {
                        return name;
                    }

                    // 对所有xml节点的转换都增加CDATA标记
                    @Override
                    protected void writeText(QuickWriter writer, String text) {
                        writer.write("<![CDATA[");
                        writer.write(text);
                        writer.write("]]>");
                    }
                };
            }
        });
        // 忽视XML与JAVABEAN转换时，XML中的字段在JAVABEAN中不存在的部分
        xstream.ignoreUnknownElements();
        return xstream;
    }

    /**
     * 以压缩的方式输出XML
     *
     * @param obj
     * @return
     */
    public static String toCompressXml(Object obj) {
        XStream xstream = getXStream();
        StringWriter sw = new StringWriter();
        // 识别obj类中的注解
        xstream.processAnnotations(obj.getClass());
        // 设置JavaBean的类别名
        xstream.aliasType("xml", obj.getClass());
        xstream.marshal(obj, new CompactWriter(sw));
        return sw.toString();
    }

    /**
     * 以格式化的方式输出XML
     *
     * @param obj
     * @return
     */
    public static String toXml(Object obj) {
        XStream xstream = getXStream();
        // 识别obj类中的注解
        xstream.processAnnotations(obj.getClass());
        // 设置JavaBean的类别名
        xstream.aliasType("xml", obj.getClass());
        return xstream.toXML(obj);
    }

    /**
     * 转换成JavaBean
     *
     * @param xmlStr
     * @param cls
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T toBean(String xmlStr, Class<T> cls) {
        XStream xstream = getXStream();
        // 识别cls类中的注解
        xstream.processAnnotations(cls);
        // 设置JavaBean的类别名
        xstream.aliasType("xml", cls);
        T t = (T) xstream.fromXML(xmlStr);
        return t;
    }

    /**
     * 以格式化的方式输出XML
     *
     * @param obj
     * @return
     */
    public static String toXmlWithCData(Object obj) {
        XStream xstream = getXStreamWithCData();
        // 识别obj类中的注解
        xstream.processAnnotations(obj.getClass());
        // 设置JavaBean的类别名
        xstream.aliasType("xml", obj.getClass());
        return xstream.toXML(obj);
    }

    /**
     * 转换成JavaBean
     *
     * @param xmlStr
     * @param cls
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T toBeanWithCData(String xmlStr, Class<T> cls) {
        XStream xstream = getXStreamWithCData();
        // 识别cls类中的注解
        xstream.processAnnotations(cls);
        // 设置JavaBean的类别名
        xstream.alias("xml", cls);
        T t = (T) xstream.fromXML(xmlStr);
        return t;
    }


    //通用的。返回map格式

    /**
     * XML格式字符串转换为Map
     *
     * @param strXML XML字符串
     * @return XML数据转换后的Map
     * @throws Exception
     */
    public static Map<String, String> xmlToMap(String strXML) throws Exception {
        try (InputStream stream = new ByteArrayInputStream(strXML.getBytes(StandardCharsets.UTF_8.name()))) {
            Map<String, String> data = new HashMap<>(16);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            org.w3c.dom.Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
            return data;
        }
    }


    /**
     * 将Map转换为XML格式的字符串
     *
     * @param data Map类型数据
     * @return XML格式的字符串
     * @throws Exception
     */
    public static String mapToXml(Map<String, String> data) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        org.w3c.dom.Document document = documentBuilder.newDocument();
        org.w3c.dom.Element root = document.createElement("xml");
        document.appendChild(root);
        for (String key : data.keySet()) {
            String value = data.get(key);
            if (value == null) {
                value = "";
            }
            value = value.trim();
            org.w3c.dom.Element filed = document.createElement(key);
            filed.appendChild(document.createTextNode(value));
            root.appendChild(filed);
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String output = writer.getBuffer().toString();
        try {
            writer.close();
        } catch (Exception ex) {

        }
        return output;
    }


    public static void main(String[] args) throws Exception {
        HttpResult result = new HttpResult().setCode(200).setContent("OK");
        String x1 = XmlUtils.toXml(result);
        System.out.println(x1);
        String x2 = XmlUtils.toXmlWithCData(result);
        System.out.println(x2);
        Map<String, String> m1 = XmlUtils.xmlToMap(x1);
        System.out.println(m1);
        Map<String, String> m2 = XmlUtils.xmlToMap(x2);
        System.out.println(m2);

        HttpResult hr1 = XmlUtils.toBean(x1, HttpResult.class);
        System.out.println(hr1);
    }
}
