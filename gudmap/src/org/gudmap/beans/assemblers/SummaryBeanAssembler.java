package org.gudmap.beans.assemblers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.gudmap.models.SummaryBeanModel;
import org.gudmap.queries.totals.QueryTotals;
import org.gudmap.globals.Globals;
/* TRIED TO CALL THIS AS @ManagedProperty FROMSUMMARY BEAN BUT DIDN'T WORK. 
 * INSTEAD MAKE IT A SINGLETON SO IT ONLY GETS CALLED ONCE WITHIN THE APP 
 * making it a singleton makes contact with the database but doesn't run the query again. 
 * Leaving it just as a class, the queries are only run once because it  populates the bean on that first run and the bean <SummaryBean> is session scoped
 * */
public class SummaryBeanAssembler {
	
	private DataSource ds;
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	
	public SummaryBeanAssembler() {
		
	}
	public List<SummaryBeanModel> getData(){
		List<SummaryBeanModel> list = new ArrayList<SummaryBeanModel>();
		SummaryBeanModel summaryTotals = new SummaryBeanModel();
		try {
			Context ctx = new InitialContext();
			ds = (DataSource)ctx.lookup("java:comp/env/jdbc/Gudmap_jdbcResource");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		summaryTotals.SetIshTotal(getISHTotals());
		summaryTotals.SetWishTotal(getWISHTotals());
		summaryTotals.SetSishTotal(getSISHTotals());
		summaryTotals.SetOptTotal(getOPTTotals());
		summaryTotals.SetIhcTotal(getIHCTotals());
		summaryTotals.SetTgTotal(getTGTotals());
		summaryTotals.SetMicroarrayTotal(getMicroarrayTotals());
		summaryTotals.SetSequenceTotal(getSequenceTotals());
		summaryTotals.SetIshGeneTotal(getISHGeneTotals());
		summaryTotals.SetWishGeneTotal(getWISHGeneTotals());
		summaryTotals.SetSishGeneTotal(getSISHGeneTotals());
		summaryTotals.SetOptGeneTotal(getOPTGeneTotals());
		summaryTotals.SetIhcGeneTotal(getIHCGeneTotals());
		summaryTotals.SetTgGeneTotal(getTGGeneTotals());
		list.add(summaryTotals);
		return list;
	}

	private int getISHTotals(){
		String queryString=QueryTotals.ReturnQuery("ISH_TOTAL");
		int counter=0;
		try
		{
		con = ds.getConnection();
		ps = con.prepareStatement(queryString); 
		result =  ps.executeQuery();
		result.next();
		counter=Integer.parseInt(result.getString(1));
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		return counter;
		
	}
	
	private int getWISHTotals(){
		String queryString=QueryTotals.ReturnQuery("WISH_TOTAL");
		int counter=0;
		try
		{
		con = ds.getConnection();
		ps = con.prepareStatement(queryString); 
		result =  ps.executeQuery();
		result.next();
		counter=Integer.parseInt(result.getString(1));
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		return counter;		
	}
	
	private int getSISHTotals(){
		String queryString=QueryTotals.ReturnQuery("SISH_TOTAL");
		int counter=0;
		try
		{
		con = ds.getConnection();
		ps = con.prepareStatement(queryString); 
		result =  ps.executeQuery();
		result.next();
		counter=Integer.parseInt(result.getString(1));
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		return counter;	
	}
	
	private int getOPTTotals(){
		String queryString=QueryTotals.ReturnQuery("OPT_TOTAL");
		int counter=0;
		try
		{
		con = ds.getConnection();
		ps = con.prepareStatement(queryString); 
		result =  ps.executeQuery();
		result.next();
		counter=Integer.parseInt(result.getString(1));
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		return counter;	
	}
	
	private int getIHCTotals(){
		String queryString=QueryTotals.ReturnQuery("IHC_TOTAL");
		int counter=0;
		try
		{
		con = ds.getConnection();
		ps = con.prepareStatement(queryString); 
		result =  ps.executeQuery();
		result.next();
		counter=Integer.parseInt(result.getString(1));
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		return counter;		
	}
	
	private int getTGTotals(){
		String queryString=QueryTotals.ReturnQuery("TG_TOTAL");
		int counter=0;
		try
		{
		con = ds.getConnection();
		ps = con.prepareStatement(queryString); 
		result =  ps.executeQuery();
		result.next();
		counter=Integer.parseInt(result.getString(1));
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		return counter;	
	}
	
	private int getMicroarrayTotals(){
		String queryString=QueryTotals.ReturnQuery("MICROARRAY_TOTAL");
		int counter=0;
		try
		{
		con = ds.getConnection();
		ps = con.prepareStatement(queryString); 
		result =  ps.executeQuery();
		result.next();
		counter=Integer.parseInt(result.getString(1));
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		return counter;	
	}
	
	private int getSequenceTotals(){
		String queryString=QueryTotals.ReturnQuery("SEQUENCE_TOTAL");
		int counter=0;
		try
		{
		con = ds.getConnection();
		ps = con.prepareStatement(queryString); 
		result =  ps.executeQuery();
		result.next();
		counter=Integer.parseInt(result.getString(1));
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		return counter;	
	}
	//
	private int getISHGeneTotals(){
		String queryString=QueryTotals.ReturnQuery("ISH_TOTAL_GENES");
		int counter=0;
		try
		{
		con = ds.getConnection();
		ps = con.prepareStatement(queryString); 
		result =  ps.executeQuery();
		result.next();
		counter=Integer.parseInt(result.getString(1));
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		return counter;
		
	}
	
	private int getWISHGeneTotals(){
		String queryString=QueryTotals.ReturnQuery("WISH_TOTAL_GENES");
		int counter=0;
		try
		{
		con = ds.getConnection();
		ps = con.prepareStatement(queryString); 
		result =  ps.executeQuery();
		result.next();
		counter=Integer.parseInt(result.getString(1));
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		return counter;		
	}
	
	private int getSISHGeneTotals(){
		String queryString=QueryTotals.ReturnQuery("SISH_TOTAL_GENES");
		int counter=0;
		try
		{
		con = ds.getConnection();
		ps = con.prepareStatement(queryString); 
		result =  ps.executeQuery();
		result.next();
		counter=Integer.parseInt(result.getString(1));
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		return counter;	
	}
	
	private int getOPTGeneTotals(){
		String queryString=QueryTotals.ReturnQuery("OPT_TOTAL_GENES");
		int counter=0;
		try
		{
		con = ds.getConnection();
		ps = con.prepareStatement(queryString); 
		result =  ps.executeQuery();
		result.next();
		counter=Integer.parseInt(result.getString(1));
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		return counter;	
	}
	
	private int getIHCGeneTotals(){
		String queryString=QueryTotals.ReturnQuery("IHC_TOTAL_GENES");
		int counter=0;
		try
		{
		con = ds.getConnection();
		ps = con.prepareStatement(queryString); 
		result =  ps.executeQuery();
		result.next();
		counter=Integer.parseInt(result.getString(1));
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		return counter;		
	}
	
	private int getTGGeneTotals(){
		String queryString=QueryTotals.ReturnQuery("TG_TOTAL_GENES");
		int counter=0;
		try
		{
		con = ds.getConnection();
		ps = con.prepareStatement(queryString); 
		result =  ps.executeQuery();
		result.next();
		counter=Integer.parseInt(result.getString(1));
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		return counter;	
	}
	
	
}
