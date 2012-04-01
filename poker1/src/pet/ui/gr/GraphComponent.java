package pet.ui.gr;

import java.awt.*;
import javax.swing.*;

public class GraphComponent extends JComponent {
	
	public static void main(String[] args) {
		GraphData data = new GraphData() {
			@Override
			public String getXName(int x) {
				return Double.toString(x / 100.0);
			}
			@Override
			public String getYName(int y) {
				return Double.toString(y / 100.0);
			}
		};
		data.name = "sine wave";
		for (float x = -2; x < 2; x += 0.05f) {
			float y = (float) Math.sin(x);
			data.points.add(new GraphDataPoint((int)(x*100.0), (int)(y*100.0)));
		}
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GraphComponent g = new GraphComponent();
		g.setData(data);
		g.setPreferredSize(new Dimension(800,600));
		f.setContentPane(g);
		f.pack();
		f.show();
	}
	
	private GraphData data;
	private int minx, miny, maxx, maxy;
	
	public GraphComponent() {
		//
	}
	
	public void setData(GraphData data) {
		this.data = data;
		System.out.println("data points: " + data.points.size());
		int minx = Integer.MAX_VALUE;
		int miny = Integer.MAX_VALUE;
		int maxx = Integer.MIN_VALUE;
		int maxy = Integer.MIN_VALUE;
		for (GraphDataPoint d : data.points) {
			int x = d.getX();
			if (x > maxx) {
				maxx = x;
			}
			if (x < minx) {
				minx = x;
			}
			int y = d.getY();
			if (y > maxy) {
				maxy = y;
			}
			if (y < miny) {
				miny = y;
			}
		}
		System.out.println(String.format("x: %d to %d, y: %d to %d", minx, maxx, miny, maxy));
		this.minx = minx;
		this.miny = miny;
		this.maxx = maxx;
		this.maxy = maxy;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		if (data == null)
			return;
		
		int w = getWidth();
		int h = getHeight();
		
		if (data.name != null) {
			g.setColor(Color.black);
			int sw = g.getFontMetrics().stringWidth(data.name);
			g.drawString(data.name, (w - sw) / 2, h / 20);
		}
		
		int xm = w / 10;
		int ym = h / 20;
		
		int gw = w - xm * 2;
		int gh = h - ym * 2;

		int vminx = getVX(gw, xm, minx);
		int vminy = getVY(gh, ym, miny);
		int vmaxx = getVX(gw, xm, maxx);
		int vmaxy = getVY(gh, ym, maxy);

		g.setColor(Color.red);
		// x axis
		g.drawLine(vminx, vminy, vmaxx, vminy);
		for (int xx = 0; xx <= 10; xx++) {
			int x = minx + ((maxx - minx) * xx) / 10;
			int vx = getVX(gw, xm, x);
			g.drawLine(vx, vminy, vx, vminy + 5);
			String n = data.getXName(x);
			int sw = g.getFontMetrics().stringWidth(n);
			int sh = g.getFontMetrics().getHeight();
			g.drawString(n, vx - (sw / 2), vminy + sh + 2);
		}
		// y axis
		g.drawLine(vminx, vminy, vminx, vmaxy);
		for (int yy = 0; yy <= 10; yy++) {
			int y = miny + ((maxy - miny) * yy) / 10;
			int vy = getVY(gh, ym, y);
			g.drawLine(vminx, vy, vminx - 5, vy);
			String n = data.getYName(y);
			int sw = g.getFontMetrics().stringWidth(n);
			int sh = g.getFontMetrics().getHeight();
			g.drawString(n, vminx - sw - 10, vy + (sh / 2) - 3);
		}

		g.setColor(Color.black);
		GraphDataPoint pd = data.points.get(0);
		for (int n = 1; n < data.points.size(); n++) {
			GraphDataPoint d = data.points.get(n);
			int x1 = getVX(gw, xm, pd.getX());
			int y1 = getVY(gh, ym, pd.getY());
			int x2 = getVX(gw, xm, d.getX());
			int y2 = getVY(gh, ym, d.getY());
			g.drawLine(x1, y1, x2, y2);
			pd = d;
		}

	}

	/** get view x for graph width and x margin */
	int getVX(int gw, int xm, int x) {
		return (((x - minx) * gw) / (maxx - minx)) + xm;
	}

	/** get view y for graph height and y margin */
	int getVY(int gh, int ym, int y) {
		return (gh - (((y - miny) * gh) / (maxy - miny))) + ym;
	}

}