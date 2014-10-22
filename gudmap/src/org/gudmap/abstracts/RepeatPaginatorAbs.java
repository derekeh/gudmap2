package org.gudmap.abstracts;

import java.util.List;

public abstract interface RepeatPaginatorAbs {
	
	public abstract void updateModel();
	public abstract void next();
	public abstract void prev();
	public abstract void firstpage();
	public abstract void lastpage();
	public abstract int getRecords();
	public abstract List<?> getModel();

}
