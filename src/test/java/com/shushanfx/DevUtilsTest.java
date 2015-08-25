package com.shushanfx;

import junit.framework.TestCase;

import javax.xml.transform.TransformerException;

/**
 * Created by shushanfx-home on 2015/8/9.
 */
public class DevUtilsTest extends TestCase{
    public void testHtml() throws TransformerException {
        String xml = "<?xml version=\"1.0\"?>\n" +
                "<!--\n" +
                "<?xml-stylesheet type=\"text/xsl\" href=\"people.xsl\"?>\n" +
                "-->\n" +
                "<people>\n" +
                "  <person born=\"1900\" died=\"1099\">\n" +
                "    <name>\n" +
                "      <first_name>Wu</first_name>\n" +
                "      <last_name>feng</last_name>\n" +
                "    </name>\n" +
                "    <profession>computer scientist</profession>\n" +
                "    <profession>mathematician</profession>\n" +
                "    <profession>cryptographer</profession>\n" +
                "  </person>\n" +
                "  <person born=\"1918\" died=\"288\">\n" +
                "    <name>\n" +
                "      <first_name>王</first_name>\n" +
                "      <middle_initial>二</middle_initial>\n" +
                "      <last_name>小</last_name>\n" +
                "    </name>\n" +
                "    <profession>生态学</profession>\n" +
                "    <hobby>放羊</hobby>\n" +
                "  </person>\n" +
                " \n" +
                "<person>\n" +
                " <hobby>摄影</hobby>\n" +
                " <hobby>旅游</hobby>\n" +
                "</person>\n" +
                "</people>";
        String xsl = "<?xml version=\"1.0\"?>\n" +
                "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n" +
                "<xsl:output method=\"xml\"  omit-xml-declaration=\"yes\" indent=\"yes\"/>\n" +
                "<xsl:template match=\"/\">\n" +
                "<xsl:for-each select=\"//person\">\n" +
                " <xsl:value-of select=\"name()\"/>　\n" +
                " <xsl:value-of select=\"@born\"/>\n" +
                " <br />\n" +
                "</xsl:for-each>\n" +
                "<xsl:for-each select=\"people/person/hobby\">\n" +
                " <xsl:value-of select=\"name()\"/>　\n" +
                " <xsl:value-of select=\".\"/>\n" +
                " <br />\n" +
                "</xsl:for-each>\n" +
                "</xsl:template>\n" +
                "</xsl:stylesheet>";
        System.out.println(DevUtils.toHtml(xsl, xml));
    }
}
