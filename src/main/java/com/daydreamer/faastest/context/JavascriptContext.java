package com.daydreamer.faastest.context;

import org.graalvm.polyglot.Context;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class JavascriptContext {
    public ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    public Context context =Context.newBuilder("js")
            .out(new PrintStream(outputStream))
            .allowAllAccess(true)
            .build();
}
