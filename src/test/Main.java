package test;

import java.io.UnsupportedEncodingException;
import java.util.List;

import utils.Container;
import utils.Gpoi;
import wblut.geom.WB_Point;

public class Main {

	public static void main(String[] args) {
		Container.initGmaps();
		int cnt = 0;
		for(Gpoi p : Container.gpois) {
			if(p.isChinese()) continue;
			cnt += p.getName().length();
		}
		System.out.println("cnt = " + cnt);
		
	}

	public static int countChar (String str) {
		int cnt = 0;
		for(int i = 0; i < str.length(); ++ i) {
			if(str.charAt(i) == ' ') {
				continue;
			}
			cnt ++;
		}
		return cnt;
	}
	public static double changeD(List<WB_Point> pts, double d) {
		pts.add(new WB_Point());
		return d / 2;

	}

}
