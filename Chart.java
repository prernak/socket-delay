import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardXYSeriesLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.chart.ChartPanel;

import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.category.DefaultCategoryDataset;




//class CustomItemLabelGenerator extends StandardCategoryItemLabelGenerator { 
//	private static final long serialVersionUID = 1L; 
//	public CustomItemLabelGenerator() { 
//		super(); 
//	} 
//	public String generateLabel(XYDataset xydataset, int series){ 
//	TimeSeriesCollection tscollection = (TimeSeriesCollection)xydataset; 
//	return tscollection.getSeries(series).getDescription(); 
//	} 
//} 


 class CustomXYSeriesLabelGenerator extends StandardXYSeriesLabelGenerator { 
	private static final long serialVersionUID = 1L; 
	public CustomXYSeriesLabelGenerator() { 
		super(); 
	} 
	public String generateLabel(XYDataset xydataset, int series){ 
	TimeSeriesCollection tscollection = (TimeSeriesCollection)xydataset; 
	return tscollection.getSeries(series).getDescription(); 
	} 
} 

public class Chart extends JPanel {

	private static TimeSeriesCollection dataset;
	private static TimeSeries google;
	private static TimeSeries yahoo;
	private static JFreeChart chart;
	public Chart(String applicationTitle, String chartTitle) { 
        createDataset();
        chart = createCombinedChart(chartTitle);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        chartPanel.setMouseZoomable(true, false);

        this.add(chartPanel,BorderLayout.CENTER);
	}
	
    @SuppressWarnings("deprecation")
	private static void createDataset() {
    	google = new TimeSeries("Google");
    	google.setMaximumItemAge(100);
    	google.setNotify(true);

    	
    	yahoo = new TimeSeries("Yahoo");
    	yahoo.setMaximumItemAge(100);
    	yahoo.setNotify(true);

    	
    	dataset = new TimeSeriesCollection();

    	dataset.addSeries(google);
    	dataset.addSeries(yahoo);
    }
	
    public void addPoint(Double gPoint,Double yPoint) {
    	google.addOrUpdate(new Second(new Date()), gPoint);
    	yahoo.addOrUpdate(new Second(new Date()), yPoint);
    }
	  private static JFreeChart createCombinedChart(String chartTitle) {		  
		  	JFreeChart latency = ChartFactory.createTimeSeriesChart
		  			( chartTitle, "Time", "Miliseconds", dataset, false, false, false );
		  	latency.setBackgroundPaint( Color.white );
	        final XYPlot plot = latency.getXYPlot();

	        XYItemRenderer renderer = plot.getRenderer();
	        
	       // renderer.setBaseItemLabelGenerator(new CustomItemLabelGenerator());
	        renderer.setLegendItemLabelGenerator(new CustomXYSeriesLabelGenerator());
	        
		  	final DateAxis axis = (DateAxis) plot.getDomainAxis();
		        axis.setDateFormatOverride(new SimpleDateFormat("mm:ss"));
			return latency;
	    }
}