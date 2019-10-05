package com.valeriotor;
import java.awt.Color;
import java.awt.Graphics;
import java.util.function.DoubleUnaryOperator;

public class Graph {
	
	public final int posX;
	public final int posY;
	public final int xSize;
	public final int ySize;
	
	public Graph(int posX, int posY, int xSize, int ySize) {
		this.posX = posX;
		this.posY = posY;
		this.xSize = xSize;
		this.ySize = ySize;
	}
	
	public void drawAxis(Graphics g, int width, int height) {
		g.fillRect(posX - 5, posY, width + 20, 5);
		g.fillRect(posX - 5, posY - height - 15, 5, height + 15);
	}
	
	public void drawGraph(Graphics g, double[] x, DoubleUnaryOperator op) {
		int y = (int)(-op.applyAsDouble(x[0]) * ySize) + posY, nextY = (int)(-op.applyAsDouble(x[1]) * ySize) + posY;
		for(int i = 0; i < x.length - 1; i++) {
			g.drawLine(i + posX, y, i + posX, nextY);
			y = nextY;
			if(i < x.length - 2) nextY = (int)(-op.applyAsDouble(x[i+2]) * ySize) + posY;
		}
	}
	
	public void drawXMarkers(Graphics g, String[] args) {
		for(int i = 0; i < 5; i++) {
			int x = posX + (i+1)*xSize/5;
			g.drawLine(x, posY + 5, x, posY + 7);
			g.drawString(args[i], x - 10, posY + 20);
		}
	}
	
	public void drawYMarkers(Graphics g, String[] args) {
		for(int i = 0; i < 5; i++) {
			int y = posY - (i+1) * ySize / 5;
			g.drawLine(posX - 7, y, posX - 5, y);
			g.drawString(args[i], posX - 70, y + 5);
		}
	}
	
	public void drawAxisNames(Graphics g, String xName, String yName) {
		g.setColor(Color.BLUE);
		g.drawString(xName, posX + xSize + 30, posY + 20);
		g.drawString(yName, posX - 20, posY - ySize - 30);		
	}
	
	public void drawDashedLine(Graphics g, int x, int y) {
		y *= -1;
		y += posY;
		x += posX;
		for(int i = y; i < posY; i+= 10) {
			g.drawLine(x, i, x, i + 5);
		}
	}
	
}
