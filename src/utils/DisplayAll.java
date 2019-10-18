package utils;

import processing.core.PApplet;

public class DisplayAll extends PApplet{

    public static void main(String[] args) {
        PApplet.main("utils.DisplayAll");
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