package com.evgs.IO;

import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.util.Arrays;

public class Input extends JComponent {
    /* InputMap - название экшона для определённо кнопки
        ActionMap - связывание экшонов и методов
     */
    private boolean[] map; // индексы кей коды, значения означают нажата кнопка или нет

    public Input() {
        map = new boolean[256];

        for (int i = 0; i < map.length; i++) {
            final int KEY_CODE = i;
            // нажатие кнопки
            getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(i, 0, false), i * 2);
            /*
            берём кнопку, слушаем, даём уникальное имя, назначаем действие
            */
            getActionMap().put(i * 2, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    map[KEY_CODE] = true;
                }
            });
            // кнопка отпускается
            getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(i, 0, true), i * 2 + 1);
            getActionMap().put(i * 2 + 1, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    map[KEY_CODE] = false;
                }
            });
        }
    }

    public boolean[] getMap() { // возвращение карты
        return Arrays.copyOf(map, map.length);
    }

    public boolean getKey(int keyCode) { // возвращение статуса кнопки
        return map[keyCode];
    }
}
