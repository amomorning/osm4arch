package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


import crosby.binary.osmosis.OsmosisReader;
import osm.PbfReader;
import processing.core.PApplet;
import wblut.geom.WB_PolyLine;

public class DisplayAll extends PApplet {
    Tools tools;
    public static final int LEN_OF_CAMERA = 100;

    List<WB_PolyLine> ply;

    public static void main(String[] args) {
        Container.initAll();

        try {
            InputStream inputStream = new FileInputStream(Container.OSM_FILENAME);
            OsmosisReader reader = new OsmosisReader(inputStream);
            reader.setSink(new PbfReader());
            reader.run();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        PApplet.main("utils.DisplayAll");

    }

    public void settings() {
        size(1800, 1000, P3D);
    }

    public void setup() {
        tools = new Tools(this, LEN_OF_CAMERA);

        ply = new ArrayList<>();

    }

    public void draw() {
        background(0);
        tools.cam.openLight();
        tools.cam.drawSystem(LEN_OF_CAMERA);
        stroke(255, 0, 0);
        for (Poi poi : Container.pois) {
            tools.render.drawPoint(poi.position);
        }
        noStroke();
        stroke(255);
        tools.render.drawPolylineEdges(ply);

    }

    public void keyPressed() {
        if (key == 'p' || key == 'P') {
            ply = new ArrayList<>();
            for(Aoi aoi : Container.aois) {
                if(aoi.isPiazza()) {
                    ply.add(aoi.ply);
                    aoi.printTag();
                }
            }
        }
        if (key == 'b' || key == 'B') {
            ply = new ArrayList<>();
            for(Aoi aoi : Container.aois) {
                if(aoi.isBuilding()) {
                    ply.add(aoi.ply);
                    aoi.printTag();
                }
            }
        }
        if (key == 'f' || key == 'F') {
            ply = new ArrayList<>();
            for(Aoi aoi : Container.aois) {
                if(aoi.isForest()) {
                    ply.add(aoi.ply);
                    aoi.printTag();
                }
            }
        }
        if (key == 'i' || key == 'I') {
            ply = new ArrayList<>();
            for(Aoi aoi : Container.aois) {
                if(aoi.isIndustrial()) {
                    ply.add(aoi.ply);
                    aoi.printTag();
                }
            }
        }
        if (key == 'r' || key == 'R') {
            ply = new ArrayList<>();
            for(Aoi aoi : Container.aois) {
                if(aoi.isResidential()) {
                    ply.add(aoi.ply);
                    aoi.printTag();
                }
            }
        }
    }
}