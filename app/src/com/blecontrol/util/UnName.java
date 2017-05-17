package com.blecontrol.util;

import java.io.Serializable;

public class UnName implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6187403869565358766L;
	private String Name;
	private String MAC;

	
	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getMAC() {
		return MAC;
	}

	public void setMAC(String mAC) {
		MAC = mAC;
	}

}
