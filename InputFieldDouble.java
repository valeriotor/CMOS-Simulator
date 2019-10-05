package com.valeriotor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.DoubleUnaryOperator;

import javax.swing.JComponent;
import javax.swing.JTextField;

import com.valeriotor.Main.GraphComponent;

public class InputFieldDouble extends JTextField{
	
	private final DoubleUnaryOperator operator;
	private final GraphComponent[] comps;
	
	public InputFieldDouble(int x, int y, int width, int height, DoubleUnaryOperator operator, GraphComponent... components) {
		this.setBounds(x, y, width, height);
		this.setColumns(10);
		this.operator = operator;
		this.comps = components;
		this.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					setText(Double.toString(operator.applyAsDouble(Double.valueOf(getText()))));
					for(JComponent comp : comps) comp.repaint();
				}catch(Exception exc) {}
			}
		});
	}
	
	

}
