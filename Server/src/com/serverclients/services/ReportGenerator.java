
package com.serverclients.services;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.serverclients.pojos.Client;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReportGenerator {
    private ArrayList<Client> clients = new ArrayList<Client>();
    private String date;
    private String fileName;
    private String filePath;
    
    public ReportGenerator(ArrayList<Client> clients){
        this.clients = clients;
        this.filePath = "pdf";
    }
    
    public String getFilePath(){
        return this.filePath;
    }
    
    public boolean generateReport(){
        try{
            this.date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            File folder = new File("pdf");
            if (!folder.exists()) {
                    folder.mkdir();
            }
            int num = (int) (Math.random() * 999999 + 1);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String uniqueKey = formatter.format(new Date()) + num + "_";
            this.fileName = uniqueKey +(int)(Math.random()*999999+1) +".pdf";

            File file = new File(this.filePath + File.separator + this.fileName);
            this.filePath = this.filePath + File.separator + this.fileName;
            file.createNewFile();

            Document document = new Document(PageSize.A4, 15f, 15f, 15f, 15f);
            FileOutputStream fout = new FileOutputStream(file);
            PdfWriter.getInstance(document, fout);
            document.open();
            Font font1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
            Font font2 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
            Font font3 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12);

            Paragraph clientInfo = new Paragraph("Clients Informations", font1);

            Paragraph reportDate = new Paragraph("Report Date : " +this.date , font2);


            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);

            PdfPCell cell1 = new PdfPCell(clientInfo);
            cell1.setColspan(7);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setPadding(5);
            cell1.disableBorderSide(2);
            cell1.setVerticalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell2 = new PdfPCell(reportDate);
            cell2.setColspan(7);
            cell2.disableBorderSide(1);
            cell2.setPadding(5);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setVerticalAlignment(Element.ALIGN_CENTER);

            table.addCell(cell1);
            table.addCell(cell2);


            PdfPCell cell;
            cell = new PdfPCell(new Paragraph("SN", font2));
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Ip Address", font2));
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Customer Name", font2));
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            
            cell = new PdfPCell(new Paragraph("OS", font2));
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("ID Number", font2));
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            
            cell = new PdfPCell(new Paragraph("Connected Time", font2));
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Disconnected Time", font2));
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            
            for (int i = 0; i < this.clients.size(); i++) {
                //Data Entry starts
                cell = new PdfPCell(new Paragraph(Integer.toString(i + 1), font3));
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(this.clients.get(i).getIpAddress(), font3));
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(this.clients.get(i).getCustomerUserName(), font3));
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                
                cell = new PdfPCell(new Paragraph(this.clients.get(i).getOperatingSystem(), font3));
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(this.clients.get(i).getUserUniqueId(), font3));
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                
                cell = new PdfPCell(new Paragraph(this.clients.get(i).getConnectedTime(), font3));
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(this.clients.get(i).getEndTime(), font3));
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
            document.add(table);
            document.close();
            return true;
        }catch(Exception er){
            return false;
        }
    }
}
