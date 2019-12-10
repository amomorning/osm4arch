package utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.locationtech.jts.geom.Coordinate;

import dxfExporter.Constants;
import dxfExporter.DXFData;
import dxfExporter.DXFExport;
import dxfExporter.DXFLayer;
import dxfExporter.DXFPoint;
import wblut.geom.WB_Coord;
import wblut.geom.WB_CoordCollection;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;
import wblut.geom.WB_Polygon;

/**
 * �������DXF�ļ��ķ�������
 * 
 * @author ����
 *
 */
public class ExportDXF {
	/**
	 * ���Գ���
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		WB_Point a = new WB_Point(0, 1, 0);
		WB_Point b = new WB_Point(10, 1, 0);
		WB_Point c = new WB_Point(8, 2, 0);
		WB_Point d = new WB_Point(5, 3, 0);
		WB_Polygon polygon = new WB_Polygon(a, b, c, d);
		ExportDXF dxf = new ExportDXF();
		dxf.add(polygon, ExportDXF.BROKEN);
		dxf.save("F:/562.dxf");
	}

	/**
	 * д���ֵ���
	 */
	private Number number;
	/**
	 * ����߶�
	 */
	private double height = 2;
	/**
	 * DXF���������
	 */
	private DXFExport wt;
	/**
	 * DXF���ݴ洢
	 */
	private DXFData dt;
	/**
	 * DXFͼ��
	 */
	private DXFLayer markLine, brokenLine, dotLine, text;
	/**
	 * DXFͼ���Ӧ����
	 */
	public static Integer MARK = 0, BROKEN = 1, DOT = 2, TEXT = 3;
	/**
	 * ͼ�������ֵ�ƥ���ϵ
	 */
	private Map<Integer, DXFLayer> map;

	/**
	 * ��ʼ��
	 */
	public ExportDXF() {
		setLayer();
		setMap();
	}

	/**
	 * ����ͼ�������ֵ�ƥ���ϵ
	 */
	private void setMap() {
		map = new LinkedHashMap<>();
		map.put(MARK, markLine);
		map.put(BROKEN, brokenLine);
		map.put(DOT, dotLine);
		map.put(TEXT, text);
	}

	/**
	 * ����ͼ����ɫ������
	 */
	private void setLayer() {
		wt = new DXFExport();
		wt.AutoCADVer = Constants.DXFVERSION_R2000;
		dt = new DXFData();

		// ������
		markLine = new DXFLayer("markLine");
		markLine.setColor(Constants.convertColorRGBToDXF(Color.YELLOW));

		// ������
		brokenLine = new DXFLayer("brokenLine");
		brokenLine.setColor(Constants.convertColorRGBToDXF(Color.RED));

		// ������
		dotLine = new DXFLayer("dotLine");
		dotLine.setColor(Constants.convertColorRGBToDXF(Color.BLUE));

		// ����
		text = new DXFLayer("text");
		text.setColor(Constants.convertColorRGBToDXF(Color.GREEN));
	}

	/**
	 * ��ָ��ͼ����Ӷ��WB_Polygon/WB_PolyLine
	 * 
	 * @param polys �����/����߼���
	 * @param layer ָ��ͼ��
	 */
	public void add(List<?> polys, Integer layer) {
		String name = polys.get(0).getClass().getName();
		setEnvironment(layer);
		switch (name) {
		case "wblut.geom.WB_Polygon":
			for (int i = 0; i < polys.size(); i++) {
				drawpolygon(wt, dt, (WB_Polygon) polys.get(i));
			}
			break;
		case "wblut.geom.WB_PolyLine":
			for (int i = 0; i < polys.size(); i++) {
				drawWBPolyLine(wt, dt, (WB_PolyLine) polys.get(i));
			}
		default:
			break;
		}
	}

	/**
	 * ���Բ
	 * 
	 * @param centers
	 * @param radius
	 * @param layer
	 */
	public void add(List<WB_Coord> centers, double radius, Integer layer) {
		setEnvironment(layer);
		for (int i = 0; i < centers.size(); i++) {
			drawCircle(wt, dt, centers.get(i), radius);
		}
	}

	/**
	 * ��ָ��ͼ�����һ��WB_Polygon
	 * 
	 * @param poly  �����
	 * @param layer ָ��ͼ��
	 */
	public void add(WB_Polygon poly, Integer layer) {
		setEnvironment(layer);
		drawpolygon(wt, dt, poly);
	}

