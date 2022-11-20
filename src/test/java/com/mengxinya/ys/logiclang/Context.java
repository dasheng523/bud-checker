package com.mengxinya.ys.logiclang;

import java.util.HashMap;
import java.util.Map;

public interface Context {
    Object getValue(String key);
    void setValue(String key, Object val);

    static Context emptyContext() {
        Map<String, Object> map = new HashMap<>();
        return new SimpleContext(map);
    }

    static Context copy(Context context) {
        if (context instanceof SimpleContext simpleContext) {
            return new SimpleContext(simpleContext.getMap());
        }
        return null;
    }

    class SimpleContext implements Context {

        private final Map<String, Object> map;
        SimpleContext(Map<String, Object> map) {
            this.map = map;
        }

        @Override
        public Object getValue(String key) {
            Object val = map.get(key);
            if (val == null) {
                throw  new RuntimeException("The key is not exits: " + key);
            }
            return val;
        }

        @Override
        public void setValue(String key, Object val) {
            map.put(key, val);
        }

        public Map<String, Object> getMap() {
            return map;
        }
    }
}
