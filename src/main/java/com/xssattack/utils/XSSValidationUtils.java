package com.xssattack.utils;


import lombok.experimental.UtilityClass;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.StringUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

@UtilityClass
public class XSSValidationUtils {

//    public final Pattern pattern = Pattern.compile("^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.\\/?\\s]*$", Pattern.CASE_INSENSITIVE);

    public static boolean isValidURL(String uri) {
        AtomicBoolean flag = new AtomicBoolean(false);
//        String[] urls = uri.split("\\/");
        String[] urls = new String[]{uri};
        Arrays.stream(urls).filter(StringUtils::hasLength)
                .forEach(url -> {
                    System.out.println("value:" + url);
                    boolean b = invalidTagFound(url);
                    flag.set(b);
                });
        return !flag.get();
    }

    public static boolean isValidURLPattern(String body) {
        AtomicBoolean flag = new AtomicBoolean(false);
//        String[] urls = body.split("\\/");
        String[] urls = new String[]{body};

        try {
            boolean match = Arrays.stream(urls)
                    .filter(StringUtils::hasLength)
                    .map(XSSValidationUtils::jsonToMap)
                    .anyMatch(XSSValidationUtils::invalidTagFound);
            flag.set(match);
        } catch (Exception ex) {
            ex.printStackTrace();
            flag.set(true);
        }
        return !flag.get();
    }

    public boolean invalidTagFound(String data) {
        try {
            return InvalidScriptTagsPatterns.getInvalidScriptTagsPatterns().stream()
                    .peek(p -> System.err.println("Pattern " + p.toString() + " checking for " + data))
                    .map(pattern -> pattern.matcher(data))
                    .anyMatch(Matcher::find);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Invalid char found!!!!!");
        }
        return false;
    }

    private static boolean invalidTagFound(Map<String, Object> map) {
        return map.values().stream()
                .peek(o -> System.out.println("Invalid tag check inside map " + o))
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .anyMatch(XSSValidationUtils::invalidTagFound);
    }

    private static Map<String, Object> jsonToMap(String url) {
        try {
            if (url.trim().startsWith("{")) {
                return jsonToMap(new JSONObject(url));
            } else {
                String[] split = url.split("&");
                return Arrays.stream(split)
                        .map(s -> s.split("="))
                        .filter(sLen -> sLen.length > 1)
                        .collect(Collectors.toMap(s -> s[0], XSSValidationUtils::getDecoded));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }

    private static String getDecoded(String[] s) {
        return URLDecoder.decode(s[1], StandardCharsets.UTF_8);
    }

    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<>();

        if (json != JSONObject.NULL) {
            toMap(json, retMap);
        }
        return retMap;
    }

    private static Map<String, Object> toMap(JSONObject object, Map<String, Object> map) throws JSONException {
        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            //   System.out.println("key  "+key+"  value:"+ value);

            if (value instanceof JSONArray) {
                value = toList(key, (JSONArray) value, map);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value, map);
            } else {
                map.put(key, value);
            }
        }
        return map;
    }

    private static List<Object> toList(String key, JSONArray array, Map<String, Object> map) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList(key, (JSONArray) value, map);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value, map);
            } else {
                String mapValue = String.valueOf(value);
                if (map.containsKey(key)) {
                    mapValue += "," + String.valueOf(map.get(key));
                }
                map.put(key, mapValue);
            }
            list.add(value);
        }
        return list;
    }
}
