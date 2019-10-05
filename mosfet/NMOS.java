package com.valeriotor.mosfet;

import java.math.BigDecimal;

public class NMOS extends MOSFET{


	public NMOS(double vT, BigDecimal tox, BigDecimal width, BigDecimal length, BigDecimal mobility) {
		super(vT, tox, width, length, mobility);
	}
	
	@Override
	public double getCurrent(double vD) {
		if(vG - vS <= vT) return 0;
		if(!isSaturated(vD)) return getConstant().multiply(BigDecimal.valueOf((vD - vS) *(vG - vS - vT - (vD - vS)/2))).doubleValue();
		return getConstant().multiply(BigDecimal.valueOf((vG - vS - vT) * (vG - vS - vT)/2)).doubleValue();
	}
	
	@Override
	public boolean isSaturated(double vD) {
		return vD > vG - vT;
	}
}