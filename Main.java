package com.valeriotor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.BiConsumer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import com.valeriotor.mosfet.CMOS;
import com.valeriotor.mosfet.MOSFET;
import com.valeriotor.mosfet.NMOS;
import com.valeriotor.mosfet.PMOS;

public class Main {
	
	public static final int GRAPH_SIZE = 400;
	public static final double DEFAULT_SUPPLY = 10;
	public static final double DEFAULT_INPUT = 4;
	
	public static void main(String[] args) {
		MOSFET pmos = new PMOS(-1.5, new BigDecimal(BigInteger.ONE, 8), new BigDecimal(BigInteger.ONE, 6), new BigDecimal(BigInteger.ONE, 6), new BigDecimal(BigInteger.valueOf(1), 1));
		MOSFET nmos = new NMOS(1.5, new BigDecimal(BigInteger.ONE, 8), new BigDecimal(BigInteger.ONE, 6), new BigDecimal(BigInteger.ONE, 6), new BigDecimal(BigInteger.valueOf(1), 1));
		CMOS cmos = new CMOS(nmos, pmos);
		
		JFrame j = new JFrame();
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		j.setBounds(0, 0, 1920, 1080);
		
		GraphComponent graphCompOutput = new GraphComponent(150, 500, GRAPH_SIZE, GRAPH_SIZE, cmos::drawOutputVoltage);
		GraphComponent graphCompCurrent = new GraphComponent(150, 500, GRAPH_SIZE, GRAPH_SIZE, cmos::drawCurrent);
		GraphComponent[] graphComps = {graphCompOutput, graphCompCurrent};
		
		JPanel centralPanel = new JPanel();
		centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.X_AXIS));
		JSeparator separateCenter = new JSeparator(JSeparator.VERTICAL);
		separateCenter.setPreferredSize(new Dimension(1, 200));
		
		//centralPanel.add(separateCenter, BorderLayout.EAST);
		JComponent miniCentralPanel = new JPanel();
		miniCentralPanel.setPreferredSize(new Dimension(3, 1080));
		//centralPanel.add(miniCentralPanel);
		centralPanel.add(graphCompOutput);
		centralPanel.add(graphCompCurrent);
		centralPanel.setBackground(Color.WHITE);

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());
		//leftPanel.setPreferredSize(new Dimension(450, 600));
		JPanel leftTopPanel = new JPanel();
		
		JLabel vIn = new JLabel("V IN:");
		JLabel vDD = new JLabel("V DD:");
		
		InputFieldDouble inputVoltageListener = new InputFieldDouble(10, 10, 20, 20, cmos::setInputVoltage, graphComps);
		InputFieldDouble supplyVoltageListener = new InputFieldDouble(10, 10, 20, 20, cmos::setSupplyVoltage, graphComps);
		inputVoltageListener.setText(Double.toString(DEFAULT_INPUT));
		supplyVoltageListener.setText(Double.toString(DEFAULT_SUPPLY));
		
		leftTopPanel.add(vIn);
		leftTopPanel.add(inputVoltageListener);
		leftTopPanel.add(vDD);
		leftTopPanel.add(supplyVoltageListener);
		
		leftPanel.add(leftTopPanel, BorderLayout.NORTH);
		
		JPanel leftCenterPanel = new JPanel();
		leftCenterPanel.setPreferredSize(new Dimension(500, 800));
		leftCenterPanel.setLayout(new BoxLayout(leftCenterPanel, BoxLayout.Y_AXIS));
		
		InputFieldsBigDecimal nmosWidth 		= new InputFieldsBigDecimal(cmos, nmos::setWidth, 		"NMOS WIDTH:    ", "m            ", nmos.width, graphComps);
		InputFieldsBigDecimal nmosLength 		= new InputFieldsBigDecimal(cmos, nmos::setLength, 		"NMOS LENGTH:   ", "m            ", nmos.length, graphComps);
		InputFieldsBigDecimal nmosTox 			= new InputFieldsBigDecimal(cmos, nmos::setOxideWidth, 	"NMOS TOX:          ", "m            ", nmos.tox, graphComps);
		InputFieldsBigDecimal nmosMobility	 	= new InputFieldsBigDecimal(cmos, nmos::setMobility, 	"NMOS MOBILITY: ", "m^2/(V*s)", nmos.mobility, graphComps);
		
		InputFieldsBigDecimal pmosWidth 		= new InputFieldsBigDecimal(cmos, pmos::setWidth, 		"PMOS WIDTH:    ", "m            ", pmos.width, graphComps);
		InputFieldsBigDecimal pmosLength 		= new InputFieldsBigDecimal(cmos, pmos::setLength, 		"PMOS LENGTH:   ", "m            ", pmos.length, graphComps);
		InputFieldsBigDecimal pmosTox 			= new InputFieldsBigDecimal(cmos, pmos::setOxideWidth, 	"PMOS TOX:          ", "m            ", pmos.tox, graphComps);
		InputFieldsBigDecimal pmosMobility	 	= new InputFieldsBigDecimal(cmos, pmos::setMobility, 	"PMOS MOBILITY: ", "m^2/(V*s)", pmos.mobility, graphComps);
		
		nmosWidth.setAlignmentX(Component.LEFT_ALIGNMENT);
		nmosLength.setAlignmentX(Component.LEFT_ALIGNMENT);
		nmosTox.setAlignmentX(Component.LEFT_ALIGNMENT);
		nmosMobility.setAlignmentX(Component.LEFT_ALIGNMENT);
		pmosWidth.setAlignmentX(Component.LEFT_ALIGNMENT);
		pmosLength.setAlignmentX(Component.LEFT_ALIGNMENT);
		pmosTox.setAlignmentX(Component.LEFT_ALIGNMENT);
		pmosMobility.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JSeparator topSeparator = new JSeparator(JSeparator.HORIZONTAL);
		JSeparator centerSeparator = new JSeparator(JSeparator.HORIZONTAL);
		JSeparator bottomSeparator = new JSeparator(JSeparator.HORIZONTAL);
		
		leftCenterPanel.add(new JLabel(" "));
		leftCenterPanel.add(topSeparator);
		leftCenterPanel.add(nmosWidth);
		leftCenterPanel.add(nmosLength);
		leftCenterPanel.add(nmosTox);
		leftCenterPanel.add(nmosMobility);
		leftCenterPanel.add(centerSeparator);
		leftCenterPanel.add(pmosWidth);
		leftCenterPanel.add(pmosLength);
		leftCenterPanel.add(pmosTox);
		leftCenterPanel.add(pmosMobility);
		leftCenterPanel.add(bottomSeparator);
		
		leftPanel.add(leftCenterPanel, BorderLayout.CENTER);
		leftPanel.add(new JSeparator(JSeparator.VERTICAL), BorderLayout.EAST);

		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		
		JLabel vOut = new JLabel();
		JLabel vM 	= new JLabel();
		JLabel I 	= new JLabel();
		
		vOut.setLocation(0, 100);
		
		cmos.setVOUTLabel(vOut);
		cmos.setVMLabel(vM);
		cmos.setILabel(I);
		cmos.updateLabels();
		
		rightPanel.add(vOut);
		rightPanel.add(vM);
		rightPanel.add(I);
		
		rightPanel.setLocation(0, 100);
		rightPanel.setBackground(Color.WHITE);
		
		j.getContentPane().add(leftPanel, BorderLayout.WEST);
		j.getContentPane().add(centralPanel, BorderLayout.CENTER);
		j.getContentPane().add(rightPanel, BorderLayout.EAST);
		j.setVisible(true);
	}
	
	public static double[] getArray(int size, double multiplier) {
		double[] array = new double[size];
		for(int i = 0; i < size; i++) array[i] = i * multiplier;
		return array;
	}
	
	public static class GraphComponent extends JComponent{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private final int xSize;
		private final int ySize;
		private final Graph graph;
		private final BiConsumer<Graph, Graphics> drawFunc;
		
		public GraphComponent(int x, int y, int xSize, int ySize, BiConsumer<Graph, Graphics> drawFunc) {
			this.graph = new Graph(x, y, xSize, ySize);
			this.drawFunc = drawFunc;
			this.xSize = xSize;
			this.ySize = ySize;
			this.setPreferredSize(new Dimension(30, 30));
		}
		
		@Override
		public void paint(Graphics g) {
			graph.drawAxis(g, xSize, ySize);
			this.drawFunc.accept(graph, g);
			super.paint(g);
		}
	}
}
