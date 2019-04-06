package com.evgs.graphics;

import com.evgs.utils.ImageLoader;

import java.awt.image.BufferedImage;

public class TextureAtlas {
    BufferedImage image;

    public TextureAtlas(String imageName) {
        image = ImageLoader.loadImage(imageName);
    }

    public BufferedImage cut(int x, int y, int w, int h) { // вырезка нужного изображения из большого
        return image.getSubimage(x, y, w, h);
    }
}
