package com.kt.ai.commerce.common.utils;

import java.util.Enumeration;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CommProperty {
    public static final String PROPERTY_FILE = "/common.properties";
    private static final Logger logger = LoggerFactory.getLogger(CommProperty.class);

    private static Properties propertyMap = new Properties();

    static {
        setProperty();
    }

    private CommProperty() {
    }

    /**
     * common.properties의 내용을 읽어 propertyMap에 저장
     */
    public static void setProperty() {
        try {
            // Read properties file.
            propertyMap.load(com.kt.ai.commerce.common.utils.CommProperty.class.getResourceAsStream(PROPERTY_FILE));

            if (logger.isInfoEnabled()) {
                logger.info("=========== Load Comm Property =======");
                @SuppressWarnings("rawtypes")
                Enumeration e = propertyMap.keys();
                while (e.hasMoreElements()) {
                    String key = (String) e.nextElement();
                    logger.info("== " + key + " : " + propertyMap.getProperty(key));
                }
                logger.info("=========== Property Load End =======");
            }

        }
        catch (NullPointerException pe) {
            pe.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * propertyMap에서 해당 key의 값을 찾아서 반환한다.
     * 
     * @param key
     * @return
     */
    public static String getProperty(String key) {
        return propertyMap.getProperty(key);
    }

    public static String getProperty(String key, String rp) {
        String str = propertyMap.getProperty(key);
        if (str != null && str.length() > 0)
            return str;
        else
            return rp;
    }

    /**
     * propertyMap에서 해당 key의 값을 찾아 int로 변환하여 반환한다.
     * 
     * @param key
     * @return
     */
    public static int getPropertyInt(String key) {
        int propertyValue = 0;
        try {
            propertyValue = StringUtil.getNumber(propertyMap.getProperty(key));
        }
        catch (NullPointerException pe) {
            pe.printStackTrace();
            propertyValue = 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            propertyValue = 0;
        }
        return propertyValue;
    }
}
