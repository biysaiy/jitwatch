/*
 * Copyright (c) 2013-2016 Chris Newland.
 * Licensed under https://github.com/AdoptOpenJDK/jitwatch/blob/master/LICENSE-BSD
 * Instructions: https://github.com/AdoptOpenJDK/jitwatch/wiki
 */
package org.adoptopenjdk.jitwatch.suggestion;

import org.adoptopenjdk.jitwatch.model.IMetaMember;

public class Suggestion
{
	public enum SuggestionType
	{
		BRANCH, INLINING, CODE_CACHE, HOT_THROW
	}

	private final IMetaMember caller;
	private final int compilationIndex;
	private final int bytecodeOffset;
	private final String suggestion;
	private final SuggestionType type;
	private final int score;

	public Suggestion(IMetaMember caller, int compilationIndex, int bytecodeOffset, String suggestion, SuggestionType type, int score)
	{
		this.caller = caller;
		this.compilationIndex = compilationIndex;
		this.bytecodeOffset = bytecodeOffset;
		this.suggestion = suggestion;
		this.score = score;
		this.type = type;
	}

	public IMetaMember getCaller()
	{
		return caller;
	}
	
	public int getCompilationIndex()
	{
		return compilationIndex;
	}

	public int getBytecodeOffset()
	{
		return bytecodeOffset;
	}

	public String getText()
	{
		return suggestion;
	}

	public SuggestionType getType()
	{
		return type;
	}

	public int getScore()
	{
		return score;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + bytecodeOffset;
		result = prime * result + ((caller == null) ? 0 : caller.hashCode());
		result = prime * result + compilationIndex;
		result = prime * result + score;
		result = prime * result + ((suggestion == null) ? 0 : suggestion.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Suggestion other = (Suggestion) obj;
		if (bytecodeOffset != other.bytecodeOffset)
			return false;
		if (caller == null)
		{
			if (other.caller != null)
				return false;
		}
		else if (!caller.equals(other.caller))
			return false;
		if (compilationIndex != other.compilationIndex)
			return false;
		if (score != other.score)
			return false;
		if (suggestion == null)
		{
			if (other.suggestion != null)
				return false;
		}
		else if (!suggestion.equals(other.suggestion))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
}