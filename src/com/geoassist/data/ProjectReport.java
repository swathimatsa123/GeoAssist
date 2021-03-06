package com.geoassist.data;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ProjectReport {
	  private static String  fileBase = Environment.getExternalStorageDirectory() + "/";
	  private static Font font18B = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD);
	  private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
	  private static Font font16B  = new Font(Font.FontFamily.TIMES_ROMAN, 16,Font.BOLD);
	  private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
	  private static Font font14 = new Font(Font.FontFamily.TIMES_ROMAN, 14,Font.NORMAL);
	  private User usr = User.getInstance();
	  private String fileName;
	  private static String tab = "          ";
	  public String getFileName() {
		return fileName;
	  }

	  public void save(WorkingProject proj) {
		  try {
			  Document document = new Document();
			  SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy",Locale.US);
			  String dateStr = df.format(Calendar.getInstance().getTime());
			  fileName = fileBase+"Report"+usr.userName+dateStr+".pdf";
		      PdfWriter.getInstance(document, new FileOutputStream(fileName));
		      document.open();
		      Log.e("Opened" , fileName);
			  Log.e("USER Name In Project", usr.userName);

		      addMetaData(document, proj);
		      Log.e("MetaData" , "Done");
		      addTitlePage(document, proj);
		      addContent(document, proj);
		      document.close();
		    } catch (Exception e) {
		    	Log.e("WRITER", "Exception " + fileName);
		    	e.printStackTrace();
		    }
	  }

	  private void addMetaData(Document document, WorkingProject proj) {
		  document.addTitle("Geology Field Reporrt");
		  document.addSubject("Using iText");
		  document.addKeywords("Java, PDF, iText");
		  document.addAuthor(usr.userName);
		  document.addCreator(usr.userName);
	  }

	  private void addTitlePage(Document document, WorkingProject proj)throws DocumentException  {
		  Paragraph preface = new Paragraph();
		  addEmptyLine(preface, 1);
		  preface.add(new Paragraph("Field Report for Geology Department ", font18B));
		  addEmptyLine(preface, 1);
		  preface.add(new Paragraph("Report generated by: " + 
				  					usr.userName+ ", " 
	    						+ new Date(), 
	    						smallBold));
		  addEmptyLine(preface, 3);
		  SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy",Locale.US);
		  String dateStr = df.format(Calendar.getInstance().getTime());
		  
		  preface.add(new Paragraph("This is field data from  " + usr.userName + " Recorded on :" + dateStr, 
	    						smallBold));
		  addEmptyLine(preface, 8);
//		  preface.add(new Paragraph("This document is not ready yet. It is an experimentation to verify pdf generation.",
//	    							redFont));
		  document.add(preface);
		  document.newPage();
	  }

	  private	 static void addContent(Document document , WorkingProject proj) throws DocumentException {
	      Paragraph paragraph = new Paragraph();
	      paragraph.add(new Phrase("Details from Sites",font18B));		  
 		  Chapter chapter = new Chapter(paragraph, 1);
	      for (int i= 0; i< proj.sites.size(); i++) {
			  addSiteInfo(document, chapter , proj,i);
	      }
	      document.add(chapter);
		  Log.e("ADDING ", "Done");  
	  }

	  private static void addEmptyLine(Paragraph paragraph, int number) {
		    for (int i = 0; i < number; i++) {
		      paragraph.add(new Paragraph(" "));
		    }
		  }
	  
	  private static void addSiteInfo(Document document, Chapter chapter , WorkingProject proj, int index) throws DocumentException{
		  Log.e("ADDING ", "Sites");
		  Section section = chapter.addSection(new Paragraph("Site "+ String.valueOf(index+1), font18B), index+1);
	      Paragraph info = new Paragraph();
		  addEmptyLine(info, 1);
		  Site site = proj.sites.get(index);

		  String rockUnitStr =  tab +"Rock Unit   : " + site.rockUnit 
				  					+"\n" + tab + "Rock Type  :  "+ site.rockType;
		  info.add(new Paragraph(rockUnitStr, font16B));
		  addEmptyLine(info, 1);
		  String mineralHdr=  "\n \n \n" + tab + "Minerals Data";
		  info.add(new Paragraph(mineralHdr, font16B));
		  addEmptyLine(info, 1);
 
		  section.add(info);
		  section.add(makeMineralTable(proj, index));
		  addEmptyLine(info, 1);
		  String contactsStr =  " \n" +tab +" Contacts Data \n";
		  section.add(new Paragraph(contactsStr, font16B));
		  section.add(makeContactsTable(proj, index));

		  String HdrStr =  " \n" +tab +" Fold Data \n";
		  section.add(new Paragraph(HdrStr, font16B));
		  section.add(getFoldInfo(proj, index));

		  HdrStr =  " \n" +tab +" Fault Data \n";
		  section.add(new Paragraph(HdrStr, font16B));
		  section.add(getFaultInfo(proj, index));

		  HdrStr =  " \n" +tab +" Joint Data \n";
		  section.add(new Paragraph(HdrStr, font16B));
		  section.add(getJointInfo(proj, index));


		  HdrStr =  " \n" +tab +" Vein Data \n";
		  section.add(new Paragraph(HdrStr, font16B));
		  section.add(getVeinInfo(proj, index));
	  }

	  private static PdfPTable makeMineralTable(WorkingProject proj, int index) {
		  PdfPTable table = new PdfPTable(7); // 3 columns.

          PdfPCell hdr1 = new PdfPCell(new Paragraph("Mineral Name", font16B));
          PdfPCell hdr2 = new PdfPCell(new Paragraph("Composition ", font16B));
          PdfPCell hdr3 = new PdfPCell(new Paragraph("Min Grain Size", font16B));
          PdfPCell hdr4 = new PdfPCell(new Paragraph("Max Grain Size", font16B));
          PdfPCell hdr5 = new PdfPCell(new Paragraph("Clevage", font16B));
          PdfPCell hdr6 = new PdfPCell(new Paragraph("Grain Form", font16B));
          PdfPCell hdr7 = new PdfPCell(new Paragraph("Grain Shape", font16B));

          table.addCell(hdr1);
          table.addCell(hdr2);
          table.addCell(hdr3);
          table.addCell(hdr4);
          table.addCell(hdr5);
          table.addCell(hdr6);
          table.addCell(hdr7);
          Site site = proj.sites.get(index);
          for (int i = 0; i<site.minerals.size(); i++ ){
        	  Mineral mineral = site.minerals.get(i);
        	  PdfPCell c1= new PdfPCell(new Paragraph(mineral.mineralName, font14));
        	  PdfPCell c2= new PdfPCell(new Paragraph(String.valueOf(mineral.composition), font14));
        	  PdfPCell c3= new PdfPCell(new Paragraph(String.valueOf(mineral.minGrainSize), font14));
        	  PdfPCell c4= new PdfPCell(new Paragraph(String.valueOf(mineral.maxGrainSize), font14));
        	  PdfPCell c5= new PdfPCell(new Paragraph(String.valueOf(mineral.mineralCleavege), font14));
        	  PdfPCell c6= new PdfPCell(new Paragraph(mineral.grainForm, font14));
        	  PdfPCell c7= new PdfPCell(new Paragraph(mineral.grainShape, font14));
        	  table.addCell(c1);
        	  table.addCell(c2);
        	  table.addCell(c3);
        	  table.addCell(c4);
        	  table.addCell(c5);
        	  table.addCell(c6);
        	  table.addCell(c7);
        	  }
          return table;
	  }

	  private static PdfPTable makeContactsTable(WorkingProject proj, int index){
          PdfPTable table = new PdfPTable(3); 
          PdfPCell hdr1 = new PdfPCell(new Paragraph("Contact", font16B));
          PdfPCell hdr2 = new PdfPCell(new Paragraph("Type", font16B));
          PdfPCell hdr3 = new PdfPCell(new Paragraph("Boundary", font16B));
          PdfPCell hdr4 = new PdfPCell(new Paragraph("Notes", font16B));
          table.addCell(hdr1);
          table.addCell(hdr2);
          table.addCell(hdr3);
          table.addCell(hdr4);
          Site site = proj.sites.get(index);
          for (int i = 0; i<site.contacts.size(); i++ ){
        	  Contact contact = site.contacts.get(i);
        	  PdfPCell c1= new PdfPCell(new Paragraph(contact.contact1Name, font14));
        	  PdfPCell c2= new PdfPCell(new Paragraph(contact.contactType, font14));
        	  PdfPCell c3= new PdfPCell(new Paragraph(contact.boundary, font14));
        	  table.addCell(c1);
        	  table.addCell(c2);
        	  table.addCell(c3);
        	  }
          return table;
	  }
	  private static Paragraph getFoldInfo(WorkingProject proj, int index){
		  Site site =  proj.sites.get(index);
		  String foldStr =  tab +" Fold Type     :  " + site.foldType
				  	+"\n" + tab + "Tighness      :  "+ site.foldTighness
				  	+"\n" + tab + "Hinge         :  "+ site.foldHinge
				  	+"\n" + tab + "Limbs         :  "+ site.foldLimbs
				  	+"\n" + tab + "Lambda        :  "+ site.foldLambda+ " " +site.foldLambdaUnits
				  	+"\n" + tab + "Amplitude     :  "+ site.foldAmplitude+ " " +site.foldAmplitudeUnits 
				  	+"\n" + tab + "2nd Order     :  "+ site.fold2nOrder
				  	+"\n" + tab + "Symmetry      :  "+ site.foldSymmetry
				  	+"\n" + tab + "Ramsays Class :  "+ site.foldRamsaysCls;
		  
		  return (new Paragraph(foldStr, font16B));
	  }

	  private static Paragraph getFaultInfo(WorkingProject proj, int index){
		  Site site =  proj.sites.get(index);
		  String writeStr =  tab +" Fault Movement :  " + site.faultMovement
				  	+"\n" + tab + "Fault Trend    :  "+ site.faultTrend
				  	+"\n" + tab + "Separation     :  "+ site.faultSeparation
				  	+"\n" + tab + "Marker Offset  :  "+ site.faultOffset   
				  	+"\n" + tab + "Piercing Point :  "+ site.faultPiercingPt  
				  	+"\n" + tab + "Net Slip       :  "+ site.faultNetSlip
				  	+"\n" + tab + "Mineralization :  "+ site.faultMineralization;
		  return (new Paragraph(writeStr, font16B));
	  }

	  private static Paragraph getJointInfo(WorkingProject proj, int index){
		  Site site =  proj.sites.get(index);
		  String writeStr =  tab +" Joint Strike   :  " + site.jointStrike
				  	+"\n" + tab + "Joint Dip      :  "+ site.jointDip
				  	+"\n" + tab + "Joint Spacing  :  "+ site.jointSpacing
				  	+"\n" + tab + "Bedding Type   :  "+ site.jointBedding 
				  	+"\n" + tab + "Fold Type      :  "+ site.jointFoldType;
		  return (new Paragraph(writeStr, font16B));
	  }


	  private static Paragraph getVeinInfo(WorkingProject proj, int index){
		  Site site =  proj.sites.get(index);
		  String writeStr =  tab + "Vein Strike   :  " + site.veinStrike
				  	+"\n" + tab +  "Vein Dip      :  "+ site.veinDip
				  	+"\n" + tab + "Joint Spacing  :  "+ site.veinZoneWidth
				  	+"\n" + tab + "Bedding Type   :  "+ site.veinComposition;
		  return (new Paragraph(writeStr, font16B));
	  }	  
}
