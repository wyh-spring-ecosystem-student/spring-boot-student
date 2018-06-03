package com.xiaolyuh;

        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.slf4j.MDC;

public class SimpleMDC {
    public static void main(String[] args) throws Exception {

        // You can put values in the MDC at any time. Before anything else
        // we put the first name
        MDC.put("first", "Dorothy");

        Logger logger = LoggerFactory.getLogger(SimpleMDC.class);
        // We now put the last name
        MDC.put("last", "Parker");

        // The most beautiful two words in the English language according
        // to Dorothy Parker:
        logger.info("Check enclosed.");
        logger.debug("The most beautiful two words in English.");

        MDC.put("first", "Richard");
        MDC.put("last", "Nixon");
        logger.info("I am not a crook.");
        logger.info("Attributed to the former US president. 17 Nov 1973.");
    }

}