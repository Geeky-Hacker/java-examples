package com.geekyhacker.valueclass;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public final class ValueClassReadiness {

    public record Finding(String member, String problem) {
    }

    private ValueClassReadiness() {
    }

    public static List<Finding> check(Class<?> type) {
        List<Finding> findings = new ArrayList<>();
        for (Field field : type.getDeclaredFields()) {
            int modifiers = field.getModifiers();
            if (!field.isSynthetic() && !Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers)) {
                findings.add(new Finding(field.getName(), "instance field is not final"));
            }
        }
        for (Method method : type.getDeclaredMethods()) {
            int modifiers = method.getModifiers();
            if (!Modifier.isStatic(modifiers) && Modifier.isSynchronized(modifiers)) {
                findings.add(new Finding(method.getName(), "synchronized instance method"));
            }
        }
        return findings;
    }
}
