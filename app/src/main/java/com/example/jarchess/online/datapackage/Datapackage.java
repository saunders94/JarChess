package com.example.jarchess.online.datapackage;

public interface Datapackage<T extends Datapackage> extends JSONConvertible<T> {
    DatapackageType getDatapackageType();
}
