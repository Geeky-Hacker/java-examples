package com.geekyhacker.zgc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record KernelVersion(int major, int minor) {

    private static final Pattern VERSION = Pattern.compile("^(\\d+)\\.(\\d+)");

    public static KernelVersion parse(String osVersion) {
        Matcher matcher = VERSION.matcher(osVersion.trim());
        if (!matcher.find()) {
            throw new IllegalArgumentException("Unrecognised kernel version: " + osVersion);
        }
        return new KernelVersion(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
    }

    /** MADV_COLLAPSE, used to upgrade small pages to large ones in the background, arrived in Linux 6.1. */
    public boolean supportsMadvCollapse() {
        return major > 6 || (major == 6 && minor >= 1);
    }
}
