package com.valeriotor.mosfet;

import static com.valeriotor.Main.GRAPH_SIZE;

import java.awt.Color;
import java.awt.Graphics;
import java.math.RoundingMode;

import javax.swing.JLabel;

import com.valeriotor.Graph;
import com.valeriotor.Main;

public class CMOS {
	
	private double vDD;
	private double vIn = 0;
	private double vM = 0;
	private double maxI;
	public MOSFET nmos;
	public MOSFET pmos;
	
	private JLabel vOutLabel;
	private JLabel vMLabel;
	private JLabel ILabel;
	
	public CMOS(MOSFET nmos, MOSFET pmos) {
		this.nmos = nmos;
		this.pmos = pmos;
		this.setSupplyVoltage(Main.DEFAULT_SUPPLY);
		this.setInputVoltage(Main.DEFAULT_INPUT);
	}
	
	public void drawCurrent(Graph graph, Graphics graphics) {
		double[] x = Main.getArray(GRAPH_SIZE + 1, vDD / GRAPH_SIZE);
		graphics.setColor(Color.RED);
		graph.drawGraph(graphics, x, this::getNMOSAxisCurrent);
		graphics.setColor(Color.BLUE);
		graph.drawGraph(graphics, x, this::getPMOSAxisCurrent);
		graphics.setColor(Color.BLACK);
		String[] args = new String[5];
		for(int i = 0; i < 5; i++) 
			args[i] = String.format("%.1f", ((double)vDD)/5 * (i+1));
		graph.drawXMarkers(graphics, args);
		args = new String[5];
		for(int i = 0; i < 5; i++) 
			args[i] = String.format("%.2e", ((double)maxI)/5 * (i+1));
		graph.drawYMarkers(graphics, args);
		graph.drawAxisNames(graphics, "V OUT", "CURRENT");
	}
	
	public void drawOutputVoltage(Graph graph, Graphics graphics) {
		double[] x = Main.getArray(GRAPH_SIZE + 1, vDD / GRAPH_SIZE);
		graph.drawGraph(graphics, x, this::getOutputVoltageOverSupply);
		String[] args = new String[5];
		for(int i = 0; i < 5; i++) 
			args[i] = String.format("%.1f", ((double)vDD)/5 * (i+1));
		graph.drawXMarkers(graphics, args);
		graph.drawYMarkers(graphics, args);
		graph.drawAxisNames(graphics, "V IN", "V OUT");
		double vOut = getOutputVoltageOverSupply(vIn);
		graph.drawDashedLine(graphics, (int)(graph.xSize *  vIn / vDD), (int) (vOut * graph.ySize));
	}
	
	public double setInputVoltage(double vIn) {
		vIn = Math.min(vDD, Math.max(0, vIn));
		this.vIn = vIn;
		this.nmos.vG = vIn;
		this.pmos.vG = vIn;
		this.updateLabels();
		return vIn;
	}
	
	public double setSupplyVoltage(double vDD) {
		vDD = Math.max(vIn, vDD);
		this.vDD = vDD;
		this.pmos.vS = vDD;
		this.calculateMaxCurrent();
		this.calculateSwitchTreshold();
		this.updateLabels();
		return vDD;
	}
	
	private void calculateMaxCurrent() {
		double temp = this.nmos.vG;
		this.nmos.vG = this.vDD;
		double maxIn = this.nmos.getCurrent(vDD);
		this.nmos.vG = temp;
		temp = this.pmos.vG;
		this.pmos.vG = 0;
		double maxIp = this.pmos.getCurrent(0);
		this.pmos.vG = temp;
		this.maxI = Double.max(maxIp, maxIn);
	}
	
	public void calculateSwitchTreshold() {
		double r = Math.sqrt(this.nmos.getConstant().divide(this.pmos.getConstant(), 30, RoundingMode.HALF_UP).doubleValue());
		this.vM = (r*(this.vDD + this.pmos.vT) + this.nmos.vT)/(1+r);
	}
	
	public double getNMOSAxisCurrent(double vD) {
		double current = (this.nmos.getCurrent((double)vD) / maxI);
		return current;
	}
	
	public double getPMOSAxisCurrent(double vD) {
		double current = (this.pmos.getCurrent((double)vD) / maxI);
		return current;
	}
	
	public double getOutputVoltageOverSupply(double vIn) {
		if(vIn < this.nmos.vT) return 1;
		if(vIn < this.vDD + this.pmos.vT) {
			double sqrtArg = Math.pow(vIn - pmos.vT, 2) - 2 * (vIn - vDD / 2 - pmos.vT) * vDD - nmos.getConstant().divide(pmos.getConstant(), 30, RoundingMode.HALF_UP).doubleValue() * Math.pow(vIn - nmos.vT, 2);
			if(sqrtArg > 0) return (vIn - pmos.vT + Math.sqrt(sqrtArg))/vDD;
			return (vIn - nmos.vT - Math.sqrt((vIn - nmos.vT) * (vIn - nmos.vT) - pmos.getConstant().divide(nmos.getConstant(), 30, RoundingMode.HALF_UP).doubleValue() * Math.pow(vIn - vDD - pmos.vT, 2))) / vDD;
		}
		return 0;
	}
	
	public double setNMOSTreshold(double vt) {
		double returnValue = nmos.setTresholdVoltage(vt);
		this.calculateMaxCurrent();
		this.calculateSwitchTreshold();
		this.updateLabels();
		return returnValue;
	}
	
	public double setPMOSTreshold(double vt) {
		double returnValue = pmos.setTresholdVoltage(vt);
		this.calculateMaxCurrent();
		this.calculateSwitchTreshold();
		this.updateLabels();
		return returnValue;
	}
	
	public void updateLabels() {
		double vOut = this.getOutputVoltageOverSupply(vIn)*vDD;
		if(this.vOutLabel != null) this.vOutLabel.setText(String.format("V OUT: %6.3fV    ", vOut));
		if(this.vMLabel != null) this.vMLabel.setText(String.format("V M: %10.3fV    ", this.vM));
		if(this.ILabel != null) this.ILabel.setText(String.format("I: %15.3fA    ", this.getNMOSAxisCurrent(vOut)));
		this.calculateMaxCurrent();
	}
	
	public void setVMLabel(JLabel label) {this.vMLabel = label;}
	public void setVOUTLabel(JLabel label) {this.vOutLabel = label;}
	public void setILabel(JLabel label) {this.ILabel = label;}
	
}
