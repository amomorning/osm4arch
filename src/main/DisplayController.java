package main;

import java.util.Arrays;

import controlP5.Button;
import controlP5.ControlP5;
import controlP5.DropdownList;
import gmaps.GmapsTypeDetail;
import gmaps.GmapsTypeDetail.Types;
import processing.core.PApplet;
import utils.Container;

public class DisplayController extends PApplet {

	ControlP5 cp5;

	public static DropdownList aoiKey, aoiValue, poiType;
	public static Button keyButton, valueButton, typeButton;
	
	public void settings() {
		size(1000, 500);
	}
	
	public void setup() {
		
		cp5 = new ControlP5(this);
		initGUI();
	}	
	
	public void draw() {
		background(255);
		drawCP5();
	}
	public void drawCP5() {
		this.cp5.update();
		this.cp5.draw();
	}
	
	public void initGUI() {

		aoiKey = cp5.addDropdownList("tagKey").setPosition(100, 100).setSize(240, 240).setItemHeight(30)
				.setBarHeight(30).setColorBackground(color(255, 128)).setColorActive(color(0))
				.setColorForeground(color(255, 100, 0));

		aoiKey.getCaptionLabel().toUpperCase(true);
		aoiKey.getCaptionLabel().setColor(0xffff0000);
		aoiKey.getCaptionLabel().getStyle().marginTop = 3;
		aoiKey.getValueLabel().getStyle().marginTop = 3;

		aoiValue = cp5.addDropdownList("tagValue").setPosition(400, 100).setSize(240, 240).setItemHeight(30)
				.setBarHeight(30).setColorBackground(color(255, 128)).setColorActive(color(0))
				.setColorForeground(color(255, 100, 0));

		aoiValue.getCaptionLabel().toUpperCase(true);
		aoiValue.getCaptionLabel().setColor(0xffff0000);
		aoiValue.getCaptionLabel().getStyle().marginTop = 3;
		aoiValue.getValueLabel().getStyle().marginTop = 3;

		poiType = cp5.addDropdownList("poiType").setPosition(700, 100).setSize(240, 240).setItemHeight(30)
				.setBarHeight(30).setColorBackground(color(255, 128)).setColorActive(color(0))
				.setColorForeground(color(255, 100, 0));

		poiType.getCaptionLabel().toUpperCase(true);
		poiType.getCaptionLabel().setColor(0xffff0000);
		poiType.getCaptionLabel().getStyle().marginTop = 3;
		poiType.getValueLabel().getStyle().marginTop = 3;

		int cnt = 0;
//		l.continuousUpdateEvents();
//		r.continuousUpdateEvents();
		String[] keys = Container.tagList.keySet().toArray(new String[Container.tagList.keySet().size()]);
		Arrays.sort(keys);

		for (String key : keys) {
			aoiKey.addItem(key, cnt++).setLabel(key).setColorBackground(color(0, 0, 255));
		}

		keyButton = cp5.addButton("keyControl").setPosition(100, 80);
		valueButton = cp5.addButton("valueControl").setPosition(400, 80);
		typeButton = cp5.addButton("typeControl").setPosition(700, 80);

		for (Types type : GmapsTypeDetail.Types.values()) {
			String str = type.toString();
//			System.out.println(str);
			poiType.addItem(str, cnt++).setLabel(str).setColorBackground(color(0, 0, 255));
		}
	
	}
	
	public void keyPressed() {
	}

}
