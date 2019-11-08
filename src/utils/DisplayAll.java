package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import crosby.binary.osmosis.OsmosisReader;
import javafx.util.Pair;
import osm.PbfReader;
import processing.core.PApplet;
import wblut.geom.WB_Network;
import wblut.geom.WB_Point;
import wblut.hemesh.HEC_FromNetwork;
import wblut.hemesh.HE_Mesh;

public class DisplayAll extends PApplet {
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
            e.printStackTrace();
        }

        PApplet.main("utils.DisplayAll");

    }

    public void settings() {
        size(1800, 1000, P3D);
    }

    public void setup() {
        tools = new Tools(this, LEN_OF_CAMERA);

        WB_Network network = new WB_Network();
        for (WB_Point pts : Container.points) {
            network.addNode(pts, 0);
        }
        for (Pair<Integer, Integer> edge : Container.edges) {
            int u = edge.getKey();
            int v = edge.getValue();
            
            network.addConnection(u, v);
            network.addConnection(v, u);
        }
        
        HEC_FromNetwork creator = new HEC_FromNetwork();
        creator.setNetwork(network);
        mesh = new HE_Mesh(creator);
    }

    public void draw() {
        background(0);
        tools.cam.openLight();
        tools.cam.drawSystem(LEN_OF_CAMERA);
        stroke(255, 0, 0);
        for (WB_Point pts : Container.points) {
            tools.render.drawPoint(pts);
        }
        noStroke();
        fill(255);
        tools.render.drawFaces(mesh);
        stroke(255);
        tools.render.drawEdges(mesh);
    }

}