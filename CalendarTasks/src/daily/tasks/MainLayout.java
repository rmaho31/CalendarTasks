package daily.tasks;

import org.eclipse.swt.*;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;

public class MainLayout {
	/* I know this is sloppy and needs to be cleaned up. Trying to figure out the best way to condense some of the clutter
	 * Initializing some variables to be used later
	 */
	Text event1;
	Text descrip;
	Text length1;
	Text time1;
	Text location1;
	Canvas randomPhoto;
	Image randomImage = null;
	
	

	

	//calls the shell
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new MainLayout().createShell(display);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
	
	public Shell createShell(final Display display) {
		//initialize SQL database class to be used later
		sqlTest x1 = new sqlTest();
		
		//initialize the shell
		final Shell shell = new Shell(display);
		shell.setText("Daily Planner");
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		shell.setLayout(gridLayout);
		
		//initialize the calendar
		DateTime calendar = new DateTime (shell, SWT.CALENDAR | SWT.BORDER);
		calendar.computeSize(500, 500);
		GridData g1 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		g1.horizontalSpan = 3;
		calendar.setLayoutData(g1);
		
		//initialize the table to hold SQL data for specific dates
		Table table = new Table(shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
	    table.setLinesVisible(true);
	    table.setHeaderVisible(true);
	    GridData g2 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		g2.horizontalSpan = 3;
		table.setLayoutData(g2);

		//label table columns
	    String[] titles = {"Date", "Title","Description", "Time", "Length(Hrs)", "Location"};
	    for (int i = 0; i < titles.length; i++) {
	      TableColumn column = new TableColumn(table, SWT.NONE);
	      column.setText(titles[i]);
	    }

	    for (int i = 0; i < titles.length; i++) {
		      table.getColumn(i).pack();
		    }
	    //add the data from the SQL database to the table using the date of the calendar(default is today on launch)
	    String monthString = monthConvert(calendar.getMonth());        
		String newDate = calendar.getDay() + " " + monthString + ", " + calendar.getYear();
		try {
			ResultSet rs = x1.readDataBase(newDate);
			table.removeAll();
			while (rs.next()) {
				TableItem item = new TableItem(table, SWT.NONE);
	            item.setText(0, rs.getString("date"));
	            item.setText(1, rs.getString("event"));
	            item.setText(2, rs.getString("description"));
	            item.setText(3, rs.getString("time"));
	            item.setText(4, rs.getString("length"));
	            item.setText(5, rs.getString("location"));
			}
			if (rs != null) {
                rs.close();
            }

		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    
		
		//initialize new group for Event info
		Group eventInfo = new Group(shell, SWT.NONE);
		eventInfo.setText("Add Event");
		gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		eventInfo.setLayout(gridLayout);
		g1 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		g1.horizontalSpan = 3;
		eventInfo.setLayoutData(g1);
		
		//initialize event label and text box
		new Label(eventInfo, SWT.NONE).setText("New Event:");
		event1 = new Text(eventInfo, SWT.SINGLE | SWT.BORDER);
		g1 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		g1.horizontalSpan = 2;
		event1.setLayoutData(g1);
		
		//initialize description label and text box. multi-line functionality is wonky need to fix it
		new Label(eventInfo, SWT.NONE).setText("Description:");
		descrip = new Text(eventInfo, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
		g2 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		g2.horizontalSpan = 2;
		g2.heightHint = 3 * descrip.getLineHeight();
		descrip.setLayoutData(g2);
		
		//initialize Time label and text box
		new Label(eventInfo, SWT.NONE).setText("Time:");
		time1 = new Text(eventInfo, SWT.SINGLE | SWT.BORDER);
		g1 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		g1.horizontalSpan = 2;
		time1.setLayoutData(g1);
		
		//initialize Length label and text box
		new Label(eventInfo, SWT.NONE).setText("Length(Hrs):");
		length1 = new Text(eventInfo, SWT.SINGLE | SWT.BORDER);
		g1 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		g1.horizontalSpan = 2;
		length1.setLayoutData(g1);
		
		//Initialize Location label and text box
		new Label(eventInfo, SWT.NONE).setText("Location:");
		location1 = new Text(eventInfo, SWT.SINGLE | SWT.BORDER);
		g1 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		g1.horizontalSpan = 2;
		location1.setLayoutData(g1);
						
		//Initializes a new button and enters data into SQL database upon pressing it from Event fields
		Button enter = new Button(shell, SWT.PUSH);
		enter.setText("Enter");
		GridData g8 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		g8.horizontalSpan = 2;
		enter.setLayoutData(g8);
		enter.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String monthString = monthConvert(calendar.getMonth());		        
				String newDate = calendar.getDay() + " " + monthString + ", " + calendar.getYear();
				String[] newEvent = {newDate, event1.getText(), descrip.getText(), time1.getText(), length1.getText(), location1.getText()};
				
				try {
					x1.writeDataBase(newEvent);
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
		});
		
		//deletes the selected row from the table and also from the SQL database
		Button delete = new Button(shell, SWT.PUSH);
		delete.setText("Delete");
		g1 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		g1.horizontalSpan = 1;
		delete.setLayoutData(g1);
		delete.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String[] a = new String[table.getColumnCount()];
				for (TableItem item : table.getSelection()) {
					for(int i = 0; i < table.getColumnCount(); i++) {
					    a[i] = (item.getText(i));
						System.out.println(a[i]);
					}
				  }
				try {
					x1.deleteRowDatabase(a);
				} catch (Exception e) {
					e.printStackTrace();
				}
				table.remove(table.getSelectionIndices());
			}
		});
		
