package pl.jjkrol.proz.controller;


import java.text.SimpleDateFormat;

import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.mockups.ResultMockup;
import pl.jjkrol.proz.model.DocumentBuilder;
import pl.jjkrol.proz.model.DocumentDirector;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.model.UsageTableBuilder;
import pl.jjkrol.proz.view.NoSuchTabException;
import pl.jjkrol.proz.view.PaymentsTab;
import pl.jjkrol.proz.view.View;
import pl.jjkrol.proz.events.payments.GenerateUsageTableEvent;

	/**
	 * generates a pdf with a usage table
	 * and displays it
	 */
	class GenerateUsageTableStrategy extends PROZStrategy {
		GenerateUsageTableStrategy(View view, Model model) {
			super(view, model);
		}

		public void execute(final PROZEvent e) {
			final ResultMockup result =
					((GenerateUsageTableEvent) e).result;
			
			String name = result.locum.getName();
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			String dateFrom = df.format(result.from.getTime());
			String dateTo = df.format(result.to.getTime());
			String filepath = "tabele/"+dateFrom+"_"+dateTo+"/"+name+".pdf";
			
			DocumentBuilder builder = new UsageTableBuilder();
			DocumentDirector director = new DocumentDirector(builder);
			director.buildDocument(filepath, result);
			PaymentsTab c;
			try {
				c = (PaymentsTab) view.getSpecificView(PaymentsTab.class);
					c.displayUsageTable(filepath);
			} catch (NoSuchTabException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

				}
	}
