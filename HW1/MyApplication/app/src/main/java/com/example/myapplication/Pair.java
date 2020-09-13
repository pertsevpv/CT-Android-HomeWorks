package com.example.myapplication;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Objects;

public class Pair<F, S> {
    public F first;
    public S second;

    public Pair(F a, S b) {
        this.first = a;
        this.second = b;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) &&
                Objects.equals(second, pair.second);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