		//Random link to news local news stories will update later
		Link link = new Link(shell, SWT.RIGHT);
		link.setText("Random News Story for the day: <a href=\"www.google.com\">link</a>");
		GridData g3 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		g3.horizontalSpan = 3;
		link.setLayoutData(g3);
		link.addListener (SWT.Selection, new Listener () {
			public void handleEvent(Event event) {
				org.eclipse.swt.program.Program.launch(event.text);
			}
		});
		
		/*Initialize canvas for random meme photos to be displayed
		 * It will initialize with a random meme populated from a google
		 * image search result. Sometimes results with 403 errors. Probably
		 * should use the search API instead
		 */
		randomPhoto = new Canvas(shell, SWT.BORDER);
		GridData g5 = new GridData(GridData.FILL, GridData.FILL, true, true);
		g5.widthHint = 500;
		g5.heightHint = 500;
		g5.horizontalSpan = 3;
		g5.verticalSpan = 3;
		randomPhoto.setLayoutData(g5);
		try {
			do{
				randomImage = getImage(MotivationalMemes.generateMemes());
			} while (randomImage == null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		randomPhoto.addPaintListener(new PaintListener() {
			public void paintControl(final PaintEvent event) {
				if (randomImage != null) {
					event.gc.drawImage(randomImage, 0, 0);
				}
			}
		});
		
		//upon switching the calendar dates the table should update to entries from the 
		//SQL database that match the selected data and also updates to a new meme just for fun
		calendar.addSelectionListener (new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String monthString = monthConvert(calendar.getMonth());
				String newDate = calendar.getDay() + " " + monthString + ", " + calendar.getYear();
				try {
					ResultSet rs = x1.readDataBase(newDate);
					table.removeAll();
					while (rs.next()) {
						TableItem item = new TableItem(table, SWT.NONE);
			            item.setText(0, rs.getString("date"));
			            item.setText(1, rs.getString("event"));
			            item.setText(2, rs.getString("description"));
			            item.setText(3, rs.getString("time"));
			            item.setText(4, rs.getString("length"));
			            item.setText(5, rs.getString("location"));
					}
					if (rs != null) {
						rs.close();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					if (randomImage != null) {
						randomImage.dispose();
						randomImage = null;
						randomImage = getImage(MotivationalMemes.generateMemes());
						randomPhoto.redraw();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		
		
		
		
		
		shell.pack();
		
		return shell;
	}
	
	//converts int to month string
	public String monthConvert(int b) {
		String a = "January";
        switch (b) {
            case 1:  a = "January";
                     break;
            case 2:  a = "February";
                     break;
            case 3:  a = "March";
                     break;
            case 4:  a = "April";
                     break;
            case 5:  a = "May";
                     break;
            case 6:  a = "June";
                     break;
            case 7:  a = "July";
                     break;
            case 8:  a = "August";
                     break;
            case 9:  a = "September";
                     break;
            case 10: a = "October";
                     break;
            case 11: a = "November";
                     break;
            case 12: a = "December";
                     break;
        }
        return a;
	}
	
	//method for obtaining an image from the MotivationalMemes class
	public Image getImage(URL a) {
		InputStream is = null;
		Image image = null;
	
		try {
			URLConnection urlConnection = a.openConnection();
			urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
			is = urlConnection.getInputStream();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
			image = new Image(Display.getCurrent(), is);

		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
}