/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.makekml;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author user
 */
public class SformirMetkyOZ3 {

    static String[] mass = null;
    static MetkaFrame frame;
    public static Map<String, String[]> listZeli = new HashMap<>();

    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MetkaFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MetkaFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MetkaFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MetkaFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        frame = new MetkaFrame();
        frame.setVisible(true);

        writetList();

        frame.getBtnZapisat().addActionListener((ActionEvent e) -> {
           
            if (!pystoePole(frame)) {
                GenerateKML metka = new GenerateKML(getNewName(frame.getpHarakterZeli().getText()), frame);
                metka.generate();
                System.exit(0);
            }
        });

        frame.getpHarakterZeli().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    frame.getpKtoVupZadachy().setText("");
                    frame.getpKtoVupZadachy().requestFocus();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        frame.getpKtoVupZadachy().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    frame.getpZametka().setText("");
                    frame.getpZametka().requestFocus();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        frame.getpNZeli().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String[] mass = getXY();
                    frame.getpXc().setText(mass[0]);
                    frame.getpYc().setText(mass[1]);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        frame.getpXc().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    frame.getpYc().setText("");
                    frame.getpYc().requestFocus();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    public static String[] getXY() {
        String name = frame.getpNZeli().getText();
        listZeli.forEach((String k, String[] v) -> {
            if (k.equals(name)) {
                mass = v;
            }
        });
        return mass;
    }

    //получение нового имени
    public static String getNewName(String name) {
        boolean proverka = false;
        String nameNew;
        File directory = new File("D:\\YO_NA\\Generate kml OZ");
        File[] list = directory.listFiles();

        nameNew = name + getRandNumber();

        proverka = getSovpadenie(list, nameNew, proverka);

        if (!proverka) {
            return nameNew;
        } else {
            while (proverka) {
                nameNew = name + getRandNumber();
                proverka = getSovpadenie(list, nameNew, proverka);
            }
            return nameNew;
        }

    }

    //проверить папку на совпадение имен
    public static boolean getSovpadenie(File[] list, String name, boolean proverka) {
        if (list != null) {
            for (File file : list) {
                if (file.getName().equals(name)) {
                    proverka = true;
                }
            }
        }
        return proverka;
    }

    //сгенерировать номер
    public static int getRandNumber() {
        int min = 1;
        int max = 1000;
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public static void handlError(IOException e) {
        System.out.println("Shlapa: " + SformirMetkyOZ3.class.getName() + e.getMessage());
    }

    public static void writetList() {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File("D:\\YO_NA\\Zeli")))) {
            String line;
            String[] massStr;
            while ((line = reader.readLine()) != null) {
                massStr = line.split(",");
                listZeli.put(massStr[0], new String[]{massStr[1], massStr[2]});
            }
        } catch (IOException e) {
            handlError(e);
        }
    }

    public static String getTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int hours = cal.get(Calendar.HOUR_OF_DAY); 
        int minutes = cal.get(Calendar.MINUTE);
        return hours + ":" + minutes;
    }

    public static boolean pystoePole(MetkaFrame frame) {
        if (frame.getpHarakterZeli().getText().isEmpty()) {
            return true;
        }
        if (frame.getpKtoVupZadachy().getText().isEmpty()) {
            return true;
        }
        if (frame.getpZametka().getText().isEmpty()) {
            return true;
        }
        if (frame.getpXc().getText().isEmpty()) {
            return true;
        }
        if (frame.getpYc().getText().isEmpty()) {
            return true;
        }

        return false;
    }
}