	/**
	 * ���Բ
	 * 
	 * @param center Բ��
	 * @param radius �뾶
	 * @param layer  ͼ��
	 */
	public void add(WB_Coord center, double radius, Integer layer) {
		setEnvironment(layer);
		drawCircle(wt, dt, center, radius);
	}

	/**
	 * ��Ӷ����
	 * 
	 * @param line  �����
	 * @param layer ͼ��
	 */
	public void add(WB_PolyLine line, Integer layer) {
		setEnvironment(layer);
		drawWBPolyLine(wt, dt, line);
	}




	/**
	 * ����ָ��ͼ��Ļ�ͼ���� s
	 * 
	 * @param layer ָ��ͼ��
	 */
	private void setEnvironment(Integer layer) {
		wt.setCurrentLayer(map.get(layer));
		dt = new DXFData();
		dt.LayerName = map.get(layer).getName();
		dt.Color = map.get(layer).getColor();
	}

	/**
	 * ��ָ��·���洢DXF�ļ�
	 * 
	 * @param filePath �ļ�·��
	 */
	public void save(String filePath) {
		try {
			wt.saveToFile(filePath);
		} catch (Exception excpt) {
		} finally {
			wt.finalize();
			System.out.println("dxf saved");
		}
		System.out.println("finish-export");
	}

	/**
	 * ��ֱ��
	 * 
	 * @param wt
	 * @param dt
	 * @param a
	 * @param b
	 */
	@SuppressWarnings("unused")
	private void drawline(DXFExport wt, DXFData dt, WB_Coord a, WB_Coord b) {
		dt.Point = new DXFPoint(a.xf(), a.yf(), 0);
		dt.Point1 = new DXFPoint(b.xf(), b.yf(), 0);
		wt.addLine(dt);
	}

	/**
	 * ��Բ
	 * 
	 * @param wt
	 * @param dt
	 * @param p
	 * @param radius
	 */
	private void drawCircle(DXFExport wt, DXFData dt, WB_Coord p, double radius) {
		dt.Point = new DXFPoint(p.xf(), p.yf(), 0);
		dt.Radius = (float) radius;
		wt.addCircle(dt);
	}

	/**
	 * �������
	 * 
	 * @param wt
	 * @param dt
	 * @param polygon
	 */
	private void drawpolygon(DXFExport wt, DXFData dt, WB_Polygon polygon) {
		WB_CoordCollection coords = polygon.getPoints();
		drawWB_CoordCollection(wt, dt, coords, true);
	}

	/**
	 * �������
	 * 
	 * @param wt
	 * @param dt
	 * @param polyLine �����
	 */
	private void drawWBPolyLine(DXFExport wt, DXFData dt, WB_PolyLine polyLine) {
		WB_CoordCollection coords = polyLine.getPoints();
		drawWB_CoordCollection(wt, dt, coords, false);
	}

	/**
	 * ���㼯���ƶ����
	 * 
	 * @param wt
	 * @param dt
	 * @param coords �㼯
	 * @param close  �ö�����Ƿ�պ�
	 */
	private void drawWB_CoordCollection(DXFExport wt, DXFData dt, WB_CoordCollection coords, boolean close) {
		dt.Count = coords.size();
		if (close)
			dt.Count++;
		dt.Points = new ArrayList<DXFPoint>();
		for (int i = 0; i < coords.size(); i++) {
			addPoint(dt, coords.get(i));
		}
		if (close)
			addPoint(dt, coords.get(0));
		wt.addPolyline(dt);
	}

	/**
	 * ��ӵ�
	 * 
	 * @param dt
	 * @param coord
	 */
	@SuppressWarnings("unchecked")
	private void addPoint(DXFData dt, WB_Coord coord) {
		dt.Points.add(new DXFPoint(coord.xf(), coord.yf(), 0));
	}

	/**
	 * ���Coordinate����
	 * 
	 * @param wt
	 * @param dt
	 * @param coords
	 */
	@SuppressWarnings("unchecked")
	private void drawCoords(DXFExport wt, DXFData dt, Coordinate[] coords) {
		dt.Count = coords.length;
		dt.Points = new ArrayList<DXFPoint>();
		for (int i = 0; i < coords.length; i++) {
			dt.Points.add(new DXFPoint((float) coords[i].x, (float) coords[i].y, 0));
		}
		wt.addPolyline(dt);
	}

}
