package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import crosby.binary.osmosis.OsmosisReader;
import osm.PbfReader;
import processing.core.PApplet;
import wblut.geom.WB_Point;

public class DisplayAll extends PApplet{

    public static void main(String[] args) {
        Container.points = new ArrayList<WB_Point>();
        try {
            InputStream inputStream = new FileInputStream(Container.OSM_FILENAME);
            OsmosisReader reader = new OsmosisReader(inputStream);
            reader.setSink(new PbfReader());
            reader.run();
//            PbfReader.main(pts);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        System.out.println(Container.points.size());
        double[] d = new double[] {1, 2};
        WB_Point pt = new WB_Point(d);
        System.out.println(pt);
//        PApplet.main("utils.DisplayAll");
        
    }

    public void settings(){
        size(1000,1000, P2D);
    }

    public void setup(){
        fill(120,50,240);
    }

    public void draw(){
        
        ellipse(width/2,height/2,second(),second());
    }
    
}