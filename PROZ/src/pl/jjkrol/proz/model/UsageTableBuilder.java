package pl.jjkrol.proz.model;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.util.Calendar;

/**
 * The Class UsageTableBuilder.
 */
public class UsageTableBuilder extends DocumentBuilder {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initializeDocument() {
		document = new Document(PageSize.A4, 50, 50, 50, 50);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initializeListeners(final String filename) throws DocumentException,
			FileNotFoundException {
		writer =
				PdfWriter.getInstance(document, new FileOutputStream(filename));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void buildBody(final Map<String, String> values)
			throws DocumentException, IOException {
			SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		document.open();
				BaseFont bf =
				BaseFont.createFont("c:/windows/fonts/arial.ttf",
						BaseFont.CP1250, BaseFont.EMBEDDED);
		Font font = new Font(bf, 12);
		Paragraph par = new Paragraph("Rozliczenie op³at zaliczkowych za media", new Font(bf,15));
		document.add(par);
		par = new Paragraph("Okres: "+values.get("from")+" - "+values.get("to"), new Font(bf, 13));
		document.add(par);
		par = new Paragraph("Lokal: "+values.get("locum_name"), font);
		document.add(par);
		par = new Paragraph("Najemca: "+values.get("occupant"), font);
		document.add(par);
		PdfPTable t = new PdfPTable(7);
		t.setWidthPercentage(100f);
		t.setWidths(new int[] { 3, 1, 1, 1, 1, 1, 1 });
		t.setSpacingBefore(10f);

		PdfPCell cell;
		t.getDefaultCell().setPadding(5);
		cell = new PdfPCell();
		cell.setRowspan(2);
		t.addCell(cell);
		cell = new PdfPCell(new Phrase("Okres rozliczeniowy"));
		cell.setColspan(2);
		t.addCell(cell);
		cell = new PdfPCell(new Phrase("Op³aty mieszkaniowe", font));
		cell.setColspan(2);
		t.addCell(cell);
		cell = new PdfPCell(new Phrase("Op³aty administracyjne", font));
		cell.setColspan(2);
		t.addCell(cell);
		cell = new PdfPCell(new Phrase(values.get("from")));
		t.addCell(cell);
		cell = new PdfPCell(new Phrase(values.get("to")));
		t.addCell(cell);
		t.addCell(new Phrase("stawka", font));
		t.addCell(new Phrase("op³ata", font));
		t.addCell(new Phrase("wsp.", font));
		t.addCell(new Phrase("op³ata", font));
		
		t.addCell("Centralne Ogrzewanie");
		t.addCell(values.get("mea_start_CO"));
		t.addCell(values.get("mea_end_CO"));
		t.addCell("");
		t.addCell(values.get("CO"));
		t.addCell("");
		t.addCell(values.get("adm_CO"));
		
		t.addCell(new Phrase("Woda zimna i ciep³a", font));
		t.addCell(values.get("mea_start_WODA"));
		t.addCell(values.get("mea_end_WODA"));
		t.addCell("");
		t.addCell(values.get("WODA"));
		t.addCell("");
		t.addCell(values.get("adm_WODA"));
		
		t.addCell(new Phrase("Podgrzanie ciep³ej wody", font));
		t.addCell(values.get("mea_start_PODGRZANIE"));
		t.addCell(values.get("mea_end_PODGRZANIE"));
		t.addCell("");
		t.addCell(values.get("PODGRZANIE"));
		t.addCell("");
		t.addCell("");
		
		t.addCell(new Phrase("Wywóz œcieków", font));
		t.addCell(values.get("mea_start_SCIEKI"));
		t.addCell(values.get("mea_end_SCIEKI"));
		t.addCell("");
		t.addCell(values.get("SCIECKI"));
		t.addCell("");
		t.addCell("");
		
		t.addCell("Gaz");
		t.addCell(values.get("mea_start_GAZ"));
		t.addCell(values.get("mea_end_GAZ"));
		t.addCell("");
		t.addCell(values.get("GAZ"));
		t.addCell("");
		t.addCell("");
		
		t.addCell("Energia elektryczna");
		t.addCell(values.get("mea_start_EE"));
		t.addCell(values.get("mea_end_EE"));
		t.addCell("");
		t.addCell(values.get("EE"));
		t.addCell("");
		t.addCell(values.get("adm_EE"));
		
		t.addCell(new Phrase("Wywóz œmieci", font));
		t.addCell(values.get("mea_start_SMIECI"));
		t.addCell(values.get("mea_end_SMIECI"));
		t.addCell("");
		t.addCell(values.get("SMIECI"));
		t.addCell("");
		t.addCell("");
		
		t.addCell("Internet");
		t.addCell(values.get("mea_start_INTERNET"));
		t.addCell(values.get("mea_end_INTERNET"));
		t.addCell("");
		t.addCell(values.get("INTERNET"));
		t.addCell("");
		t.addCell("");
		
		t.addCell(new Phrase("Nale¿na suma op³at za media", font));
		cell = new PdfPCell();
		cell.setColspan(3);
		t.addCell(cell);
		t.addCell(values.get("sum"));
		t.addCell("");
		t.addCell(values.get("adm_sum"));
		document.add(t);
		par = new Paragraph("Pobrana op³ata: "+values.get("advancement"), font);
		document.add(par);
		par = new Paragraph("Nale¿noœæ: "+values.get("to_pay"), font);
		document.add(par);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finalizeDocument() {
		document.close();
	}
}
