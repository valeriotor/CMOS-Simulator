package com.valeriotor.mosfet;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public abstract class MOSFET {
	
	public static final BigDecimal OXIDE_PERMITTIVITY = BigDecimal.valueOf(35, 12);
	
	
	public double vG = 5;
	public double vS = 0;			
	public double vT;				
	public BigDecimal tox; 			
	public BigDecimal width;			
	public BigDecimal length;			
	public BigDecimal mobility;
	private BigDecimal costant;
	
	public MOSFET(double vT, BigDecimal tox, BigDecimal width, BigDecimal length, BigDecimal mobility) {
		this.vT = vT;
		this.tox = tox;
		this.width = width;
		this.length = length;
		this.mobility = mobility;
		this.calculateCostant();
	}
	
	public abstract double getCurrent(double vD);
	
	public abstract boolean isSaturated(double vD);
	
	public BigDecimal getCapacityOverArea() {
		BigDecimal bd = OXIDE_PERMITTIVITY.divide(tox, 30, RoundingMode.HALF_UP); 
		return bd;
	}
	
	public BigDecimal getConstant() {
		return this.costant;
	}
	
	public void calculateCostant() {
		this.costant = getCapacityOverArea().multiply(mobility).multiply(width).divide(length, 30, RoundingMode.HALF_UP);
	}
	
	public BigDecimal setOxideWidth(BigDecimal bd) {
		if(bd.doubleValue() <= 0) bd = new BigDecimal(BigInteger.ONE, 15);
		this.tox = bd;
		this.calculateCostant();
		return this.tox;
	}
	
	public BigDecimal setWidth(BigDecimal bd) {
		if(bd.doubleValue() <= 0) bd = new BigDecimal(BigInteger.ONE, 15);
		this.width = bd;
		this.calculateCostant();
		return this.width;
	}
	
	public BigDecimal setLength(BigDecimal bd) {
		if(bd.doubleValue() <= 0) bd = new BigDecimal(BigInteger.ONE, 15);
		this.length = bd;
		System.out.println(length + " " + bd);
		this.calculateCostant();
		return this.length;
	}
	
	public BigDecimal setMobility(BigDecimal bd) {
		if(bd.doubleValue() <= 0) bd = new BigDecimal(BigInteger.ONE, 15);
		this.mobility = bd;
		this.calculateCostant();
		return this.mobility;
	}
	
}
