package com.pillar.bridge.dto;

public class DeviceDto {

    private String accessToken;
    private String refreshToken;

    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
