package com.danielkueffer.wator;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;

@SuppressWarnings("serial")
public class AnimalChart extends ApplicationFrame {
	
	private Planet pl;
	private TimeSeries fishSeries;
	private TimeSeries sharkSeries;
	private ChartPanel chartPanel;

	public AnimalChart(String title, Planet pl) {
		super(title);
		
		this.pl = pl;
		
		this.fishSeries = new TimeSeries("Random Data");
		this.sharkSeries = new TimeSeries("Random Data");
		
		final TimeSeriesCollection dataset = new TimeSeriesCollection();
		
		dataset.addSeries(this.fishSeries);
		dataset.addSeries(this.sharkSeries);
		
		final JFreeChart chart = createChart(dataset);
		
		chart.setBackgroundPaint(Color.white);
		
		chartPanel = new ChartPanel(chart);
		
		chartPanel.setPreferredSize(new java.awt.Dimension(450, 150));
		chartPanel.setMaximumSize(new java.awt.Dimension(450, 150));
		chartPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		chartPanel.setMouseZoomable(false);
		chartPanel.setBackground(Color.white);
	}
	
	/**
	 * Create chart
	 * @param dataset
	 * @return
	 */
	private JFreeChart createChart(final XYDataset dataset) {
		
		final JFreeChart result = ChartFactory.createTimeSeriesChart(
			"",
			"",
			"",
			dataset,
			false,
			false,
			false
		);
		
		// Background
		final XYPlot plot = result.getXYPlot();
		
		plot.setDataset(dataset);

		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinesVisible(true);
		plot.setDomainGridlinePaint(Color.lightGray);
		plot.setRangeGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.lightGray);
		
		// Lines
		XYItemRenderer renderer = plot.getRenderer();
		renderer.setSeriesPaint(0, Color.blue);
		renderer.setSeriesPaint(1, Color.red);
		
		// X Axis
		ValueAxis xaxis = plot.getDomainAxis();
		xaxis.setAutoRange(true);

		//Domain axis would show data of 60 seconds for a time
		xaxis.setFixedAutoRange(10000.0);
		xaxis.setVerticalTickLabels(false);
		xaxis.setTickMarksVisible(false);
		xaxis.setTickLabelsVisible(false);
		xaxis.setAxisLineVisible(false);
		
		// Y Axis
		ValueAxis yaxis = plot.getRangeAxis();
		yaxis.setRange(0.0, 8000.0);

		return result;
	}
	
	/**
	 * Perform a chart step
	 */
	public void chartStep() {
		
		// Fish
		int numberOfFish = pl.getFishCount();
		this.fishSeries.addOrUpdate(new Millisecond(), (double) numberOfFish);
		
		// Shark
		int numberOfShark = pl.getSharkCount();
		this.sharkSeries.addOrUpdate(new Millisecond(), (double) numberOfShark);
	}
	
	/**
	 * Reset the chart
	 */
	public void resetChart() {
		this.fishSeries.clear();
		this.sharkSeries.clear();
	}
	
	public ChartPanel getChartPanel() {
		return this.chartPanel;
	}
}
