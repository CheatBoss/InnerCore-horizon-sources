package com.google.firebase.components;

public class MissingDependencyException extends DependencyException
{
    public MissingDependencyException(final String s) {
        super(s);
    }
}
