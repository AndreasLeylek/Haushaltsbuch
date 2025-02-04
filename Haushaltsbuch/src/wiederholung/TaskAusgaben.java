package wiederholung;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import datenbank.Datenbank;
import javafx.application.Platform;
import model.Ausgaben;
import model.Intervall;
import model.WiederholungenAusgaben;

public class TaskAusgaben implements Runnable {

	private final ScheduledExecutorService service;
	private final Runnable task;
	private final int hour;
	private final int min;
	private final int sec;

	public static Runnable getRunnableTask(Runnable afterrunnableTask) {    	   	
		return () -> {
			try {
				List<WiederholungenAusgaben> alAusgabenWiederholungen = 
						Datenbank.leseWiederholungenAusgaben().stream().filter(e -> e.getDp().compareTo(LocalDate.now()) <= 0).toList(); 
				// holen alle Wiederholungen die heute gemacht werden 

				
				for (WiederholungenAusgaben e : alAusgabenWiederholungen){
					Datenbank.insertAusgabe(new Ausgaben(0, e.getAusgaben().getKategorie(), 
							e.getAusgaben().getProdukt(), e.getAusgaben().getPreis(), LocalDate.now()));		
					e.setDp(Intervall.values()[e.getIntervall().ordinal()].addTo(e.getDp()));
					Datenbank.updateAusgabenWiederholungen(e);
					
				
				}
				Platform.runLater(afterrunnableTask);
			} catch (SQLException e) {

				e.printStackTrace();
			}  
		};
	}

	public static void initDailyTask(Runnable afterrunnableTask) {		//startet Task für heute & macht die Wiederholung für den nächsten Tag 
		Calendar cal = Calendar.getInstance();
		System.out.println("Time now: " + cal.getTimeInMillis());  // 5 Sekunden nach start und danach "ZEILE 52"
		cal.add(Calendar.SECOND, 5);
		System.out.println("Execute at: " + cal.getTimeInMillis());
		int hour = cal.get(Calendar.HOUR);
		int min = cal.get(Calendar.MINUTE);
		int sec = cal.get(Calendar.SECOND);

		TaskAusgaben daily = new TaskAusgaben(hour, min, sec, afterrunnableTask);
		daily.reSchedule();

		System.out.println("Done");
	}

	public TaskAusgaben(final int hour, final int min, final int sec, Runnable afterrunnableTask) {
		this.service = Executors.newScheduledThreadPool(1);
		this.task = TaskAusgaben.getRunnableTask(afterrunnableTask);
		this.hour = hour;
		this.min = min;
		this.sec = sec;       
	}

	private void reSchedule() { //
		Calendar calendar = Calendar.getInstance();
		long now = calendar.getTimeInMillis();
		calendar.set(Calendar.HOUR, hour);
		calendar.set(Calendar.MINUTE, min);
		calendar.set(Calendar.SECOND, sec);
		calendar.set(Calendar.MILLISECOND, 0);
		while (calendar.getTimeInMillis() < now) {
			calendar.add(Calendar.DAY_OF_WEEK, 1); //aktualisierung jeden tag 
		}

		service.schedule(this, calendar.getTimeInMillis() - now, TimeUnit.MILLISECONDS); // plant den Task mit der richtigen Zeit
	}

	@Override
	public void run() {
		try {
			Platform.runLater(task);
		} finally {
			reSchedule();
		}
	}
}
