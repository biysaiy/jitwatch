/*
 * Copyright (c) 2013-2016 Chris Newland.
 * Licensed under https://github.com/AdoptOpenJDK/jitwatch/blob/master/LICENSE-BSD
 * Instructions: https://github.com/AdoptOpenJDK/jitwatch/wiki
 */
package org.adoptopenjdk.jitwatch.model;

import static org.adoptopenjdk.jitwatch.core.JITWatchConstants.C_SPACE;
import static org.adoptopenjdk.jitwatch.core.JITWatchConstants.DEBUG_LOGGING_PARSE_DICTIONARY;
import static org.adoptopenjdk.jitwatch.core.JITWatchConstants.S_COMMA;
import static org.adoptopenjdk.jitwatch.core.JITWatchConstants.S_DOT;
import static org.adoptopenjdk.jitwatch.core.JITWatchConstants.S_OPEN_PARENTHESES;
import static org.adoptopenjdk.jitwatch.core.JITWatchConstants.S_SLASH;
import static org.adoptopenjdk.jitwatch.core.JITWatchConstants.S_SPACE;

import java.util.Map;

import org.adoptopenjdk.jitwatch.core.JITWatchConstants;
import org.adoptopenjdk.jitwatch.util.ParseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Task extends Tag
{
	private static final Logger logger = LoggerFactory.getLogger(Task.class);

	private IParseDictionary parseDictionary;

	public Task(String name, String attributeString, boolean selfClosing)
	{
		super(name, attributeString, selfClosing);

		parseDictionary = new ParseDictionary();
	}

	public IParseDictionary getParseDictionary()
	{
		return parseDictionary;
	}

	public void addDictionaryType(String type, Tag tag)
	{
		if (DEBUG_LOGGING_PARSE_DICTIONARY)
		{
			logger.debug("Adding type: {}", type);
		}

		parseDictionary.putType(type, tag);
	}

	public void addDictionaryMethod(String method, Tag tag)
	{
		if (DEBUG_LOGGING_PARSE_DICTIONARY)
		{
			logger.debug("Adding method: {}", method);
		}

		parseDictionary.putMethod(method, tag);
	}

	public void addDictionaryKlass(String klass, Tag tag)
	{
		if (DEBUG_LOGGING_PARSE_DICTIONARY)
		{
			logger.debug("Adding klass: {}", klass);
		}

		parseDictionary.putKlass(klass, tag);
	}

	public String decodeParseMethod(String method)
	{
		StringBuilder builder = new StringBuilder();

		Tag methodTag = parseDictionary.getMethod(method);
		
		Map<String, String> methodTagAttrs = methodTag.getAttributes();

		String returnTypeID = methodTagAttrs.get(JITWatchConstants.ATTR_RETURN);

		String args = methodTagAttrs.get(JITWatchConstants.ATTR_ARGUMENTS);

		String methodName = methodTagAttrs.get(JITWatchConstants.ATTR_NAME);

		String klassId = methodTagAttrs.get(JITWatchConstants.ATTR_HOLDER);

		Tag klassTag = parseDictionary.getKlass(klassId);

		Map<String, String> klassTagAttrs = klassTag.getAttributes();
		
		String klassName = klassTagAttrs.get(JITWatchConstants.ATTR_NAME);
		klassName = klassName.replace(S_SLASH, S_DOT);

		builder.append(" <!-- ");
		builder.append(getTypeOrKlass(returnTypeID));
		builder.append(C_SPACE);
		builder.append(klassName);
		builder.append(S_DOT);
		builder.append(methodName);
		builder.append(S_OPEN_PARENTHESES);

		if (args != null && args.length() > 0)
		{
			String[] ids = args.split(S_SPACE);

			for (String id : ids)
			{
				builder.append(getTypeOrKlass(id));
				builder.append(S_COMMA);
			}

			builder.deleteCharAt(builder.length() - 1);
		}

		builder.append(") -->");

		return builder.toString();
	}

	private String getTypeOrKlass(String id)
	{
		Tag typeTag = parseDictionary.getType(id);

		String result = null;

		if (typeTag == null)
		{
			Tag klassTag = parseDictionary.getKlass(id);

			if (klassTag != null)
			{
				result = klassTag.getAttributes().get(JITWatchConstants.ATTR_NAME);
				result = result.replace(S_SLASH, S_DOT);
			}
		}
		else
		{
			result = typeTag.getAttributes().get(JITWatchConstants.ATTR_NAME);
		}

		if (result == null)
		{
			result = "???"; // further understanding required!
		}
		else
		{
			result = ParseUtil.expandParameterType(result);
		}

		return result;
	}
}
