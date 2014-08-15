package uk.co.thinkofdeath.prismarine.network.login;

import java.util.Arrays;

public class LoginResponse {

    private String id;
    private String name;
    private Property[] properties;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Property[] getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", properties=" + Arrays.toString(properties) +
                '}';
    }
}
