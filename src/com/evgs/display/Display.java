package com.evgs.display;

import com.evgs.IO.Input;

import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

public abstract class Display {
    private static boolean created = false; // статус окна
    private static JFrame window; // собственно окно
    private static Canvas content; // собственно поле для отрисовки контента

    private static BufferedImage buffer; // буфер хранящий изображение
    private static int[] bufferData; // буфер с данными изображения
    private static Graphics bufferGraphics; // графика изображения
    private static int clearColor;

    private static BufferStrategy bufferStrategy; // организация буферов изображения

    public static void create(int width, int height, int _clearColor, int numBuffers, String title) {
        if (created) {
            return; // если окно уже создано, выйти
        }

        window = new JFrame(title);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // выход по нажатию крестика
        content = new Canvas();

        Dimension size = new Dimension(width, height);
        content.setPreferredSize(size); // размер окна при запуске

        window.setResizable(false); // пользователь не может менять размер
        window.getContentPane().add(content);// только внутренняя часть окна
        window.pack(); // подгонка окна
        window.setLocationRelativeTo(null); // отображение по центру
        window.setVisible(true); // делает окно видимым

        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB); // создание изображения с хранением байтов в виде одномерного массива
        bufferData = ((DataBufferInt) buffer.getRaster().getDataBuffer()).getData(); // извлечениее данных из изображения
        bufferGraphics = buffer.getGraphics(); // получение графики
        ((Graphics2D) bufferGraphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // сглаживание
        clearColor = _clearColor;

        content.createBufferStrategy(numBuffers);
        bufferStrategy = content.getBufferStrategy();

        created = true;
    }

    public static void clear() {
        Arrays.fill(bufferData, clearColor); // вместо for loop по bufferData
    }

    public static void swapBuffers() { // извлечение буферов
        Graphics g = bufferStrategy.getDrawGraphics(); // создание графического контекста для буфера рисования
        g.drawImage(buffer, 0, 0, null); // отрисовка изображения
        bufferStrategy.show(); // делает следующий доступный буфер видимым
    }

    public static Graphics2D getGraphics() {  // возвращение графического объекта
        return (Graphics2D) bufferGraphics;
    }

    public static void destroy() { // правильное уничтожение окна
        if (!created) {
            return;
        }

        window.dispose();
    }

    public static void setTitle(String title) {
        window.setTitle(title);
    }

    public static void addInputListener(Input inputListener) {
        window.add(inputListener);
    }
}
