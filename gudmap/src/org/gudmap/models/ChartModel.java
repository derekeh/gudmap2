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
	
	private double wish_tissue = 0.0;
	private double sish_tissue = 0.0;
	private double opt_tissue = 0.0;
	private double ihc_tissue = 0.0;
	private double tg_tissue = 0.0;
	private double microarray_tissue = 0.0;
	private double sequence_tissue = 0.0;
	
	private double met_percent = 0.0;
	private double lut_percent = 0.0;
	private double frs_percent = 0.0;
	private double mrs_percent = 0.0;
	private double ers_percent = 0.0;
	
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
	
	private int tot_wish_tissue_entries = 0;
	private int tot_sish_tissue_entries = 0;
	private int tot_opt_tissue_entries = 0;
	private int tot_ihc_tissue_entries = 0;
	private int tot_tg_tissue_entries = 0;
	private int tot_microarray_tissue_entries = 0;
	private int tot_sequence_tissue_entries = 0;
	
	private int tot_wish = 0;
	private int tot_sish = 0;
	private int tot_ihc = 0;
	private int tot_tg = 0;
	private int tot_opt = 0;
	private int tot_microarray = 0;
	private int tot_sequence = 0;
	
	private int tot_age_1 = 0;
	private int tot_age_2 = 0;
	private int tot_age_3 = 0;
	private int tot_age_4 = 0;
	private int tot_age_5 = 0;
	
	
	
	
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
	
	public void setWish_tissue(double wish_tissue){
		this.wish_tissue = wish_tissue;
	}
	public double getWish_tissue(){
		return wish_tissue;
	}
	
	public void setSish_tissue(double sish_tissue){
		this.sish_tissue = sish_tissue;
	}
	public double getSish_tissue(){
		return sish_tissue;
	}
	
	public void setOpt_tissue(double opt_tissue){
		this.opt_tissue = opt_tissue;
	}
	public double getOpt_tissue(){
		return opt_tissue;
	}
	
	public void setTg_tissue(double tg_tissue){
		this.tg_tissue = tg_tissue;
	}
	public double getTg_tissue(){
		return tg_tissue;
	}
	
	public void setIhc_tissue(double ihc_tissue){
		this.ihc_tissue = ihc_tissue;
	}
	public double getIhc_tissue(){
		return ihc_tissue;
	}
	
	public void setMicroarray_tissue(double microarray_tissue){
		this.microarray_tissue = microarray_tissue;
	}
	public double getMicroarray_tissue(){
		return microarray_tissue;
	}
	
	public void setSequence_tissue(double sequence_tissue){
		this.sequence_tissue = sequence_tissue;
	}
	public double getSequence_tissue(){
		return sequence_tissue;
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
	
	/////
	public void setTot_wish_tissue_entries(int tot_wish_tissue_entries) {
		this.tot_wish_tissue_entries = tot_wish_tissue_entries;
	}
	public int getTot_wish_tissue_entries() {
		return tot_wish_tissue_entries;
	}
	
	public void setTot_sish_tissue_entries(int tot_sish_tissue_entries) {
		this.tot_sish_tissue_entries = tot_sish_tissue_entries;
	}
	public int getTot_sish_tissue_entries() {
		return tot_sish_tissue_entries;
	}
	
	public void setTot_opt_tissue_entries(int tot_opt_tissue_entries) {
		this.tot_opt_tissue_entries = tot_opt_tissue_entries;
	}
	public int getTot_opt_tissue_entries() {
		return tot_opt_tissue_entries;
	}
	
	public void setTot_ihc_tissue_entries(int tot_ihc_tissue_entries) {
		this.tot_ihc_tissue_entries = tot_ihc_tissue_entries;
	}
	public int getTot_ihc_tissue_entries() {
		return tot_ihc_tissue_entries;
	}
	
	public void setTot_tg_tissue_entries(int tot_tg_tissue_entries) {
		this.tot_tg_tissue_entries = tot_tg_tissue_entries;
	}
	public int getTot_tg_tissue_entries() {
		return tot_tg_tissue_entries;
	}
	public void setTot_microarray_tissue_entries(int tot_microarray_tissue_entries) {
		this.tot_microarray_tissue_entries = tot_microarray_tissue_entries;
	}
	public int getTot_microarray_tissue_entries() {
		return tot_microarray_tissue_entries;
	}
	
	public void setTot_sequence_tissue_entries(int tot_sequence_tissue_entries) {
		this.tot_sequence_tissue_entries = tot_sequence_tissue_entries;
	}
	public int getTot_sequence_tissue_entries() {
		return tot_sequence_tissue_entries;
	}
	
	////
	public void setMet_percent(double met_percent){
		this.met_percent = met_percent;
	}
	public double getMet_percent(){
		return met_percent;
	}
	public void setLut_percent(double lut_percent){
		this.lut_percent = lut_percent;
	}
	public double getLut_percent(){
		return lut_percent;
	}
	
	public void setFrs_percent(double frs_percent){
		this.frs_percent = frs_percent;
	}
	public double getFrs_percent(){
		return frs_percent;
	}
	public void setMrs_percent(double mrs_percent){
		this.mrs_percent = mrs_percent;
	}
	public double getMrs_percent(){
		return mrs_percent;
	}
	
	public void setErs_percent(double ers_percent){
		this.ers_percent = ers_percent;
	}
	public double getErs_percent(){
		return ers_percent;
	}
	
	/////
	
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
	/////
	public void setTot_wish(int tot_wish){
		this.tot_wish = tot_wish;
	}
	public int getTot_wish() {
		return tot_wish;
	}
	public void setTot_sish(int tot_sish){
		this.tot_sish = tot_sish;
	}
	public int getTot_sish() {
		return tot_sish;
	}
	public void setTot_opt(int tot_opt){
		this.tot_opt = tot_opt;
	}
	public int getTot_opt() {
		return tot_opt;
	}
	public void setTot_ihc(int tot_ihc){
		this.tot_ihc = tot_ihc;
	}
	public int getTot_ihc() {
		return tot_ihc;
	}
	public void setTot_tg(int tot_tg){
		this.tot_tg = tot_tg;
	}
	public int getTot_tg() {
		return tot_tg;
	}
	public void setTot_microarray(int tot_microarray){
		this.tot_microarray = tot_microarray;
	}
	public int getTot_microarray() {
		return tot_microarray;
	}
	public void setTot_sequence(int tot_sequence){
		this.tot_sequence = tot_sequence;
	}
	public int getTot_sequence() {
		return tot_sequence;
	}
	///
	public void setTot_age_1 (int tot_age_1){
		this.tot_age_1 = tot_age_1;
	}
	public int getTot_age_1() {
		return tot_age_1;
	}
	public void setTot_age_2 (int tot_age_2){
		this.tot_age_2 = tot_age_2;
	}
	public int getTot_age_2() {
		return tot_age_2;
	}
	public void setTot_age_3 (int tot_age_3){
		this.tot_age_3 = tot_age_3;
	}
	public int getTot_age_3() {
		return tot_age_3;
	}
	public void setTot_age_4 (int tot_age_4){
		this.tot_age_4 = tot_age_4;
	}
	public int getTot_age_4() {
		return tot_age_4;
	}
	public void setTot_age_5 (int tot_age_5){
		this.tot_age_5 = tot_age_5;
	}
	public int getTot_age_5() {
		return tot_age_5;
	}
}
	
	