package com.popit.popitproject.store.model;

public enum StoreType {
    POPUP_STORE("Popup store"),
    FLEA_MARKET("Flea market");

    private final String displayName;

    StoreType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}