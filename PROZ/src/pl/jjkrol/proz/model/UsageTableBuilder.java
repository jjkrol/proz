package pl.jjkrol.proz.model;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
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
	public void initializeListeners(String filename) throws DocumentException,
			FileNotFoundException {
		writer =
				PdfWriter.getInstance(document, new FileOutputStream(filename));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void buildBody(final Map<BillableService, Float> results,
			final Map<BillableService, Float> administrativeResults)
			throws DocumentException, IOException {
		document.open();
		PdfPTable t = new PdfPTable(7);
		t.setWidthPercentage(100f);
		t.setWidths(new int[] { 3, 1, 1, 1, 1, 1, 1 });
		BaseFont bf =
				BaseFont.createFont("c:/windows/fonts/arial.ttf",
						BaseFont.CP1250, BaseFont.EMBEDDED);
		Font font = new Font(bf, 12);
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
		cell = new PdfPCell(new Phrase("poczatek"));
		t.addCell(cell);
		cell = new PdfPCell(new Phrase("koniec"));
		t.addCell(cell);
		t.addCell(new Phrase("stawka", font));
		t.addCell(new Phrase("op³ata", font));
		t.addCell(new Phrase("wsp.", font));
		t.addCell(new Phrase("op³ata", font));
		t.addCell("Centralne Ogrzewanie");
		t.addCell("");
		t.addCell("");
		t.addCell("");
		t.addCell(new DecimalFormat("#.##").format(results
				.get(BillableService.CO)));
		t.addCell("");
		t.addCell(new DecimalFormat("#.##").format(administrativeResults
				.get(BillableService.CO)));
		t.addCell(new Phrase("Woda zimna i ciep³a", font));
		t.addCell("");
		t.addCell("");
		t.addCell("");
		t.addCell(new DecimalFormat("#.##").format(results
				.get(BillableService.WODA)));
		t.addCell("");
		t.addCell(new DecimalFormat("#.##").format(administrativeResults
				.get(BillableService.WODA)));
		t.addCell(new Phrase("Podgrzanie ciep³ej wody", font));
		t.addCell("");
		t.addCell("");
		t.addCell("");
		t.addCell(new DecimalFormat("#.##").format(results
				.get(BillableService.PODGRZANIE)));
		t.addCell("");
		t.addCell("");
		t.addCell(new Phrase("Wywóz œcieków", font));
		t.addCell("");
		t.addCell("");
		t.addCell("");
		t.addCell(new DecimalFormat("#.##").format(results
				.get(BillableService.SCIEKI)));
		t.addCell("");
		t.addCell("");
		t.addCell("Gaz");
		t.addCell("");
		t.addCell("");
		t.addCell("");
		t.addCell(new DecimalFormat("#.##").format(results
				.get(BillableService.GAZ)));
		t.addCell("");
		t.addCell("");
		t.addCell("Energia elektryczna");
		t.addCell("");
		t.addCell("");
		t.addCell("");
		t.addCell(new DecimalFormat("#.##").format(results
				.get(BillableService.EE)));
		t.addCell("");
		t.addCell(new DecimalFormat("#.##").format(administrativeResults
				.get(BillableService.EE)));
		t.addCell(new Phrase("Wywóz œmieci", font));
		t.addCell("");
		t.addCell("");
		t.addCell("");
		t.addCell(new DecimalFormat("#.##").format(results
				.get(BillableService.SMIECI)));
		;
		t.addCell("");
		t.addCell("");
		t.addCell("Internet");
		t.addCell("");
		t.addCell("");
		t.addCell("");
		t.addCell(new DecimalFormat("#.##").format(results
				.get(BillableService.INTERNET)));
		t.addCell("");
		t.addCell("");
		t.addCell(new Phrase("Nale¿na suma op³at za media", font));
		cell = new PdfPCell();
		cell.setColspan(3);
		t.addCell(cell);
		t.addCell("suma");
		t.addCell("");
		t.addCell("suma");
		document.add(t);
		Paragraph par = new Paragraph("Pobrana op³ata: op³ata", font);
		document.add(par);
		par = new Paragraph("Nale¿noœæ: op³ata", font);
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
