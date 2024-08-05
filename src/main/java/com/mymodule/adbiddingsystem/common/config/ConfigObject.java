package com.mymodule.adbiddingsystem.common.config;

public class ConfigObject {
    public static final ConfigObject instance = new ConfigObject();

    public final int WINDOW_TO_SUBMIT_BIDS_IN_MILLIS = 1000;
    public final double LEVENSHTEIN_DISTANCE_THRESHOLD = 0.7;

    private ConfigObject(){ /*singleton*/}
}
