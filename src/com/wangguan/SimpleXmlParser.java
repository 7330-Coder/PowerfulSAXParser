package com.wangguan;

import java.io.File;
import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SimpleXmlParser {
	
	private SimpleXmlParser() {}
	
	public static void parse(File file, ISimpleXmlParserResultHandler resultHandler) throws Exception {
		
		String newLine = System.lineSeparator();
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		
		StringBuilder sb = new StringBuilder();
		
		DefaultHandler handler = new DefaultHandler() {

			Stack<String> stack = new Stack<>();

			@Override
			public void startElement(String uri, String localName, String qName, Attributes attributes)
					throws SAXException {
				stack.push(qName);
				sb.append(getIndent(stack.size() - 1));
				sb.append(qName).append(newLine);
				for (int i = 0; i < attributes.getLength(); i++) {
					sb.append(getIndent(stack.size() - 1));
					sb.append(attributes.getQName(i) + " = " + attributes.getValue(i)).append(newLine);
				}
				resultHandler.handle(sb.toString());
			}

			@Override
			public void endElement(String uri, String localName, String qName) throws SAXException {
				if (!stack.isEmpty() && qName.equals(stack.peek())) {
					stack.pop();
				}
			}

			@Override
			public void characters(char[] ch, int start, int length) throws SAXException {
				String str = new String(ch, start, length);
				if (!"".equals(str.trim())) {
					sb.append(getIndent(stack.size()));
					sb.append(" = " + str).append(newLine);
				}
				resultHandler.handle(sb.toString());
			}
			
		};

		parser.parse(file, handler);
	}
	
	private static String getIndent(int count) {
		StringBuilder sb = new StringBuilder(count * 4);
		for (int i = 0; i < count; i++) {
				sb.append("    ");
		}
		return sb.toString();
	}
}
