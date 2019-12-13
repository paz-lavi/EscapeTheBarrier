package com.example.hw2;

import android.animation.Animator;
import android.view.animation.Animation;
import android.widget.ImageView;

public class SaveIndexForLater {
    private int col;
    private float y;
    private ImageView iv;
    Animation anim;

    public SaveIndexForLater(int col, float y, ImageView iv, Animation anim) {
        this.col = col;
        this.y = y;
        this.iv = iv;
        this.anim = anim;
    }

    public Animation getAnim() {
        return anim;
    }

    public float getY() {
        return y;
    }

    public int getCol() {
        return col;
    }

    public ImageView getIv() {
        return iv;
    }
}
