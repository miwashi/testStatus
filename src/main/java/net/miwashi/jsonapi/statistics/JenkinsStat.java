package net.miwashi.jsonapi.statistics;

import net.miwashi.jsonapi.SimpleJob;

public class JenkinsStat {
	private int passes;
	private int fails;
	private int skips;
	private int total;
	
	public JenkinsStat(int passes, int fails, int skips, int total){
		this.passes = passes;
		this.fails = fails;
		this.skips = skips;
		this.total = total;
	}
	
	public int getPasses() {
		return passes;
	}
	public void setPasses(int passes) {
		this.passes = passes;
	}
	public int getFails() {
		return fails;
	}
	public void setFails(int fails) {
		this.fails = fails;
	}
	public int getSkips() {
		return skips;
	}
	public void setSkips(int skips) {
		this.skips = skips;
	}
	public int getTotal() {
		return total;
	}
	
	public void setTotal(int total) {
		this.total = total;
	}
	
	public int getRuns(){
		return getTotal() - getSkips();
	}
	
	public int getFailRatio() {
		return 100 - (getRuns()==0?0:Math.round((100 * (getRuns()-getFails())) / getRuns()));
	}

}
