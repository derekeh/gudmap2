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
import org.gudmap.queries.generic.GenericQueries;
import org.gudmap.queries.totals.QueryTotals;
import org.gudmap.globals.Globals;
/* 
 * Leaving it just as a class, the queries are only run once because it  populates the bean on that first run and the bean <SummaryBean> is session scoped
 * */
public class SummaryBeanAssembler {
	
	private DataSource ds;
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	private String focusGroup="";
	private String focusGroupWhereclause="";
	private boolean isFocusGroup;
	
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
		String queryString=(isFocusGroup)?String.format(QueryTotals.ReturnQuery("ISH_VOLATILE_TOTAL"),focusGroupWhereclause):QueryTotals.ReturnQuery("ISH_TOTAL");
		//String queryString=QueryTotals.ReturnQuery("ISH_TOTAL");
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
		String queryString=(isFocusGroup)?String.format(QueryTotals.ReturnQuery("WISH_VOLATILE_TOTAL"),focusGroupWhereclause):QueryTotals.ReturnQuery("WISH_TOTAL");
		//String queryString=QueryTotals.ReturnQuery("WISH_TOTAL");
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
		String queryString=(isFocusGroup)?String.format(QueryTotals.ReturnQuery("SISH_VOLATILE_TOTAL"),focusGroupWhereclause):QueryTotals.ReturnQuery("SISH_TOTAL");
		//String queryString=QueryTotals.ReturnQuery("SISH_TOTAL");
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
		String queryString=(isFocusGroup)?String.format(QueryTotals.ReturnQuery("OPT_VOLATILE_TOTAL"),focusGroupWhereclause):QueryTotals.ReturnQuery("OPT_TOTAL");
		//String queryString=QueryTotals.ReturnQuery("OPT_TOTAL");
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
		String queryString=(isFocusGroup)?String.format(QueryTotals.ReturnQuery("IHC_VOLATILE_TOTAL"),focusGroupWhereclause):QueryTotals.ReturnQuery("IHC_TOTAL");
		//String queryString=QueryTotals.ReturnQuery("IHC_TOTAL");
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
		String queryString=(isFocusGroup)?String.format(QueryTotals.ReturnQuery("TG_VOLATILE_TOTAL"),focusGroupWhereclause):QueryTotals.ReturnQuery("TG_TOTAL");
		//String queryString=QueryTotals.ReturnQuery("TG_TOTAL");
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
		String queryString=(isFocusGroup)?String.format(QueryTotals.ReturnQuery("MICROARRAY_VOLATILE_TOTAL"),focusGroupWhereclause):QueryTotals.ReturnQuery("MICROARRAY_TOTAL");
		//String queryString=QueryTotals.ReturnQuery("MICROARRAY_TOTAL");
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
		String queryString=(isFocusGroup)?String.format(QueryTotals.ReturnQuery("SEQUENCE_VOLATILE_TOTAL"),focusGroupWhereclause):QueryTotals.ReturnQuery("SEQUENCE_TOTAL");
		//String queryString=QueryTotals.ReturnQuery("SEQUENCE_TOTAL");
		if(isFocusGroup)
			queryString=queryString.replace("EXP_COMPONENT_ID", "IST_COMPONENT");
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
		String queryString=(isFocusGroup)?String.format(QueryTotals.ReturnQuery("ISH_VOLATILE_TOTAL_GENES"),focusGroupWhereclause):QueryTotals.ReturnQuery("ISH_TOTAL_GENES");
		//String queryString=QueryTotals.ReturnQuery("ISH_TOTAL_GENES");
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
		String queryString=(isFocusGroup)?String.format(QueryTotals.ReturnQuery("WISH_VOLATILE_TOTAL_GENES"),focusGroupWhereclause):QueryTotals.ReturnQuery("WISH_TOTAL_GENES");
		//String queryString=QueryTotals.ReturnQuery("WISH_TOTAL_GENES");
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
		String queryString=(isFocusGroup)?String.format(QueryTotals.ReturnQuery("SISH_VOLATILE_TOTAL_GENES"),focusGroupWhereclause):QueryTotals.ReturnQuery("SISH_TOTAL_GENES");
		//String queryString=QueryTotals.ReturnQuery("SISH_TOTAL_GENES");
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
		String queryString=(isFocusGroup)?String.format(QueryTotals.ReturnQuery("OPT_VOLATILE_TOTAL_GENES"),focusGroupWhereclause):QueryTotals.ReturnQuery("OPT_TOTAL_GENES");
		//String queryString=QueryTotals.ReturnQuery("OPT_TOTAL_GENES");
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
		String queryString=(isFocusGroup)?String.format(QueryTotals.ReturnQuery("IHC_VOLATILE_TOTAL_GENES"),focusGroupWhereclause):QueryTotals.ReturnQuery("IHC_TOTAL_GENES");
		//String queryString=QueryTotals.ReturnQuery("IHC_TOTAL_GENES");
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
		String queryString=(isFocusGroup)?String.format(QueryTotals.ReturnQuery("TG_VOLATILE_TOTAL_GENES"),focusGroupWhereclause):QueryTotals.ReturnQuery("TG_TOTAL_GENES");
		//String queryString=QueryTotals.ReturnQuery("TG_TOTAL_GENES");
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
	
	public void setFocusGroup(String focusGroup){
		
		if(focusGroup.equals("reset"))
		{
			this.focusGroup="";
			this.focusGroupWhereclause="";
			isFocusGroup=false;
		}
		else if(focusGroup.equals("Metanephros"))
		{
			this.focusGroupWhereclause =  GenericQueries.FOCUS_METANEPHROS;
			this.focusGroup=focusGroup;
			isFocusGroup=true;
		}
		else if(focusGroup.equals("Lower urinary tract"))
		{
			this.focusGroupWhereclause =  GenericQueries.FOCUS_URINARY;
			this.focusGroup=focusGroup;
			isFocusGroup=true;
		}
		else if(focusGroup.equals("Early reproductive system"))
		{
			this.focusGroupWhereclause =  GenericQueries.FOCUS_EARLY_REPRO;
			this.focusGroup=focusGroup;
			isFocusGroup=true;
		}
		else if(focusGroup.equals("Male reproductive system"))
		{
			this.focusGroupWhereclause =  GenericQueries.FOCUS_MALE_REPRO;
			this.focusGroup=focusGroup;
			isFocusGroup=true;
		}
		else if(focusGroup.equals("Female reproductive system"))
		{
			this.focusGroupWhereclause =  GenericQueries.FOCUS_FEMALE_REPRO;
			this.focusGroup=focusGroup;
			isFocusGroup=true;
		}
	}
	
	
}
