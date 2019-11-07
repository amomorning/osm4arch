package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import crosby.binary.osmosis.OsmosisReader;
import osm.PbfReader;
import processing.core.PApplet;
import wblut.geom.WB_Network;
import wblut.geom.WB_Point;
import wblut.hemesh.HE_Mesh;

public class DisplayAll extends PApplet{
    Tools tools;
    public static final int LEN_OF_CAMERA = 100;
    
    HE_Mesh mesh;

    public static void main(String[] args) {
        Container.initAll();
        try {
            InputStream inputStream = new FileInputStream(Container.OSM_FILENAME);
            OsmosisReader reader = new OsmosisReader(inputStream);
            reader.setSink(new PbfReader());
            reader.run();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }




        PApplet.main("utils.DisplayAll");
       
    }

    public void settings(){
        size(1800,1000, P3D);
    }

    public void setup(){
        tools = new Tools(this, LEN_OF_CAMERA);

        WB_Network network=new WB_Network();
        for(WB_Point pts : Container.points) {
            network.addNode(pts, 0);
        }
        
    }

    public void draw(){
        background(0);
        tools.cam.openLight();
        tools.cam.drawSystem(LEN_OF_CAMERA);
        stroke(255, 0, 0);
        for(WB_Point pts : Container.points) {
//            System.out.println(pts);
            tools.render.drawPoint(pts);
        }
    }
    
}