package com.evgs.game;

import com.evgs.IO.Input;
import com.evgs.display.Display;
import com.evgs.game.level.Level;
import com.evgs.graphics.TextureAtlas;
import com.evgs.utils.Time;

import java.awt.*;

public class Game implements Runnable {
    public static final int     WIDTH = 800;
    public static final int     HEIGHT = 600;
    public static final int     CLEAR_COLOR = 0xff000000;
    public static final int     NUM_BUFFERS = 3;
    public static final String  TITLE = "Tanks";

    public static final float   UPDATE_RATE = 60.0f;
    public static final float   UPDATE_INTERVAL = Time.SECOND / UPDATE_RATE;
    public static final long    IDLE_TIME = 1; // остановка нового процесса на данное время

    public static final String  ATLAS_FILENAME = "texture_atlas.png";

    private boolean             running; // проверка статуса процесса
    private Thread              gameThread; // поток для игры
    private Graphics2D          graphics;
    private Input               input;
    private TextureAtlas        atlas;
    private Player              player;
    private Level               level;

    public Game() {
        running = false;

        Display.create(WIDTH,HEIGHT,CLEAR_COLOR,NUM_BUFFERS,TITLE);
        graphics = Display.getGraphics();
        input = new Input();
        Display.addInputListener(input);
        atlas = new TextureAtlas(ATLAS_FILENAME);
        player = new Player(300, 300, 3, 3, atlas);
        level = new Level(atlas);
    }

    public synchronized void start() {
        if (running) {
            return;
        }

        running = true;

        gameThread = new Thread(this); // создание игрового потока
        gameThread.start();
    }

    public synchronized void stop() {
        if (!running) {
            return;
        }

        running = false;

        try {
            gameThread.join(); // объединение потоков
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cleanUp(); // очистка
    }

    private void update() { // вся физика
        player.update(input);
        level.update();
    }

    private void render() { // отрисовка следующей сцены
        Display.clear(); // очистка экрана

        level.render(graphics);
        player.render(graphics);
        level.renderGrass(graphics);

        Display.swapBuffers();
    }

    public void run() { // держит в себе гейм луп
        int fps = 0; // собственно кол-во кадров в секунду
        int upd = 0; // кол-во вызовов фун-ии апдейт
        int updl = 0; // кол-во дополнительных вызовов апдейт

        long count = 0; // время проходящее по ходу игры

        float delta = 0; // кол-во апдейтев которое необходимо сделать

        long lastTime = Time.get(); // временная отметка последней итерации

        while (running) {
            long now = Time.get(); // временная отметка нынешней итерации
            long elapsedTime = now - lastTime; // время прошедшее с последней итерации
            lastTime = now;

            count += elapsedTime;

            boolean render = false;
            delta += (elapsedTime / UPDATE_INTERVAL);
            while (delta > 1) { // вайл в виду более медленых ПК
                update();
                upd++;
                delta--;
                if (render) {
                    updl++;
                }
                else {
                    render = true; // был сделан апдей, значит пора рисовать
                }
            }

            if (render) {
                render();
                fps++;
            }
            else {
                try {
                    Thread.sleep(IDLE_TIME); // дать остальным процессам что-то сделать
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (count >= Time.SECOND) {
                Display.setTitle(TITLE + "|| Fps: " + fps + "| Upd: " + upd + "| Updl: " + updl);
                upd = 0;
                fps = 0;
                updl = 0;
                count = 0;
            }
        }
    }

    private void cleanUp() { // очистка ресурсов
        Display.destroy();
    }
}
