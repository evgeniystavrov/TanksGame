package com.evgs.game;

import com.evgs.IO.Input;

import java.awt.Graphics2D;

public abstract class Entity {
    public final EntityType     type;

    protected float             x;
    protected float             y;

    protected Entity(EntityType type, float x, float y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public abstract void update(Input input);

    public abstract void render(Graphics2D g);
}
