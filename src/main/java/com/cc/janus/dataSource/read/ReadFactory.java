package com.cc.janus.dataSource.read;

import java.util.Map;

public class ReadFactory {
    public static final Read getRead(Map<String,Object>options) {
        String type = String.class.cast(options.getOrDefault("type", "NULL"));
        switch (type) {
            case "file":
                return new ReadFile(options);
            default:
                return null;
        }
    }
}
