package test;

import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.ListBox;
import processing.core.PApplet;
import wblut.hemesh.HE_Mesh;
import wblut.processing.WB_Render;

public class Test extends PApplet {
	ControlP5 cp5;

    ListBox l;

    int cnt = 0;

    public static void main(String[] args) {

        PApplet.main("test.Test");
    }

    public void settings() {
      size(700, 400);
    }

    public void setup() {
      
      ControlP5.printPublicMethodsFor(ListBox.class);

      cp5 = new ControlP5(this);
      l = cp5.addListBox("myList")
             .setPosition(100, 100)
             .setSize(120, 120)
             .setItemHeight(15)
             .setBarHeight(15)
             .setColorBackground(color(255, 128))
             .setColorActive(color(0))
             .setColorForeground(color(255, 100,0))
             ;

      l.getCaptionLabel().toUpperCase(true);
      l.getCaptionLabel().set("A Listbox");
      l.getCaptionLabel().setColor(0xffff0000);
      l.getCaptionLabel().getStyle().marginTop = 3;
      l.getValueLabel().getStyle().marginTop = 3;
      
      for (int i=0;i<80;i++) {
        l.addItem("item "+i, i).setColorBackground(0xffff0000);
      }
      
    }

    public void keyPressed() {
      if (key=='0') {
        // will activate the listbox item with value 5
        l.setValue(5);
      }
      if (key=='1') {
        // set the height of a listBox should always be a multiple of itemHeight
        l.setHeight(210);
      } 
      else if (key=='2') {
        // set the height of a listBox should always be a multiple of itemHeight
        l.setHeight(120);
      } 
      else if (key=='3') {
        // set the width of a listBox
        l.setWidth(200);
      }
      else if (key=='i') {
        // set the height of a listBoxItem, should always be a fraction of the listBox
        l.setItemHeight(30);
      } 
      else if (key=='u') {
        // set the height of a listBoxItem, should always be a fraction of the listBox
        l.setItemHeight(10);
        l.setBackgroundColor(color(100, 0, 0));
      } 
      else if (key=='a') {
        int n = (int)(random(100000));
        l.addItem("item "+n, n);
      } 
      else if (key=='d') {
        l.removeItem("item "+cnt);
        cnt++;
      } else if (key=='c') {
        l.clear();
      }
    }

    void controlEvent(ControlEvent theEvent) {
      // ListBox is if type ControlGroup.
      // 1 controlEvent will be executed, where the event
      // originates from a ControlGroup. therefore
      // you need to check the Event with
      // if (theEvent.isGroup())
      // to avoid an error message from controlP5.

      if (theEvent.isGroup()) {
        // an event from a group e.g. scrollList
        println(theEvent.group().getValue()+" from "+theEvent.group());
      }
      
      if(theEvent.isGroup() && theEvent.name().equals("myList")){
        int test = (int)theEvent.group().getValue();
        println("test "+test);
    }
    }

    public void draw() {
		l.continuousUpdateEvents();
      background(128);
      // scroll the scroll List according to the mouseX position
      // when holding down SPACE.
      if (keyPressed && key==' ') {
        //l.scroll(mouseX/((float)width)); // scroll taks values between 0 and 1
      }
      if (keyPressed && key==' ') {
        l.setWidth(mouseX);
      }
    }
    
    public void myList() {
    	l.updateItemIndexOffset();
    	System.out.println(l.getInfo());
    }

}
