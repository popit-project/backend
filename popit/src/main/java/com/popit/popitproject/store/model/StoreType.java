package com.popit.popitproject.store.model;

public enum StoreType{
    POPUP_STORE("팝업스토어"),
    FLEA_MARKET("플리마켓"),
    CHOOSE_TYPE("선택안함");

    private final String displayName;

    StoreType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}