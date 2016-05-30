package org.gudmap.models;

public class ChartModel {
	
	private double wish_percent=0.0;
	private double wish_gene_percent=0.0;
	private double sish_percent=0.0;
	private double sish_gene_percent=0.0;
	private double opt_percent=0.0;
	private double opt_gene_percent=0.0;
	private double ihc_percent=0.0;
	private double ihc_gene_percent=0.0;
	private double tg_percent=0.0;
	private double tg_gene_percent=0.0;
	private double microarray_percent=0.0;
	private double sequence_percent=0.0;
	private int total_entries=0;
	private int total_genes=0;
	
	private double wish_lab = 0.0;
	private double sish_lab = 0.0;
	private double opt_lab = 0.0;
	private double ihc_lab = 0.0;
	private double tg_lab = 0.0;
	private double microarray_lab = 0.0;
	private double sequence_lab = 0.0;
	
	private double wish_age = 0.0;
	private double sish_age = 0.0;
	private double opt_age = 0.0;
	private double ihc_age = 0.0;
	private double tg_age = 0.0;
	private double microarray_age = 0.0;
	private double sequence_age = 0.0;
	
	private double age_1_percent = 0.0;
	private double age_2_percent = 0.0;
	private double age_3_percent = 0.0;
	private double age_4_percent = 0.0;
	private double age_5_percent = 0.0;
	
	private int tot_met_entries = 0;
	private int tot_met_genes = 0;
	private int tot_lut_entries = 0;
	private int tot_lut_genes = 0;
	private int tot_ers_entries = 0;
	private int tot_ers_genes = 0;
	private int tot_frs_entries = 0;
	private int tot_frs_genes = 0;
	private int tot_mrs_entries = 0;
	private int tot_mrs_genes = 0;
	
	
	public ChartModel(){
		
	}
	
	public void setWish_percent(double wish_percent){
		this.wish_percent = wish_percent;
	}
	public double getWish_percent(){
		return wish_percent;
	}
	public void setWish_gene_percent(double wish_gene_percent){
		this.wish_gene_percent = wish_gene_percent;
	}
	public double getWish_gene_percent(){
		return wish_gene_percent;
	}
	
	public void setSish_percent(double sish_percent){
		this.sish_percent = sish_percent;
	}
	public double getSish_percent(){
		return sish_percent;
	}
	public void setSish_gene_percent(double sish_gene_percent){
		this.sish_gene_percent = sish_gene_percent;
	}
	public double getSish_gene_percent(){
		return sish_gene_percent;
	}
	
	public void setOpt_percent(double opt_percent){
		this.opt_percent = opt_percent;
	}
	public double getOpt_percent(){
		return opt_percent;
	}
	public void setOpt_gene_percent(double opt_gene_percent){
		this.opt_gene_percent = opt_gene_percent;
	}
	public double getOpt_gene_percent(){
		return opt_gene_percent;
	}
	
	public void setIhc_percent(double ihc_percent){
		this.ihc_percent = ihc_percent;
	}
	public double getIhc_percent(){
		return ihc_percent;
	}
	public void setIhc_gene_percent(double ihc_gene_percent){
		this.ihc_gene_percent = ihc_gene_percent;
	}
	public double getIhc_gene_percent(){
		return ihc_gene_percent;
	}
	
	public void setTg_percent(double tg_percent){
		this.tg_percent = tg_percent;
	}
	public double getTg_percent(){
		return tg_percent;
	}
	public void setTg_gene_percent(double tg_gene_percent){
		this.tg_gene_percent = tg_gene_percent;
	}
	public double getTg_gene_percent(){
		return tg_gene_percent;
	}
	
	public void setMicroarray_percent(double microarray_percent){
		this.microarray_percent = microarray_percent;
	}
	public double getMicroarray_percent(){
		return microarray_percent;
	}
	
	public void setSequence_percent(double sequence_percent){
		this.sequence_percent = sequence_percent;
	}
	public double getSequence_percent(){
		return sequence_percent;
	}
		
	public void setTotal_entries(int total_entries){
		this.total_entries = total_entries;
	}
	public int getTotal_entries(){
		return total_entries;
	}
	
	public void setTotal_genes(int total_genes){
		this.total_genes = total_genes;
	}
	public int getTotal_genes(){
		return total_genes;
	}
	
	public void setWish_lab(double wish_lab){
		this.wish_lab = wish_lab;
	}
	public double getWish_lab(){
		return wish_lab;
	}
	
	public void setSish_lab(double sish_lab){
		this.sish_lab = sish_lab;
	}
	public double getSish_lab(){
		return sish_lab;
	}
	
	public void setOpt_lab(double opt_lab){
		this.opt_lab = opt_lab;
	}
	public double getOpt_lab(){
		return opt_lab;
	}
	
