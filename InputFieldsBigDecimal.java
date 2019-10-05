package com.valeriotor;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.function.UnaryOperator;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.valeriotor.Main.GraphComponent;
import com.valeriotor.mosfet.CMOS;

public class InputFieldsBigDecimal extends JComponent{
	
	private JTextField unscaledValField;
	private JTextField scaleField;
	private UnaryOperator<BigDecimal> operator;
	private CMOS cmos;
	private GraphComponent[] comps;
	
	public InputFieldsBigDecimal(CMOS cmos, UnaryOperator<BigDecimal> operator, String name, String unit, BigDecimal defaultValue, GraphComponent[] comps) {
		this.setLayout(new FlowLayout());
		this.unscaledValField = new JTextField();
		this.scaleField = new JTextField();
		this.unscaledValField.setColumns(10);
		this.scaleField.setColumns(10);
		this.operator = operator;
		ListenerImpl actionListener = new ListenerImpl(this);
		this.unscaledValField.addActionListener(actionListener);
		this.scaleField.addActionListener(actionListener);
		this.add(new JLabel(name));
		this.add(unscaledValField);
		this.add(new JLabel(" * 10^"));
		this.add(scaleField);
		this.add(new JLabel(unit));
		this.setText(defaultValue);
		this.cmos = cmos;
		this.comps = comps;
	}
	
	void performAction() {
		try {
			double val = Double.parseDouble(this.unscaledValField.getText());
			int scale = Integer.parseInt(this.scaleField.getText());
			BigDecimal sent = BigDecimal.valueOf(val).scaleByPowerOfTen(scale);
			BigDecimal received = this.operator.apply(sent);
			this.setText(received);
			cmos.calculateSwitchTreshold();
			cmos.updateLabels();
			for(JComponent comp : comps) comp.repaint();
		} catch(Exception e) {}
	}
	
	void setText(BigDecimal bd) {
		this.unscaledValField.setText(Double.toString(bd.unscaledValue().doubleValue() / Math.pow(10, bd.precision() - 1)));
		this.scaleField.setText(Integer.toString(-bd.scale() + bd.precision() - 1));
	}
	
	private static class ListenerImpl implements ActionListener{
		
		private InputFieldsBigDecimal ifbd;
		
		public ListenerImpl(InputFieldsBigDecimal ifdb) {
			this.ifbd = ifdb;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			this.ifbd.performAction();
		}
		
	}
	
}
