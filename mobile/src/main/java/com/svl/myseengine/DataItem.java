package com.svl.myseengine;

public class DataItem {
    private final String text;
    private final String additionalText;
    private final int imageResId;

    public DataItem(String text, String additionalText, int imageResId) {
        this.text = text;
        this.additionalText = additionalText;
        this.imageResId = imageResId;
    }

    public String getText() {
        return text;
    }

    public String getAdditionalText() {
        return additionalText;
    }

    public int getImageResId() {
        return imageResId;
    }
}
