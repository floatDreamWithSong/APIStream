package com.daydreamer.faastest.context;

import org.graalvm.polyglot.Context;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class JavascriptContext {
    public static ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    public static Context context =Context.newBuilder("js")
            .out(new PrintStream(JavascriptContext.outputStream))
            .allowAllAccess(true)
            .build();
}