	public void setTg_lab(double tg_lab){
		this.tg_lab = tg_lab;
	}
	public double getTg_lab(){
		return tg_lab;
	}
	
	public void setIhc_lab(double ihc_lab){
		this.ihc_lab = ihc_lab;
	}
	public double getIhc_lab(){
		return ihc_lab;
	}
	
	public void setMicroarray_lab(double microarray_lab){
		this.microarray_lab = microarray_lab;
	}
	public double getMicroarray_lab(){
		return microarray_lab;
	}
	
	public void setSequence_lab(double sequence_lab){
		this.sequence_lab = sequence_lab;
	}
	public double getSequence_lab(){
		return sequence_lab;
	}
	/////
	public void setAge_1_percent(double age_1_percent){
		this.age_1_percent = age_1_percent;
	}
	public double getAge_1_percent(){
		return age_1_percent;
	}
	public void setAge_2_percent(double age_2_percent){
		this.age_2_percent = age_2_percent;
	}
	public double getAge_2_percent(){
		return age_2_percent;
	}
	
	public void setAge_3_percent(double age_3_percent){
		this.age_3_percent = age_3_percent;
	}
	public double getAge_3_percent(){
		return age_3_percent;
	}
	public void setAge_4_percent(double age_4_percent){
		this.age_4_percent = age_4_percent;
	}
	public double getAge_4_percent(){
		return age_4_percent;
	}
	
	public void setAge_5_percent(double age_5_percent){
		this.age_5_percent = age_5_percent;
	}
	public double getAge_5_percent(){
		return age_5_percent;
	}
	////
	public void setWish_age(double wish_age){
		this.wish_age = wish_age;
	}
	public double getWish_age(){
		return wish_age;
	}
	
	public void setSish_age(double sish_age){
		this.sish_age = sish_age;
	}
	public double getSish_age(){
		return sish_age;
	}
	
	public void setOpt_age(double opt_age){
		this.opt_age = opt_age;
	}
	public double getOpt_age(){
		return opt_age;
	}
	
	public void setTg_age(double tg_age){
		this.tg_age = tg_age;
	}
	public double getTg_age(){
		return tg_age;
	}
	
	public void setIhc_age(double ihc_age){
		this.ihc_age = ihc_age;
	}
	public double getIhc_age(){
		return ihc_age;
	}
	
	public void setMicroarray_age(double microarray_age){
		this.microarray_age = microarray_age;
	}
	public double getMicroarray_age(){
		return microarray_age;
	}
	
	public void setSequence_age(double sequence_age){
		this.sequence_age = sequence_age;
	}
	public double getSequence_age(){
		return sequence_age;
	}
	
	////
	
	public void setTot_met_entries(int tot_met_entries) {
		this.tot_met_entries = tot_met_entries;
	}
	public int getTot_met_entries() {
		return tot_met_entries;
	}
	
	public void setTot_lut_entries(int tot_lut_entries) {
		this.tot_lut_entries = tot_lut_entries;
	}
	public int getTot_lut_entries() {
		return tot_lut_entries;
	}
	
	public void setTot_ers_entries(int tot_ers_entries) {
		this.tot_ers_entries = tot_ers_entries;
	}
	public int getTot_ers_entries() {
		return tot_ers_entries;
	}
	
	public void setTot_frs_entries(int tot_frs_entries) {
		this.tot_frs_entries = tot_frs_entries;
	}
	public int getTot_frs_entries() {
		return tot_frs_entries;
	}
	
	public void setTot_mrs_entries(int tot_mrs_entries) {
		this.tot_mrs_entries = tot_mrs_entries;
	}
	public int getTot_mrs_entries() {
		return tot_mrs_entries;
	}
	
	public void setTot_met_genes(int tot_met_genes) {
		this.tot_met_genes = tot_met_genes;
	}
	public int getTot_met_genes() {
		return tot_met_genes;
	}
	
	public void setTot_ers_genes(int tot_ers_genes) {
		this.tot_ers_genes = tot_ers_genes;
	}
	public int getTot_ers_genes() {
		return tot_ers_genes;
	}
	
	public void setTot_lut_genes(int tot_lut_genes) {
		this.tot_lut_genes = tot_lut_genes;
	}
	public int getTot_lut_genes() {
		return tot_lut_genes;
	}
	
	public void setTot_frs_genes(int tot_frs_genes) {
		this.tot_frs_genes = tot_frs_genes;
	}
	public int getTot_frs_genes() {
		return tot_frs_genes;
	}
	
	public void setTot_mrs_genes(int tot_mrs_genes) {
		this.tot_mrs_genes = tot_mrs_genes;
	}
	public int getTot_mrs_genes() {
		return tot_mrs_genes;
	}
	
}
	
	