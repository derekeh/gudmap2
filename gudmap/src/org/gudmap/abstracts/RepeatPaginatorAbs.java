package org.gudmap.abstracts;

import java.util.List;
/**
 * Interface methods for paginator which takes in and processes the whole model.
 * <p>For example, all the subsequent work is done client side on the model with no return to the DB server.
 * This is not the preferred option at present. Instead we use {@link PagerAbs} so that a definite number of records are retrieved per request.
 * @author dhoughto
 *
 */


public abstract interface RepeatPaginatorAbs {
	
	public abstract void updateModel();
	public abstract void next();
	public abstract void prev();
	public abstract void firstpage();
	public abstract void lastpage();
	public abstract int getRecords();
	public abstract List<?> getModel();

}
