package dev.lightdream.chunkgangs.database;

public interface Callback<V> {
    void onSuccess(V var1);

    void onFailure(V var1);
}
