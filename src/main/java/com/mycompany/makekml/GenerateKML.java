/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.makekml;

import org.osgeo.proj4j.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import static com.mycompany.makekml.SformirMetkyOZ3.getTime;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author user
 */
public class GenerateKML {

    private File filePath;
    private String harakterZeli;
    private String zametka;
    private double x;
    private double y;
    private String nameOSs;

    public GenerateKML(String fileName, MetkaFrame frame) {
        this.filePath = new File("D:\\YO_NA\\Generate kml OZ" + "\\" + fileName + ".kml");
        this.harakterZeli = frame.getpHarakterZeli().getText();
        this.zametka = frame.getpZametka().getText();
        this.x = Double.parseDouble(frame.getpXc().getText());
        this.y = Double.parseDouble(frame.getpYc().getText());
        this.nameOSs = frame.getpKtoVupZadachy().getText();
    }

    public void generate() {
        List<Double> bl = refactorXYtoBL(x, y);
        double b = bl.get(0);
        double l = bl.get(1);

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                 new FileOutputStream(filePath),"UTF-8"))) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<kml xmlns=\"http://earth.google.com/kml/2.2\">\n"
                    + "  <Document>\n"
                    + "    <Placemark>\n");
            writer.write("<name>" + getTime() + " " + harakterZeli + "</name>\n");

            writer.write("<description>" + zametka + "\n"
                    + nameOSs + "</description>\n");
            writer.write("<Style>\n"
                    + "        <LabelStyle>\n"
                    + "          <color>FF00FFFF</color>\n"
                    + "          <scale>1.36363636363636</scale>\n"
                    + "        </LabelStyle>\n"
                    + "        <IconStyle>\n"
                    + "          <scale>0.625</scale>\n"
                    + "          <Icon>\n"
                    + "            <href>files/Разрыв.png</href>\n"
                    + "          </Icon>\n"
                    + "          <hotSpot x=\"0.5\" y=\"0\" xunits=\"fraction\" yunits=\"fraction\"/>\n"
                    + "        </IconStyle>\n"
                    + "      </Style>\n"
                    + "      <Point>\n"
                    + "        <extrude>1</extrude>\n");
            writer.write("<coordinates>" + l + "," + b + "," + 0 + "</coordinates>\n");
            writer.write(" </Point>\n"
                    + "  </Placemark>\n"
                    + " </Document>\n"
                    + "</kml>");
        } catch (IOException e) {
            System.out.println("Шляпа: " + GenerateKML.class.getName() + e.getMessage());
        }

    }

    public static List<Double> refactorXYtoBL(double x, double y) {
        List<Double> list = new ArrayList();
        x += 5300000;
        if (y > 50000) {
            y += 7300000;
        } else {
            y += 7400000;
        }
// Создаем исходную и целевую системы координат
        CRSFactory factory = new CRSFactory();
        CoordinateReferenceSystem srcCRS = factory.createFromName("EPSG:28407");
        CoordinateReferenceSystem dstCRS = factory.createFromName("EPSG:4326");

        // Создаем объект для преобразования координат
        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
        CoordinateTransform transform = ctFactory.createTransform(srcCRS, dstCRS);

        // Преобразуем координаты
        //сначала вводим долготу потом широту
        ProjCoordinate srcCoord = new ProjCoordinate(y, x);
        ProjCoordinate dstCoord = new ProjCoordinate();
        transform.transform(srcCoord, dstCoord);

        // Выводим результат(наоборот x->y)
        //System.out.println("Преобразованные координаты: " + dstCoord.x + ", " + dstCoord.y);
        //возвращаем масив b, l
        list.add(dstCoord.y);
        list.add(dstCoord.x);
        return list;
    }

}