package gui;

import java.applet.Applet;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import simulator.SimulatorController;
import driver.Simulevator;
import network.CommandServer;

import clock.Clock;

@SuppressWarnings("serial")
public class GUI extends Applet implements Runnable {

	private Image dbImage;
	private Graphics dbg;
	private static int type = AlphaComposite.SRC_OVER;

	public static boolean connection = false;
	private static Thread thread;
	public static Integer numElevators;
	public static Integer numFloors;
	private static int drawfloor;
	private static int offset;
	public static boolean elev_y_down = false;
	public static int elev_y_down_amount = 0;
	public static int elev_y_dest = 0;
	public static boolean elev_y_goto_dest = false;
	public static int x = 100;
	private CommandServer myCommander;
	private String myIP;

	// Clock
	private Clock sysClock;
	private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	public static boolean follow_elev[] = { false, false, false, false, false,
			false, false, false, false, false, false };

	private static SimulatorController SC;

	public GUI(Integer numFloors, Integer numElevators, SimulatorController S,
			Clock clk, CommandServer cs, String pIP) {
		SC = S;
		GUI.numFloors = numFloors;
		GUI.numElevators = numElevators;
		sysClock = clk;
		myCommander = cs;
		myIP = pIP;
	}

	public void stop() {
		numElevators = 0;
		numFloors = 0;
		thread = null;
	}

	// INITIALIZE
	public void init() {

		setBackground(new Color(255, 255, 255));
		setSize(new Dimension(880, 580));
		for (int i = 1; i < numElevators; i++) {
			follow_elev[i] = false;
		}
		start();
	}

	public void start() {

		thread = new Thread(this);
		thread.start();
	}

	public void run() {

		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		/* MEOW */
		while (true && numElevators != 0) {

			// IF FOLLOWING ELEVATOR - MANIPULATE SCROLLBAR POSITION
			for (int i = 0; i < numElevators; i++) {
				if (follow_elev[i + 1] == true) {
					Simulevator.SimulationScrollBar.setValue((numFloors * 60)
							- (SC.getElevatorPosition(i)) - 10 - 300);
				}
			}

			repaint();

			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		}
	}

	public void paint(Graphics g) {

		if (SC.getSimulationStatus() == 2)
			setBackground(Color.GRAY);
		else if (SC.getSimulationStatus() != 2)
			setBackground(new Color(255, 255, 255));

		offset = Simulevator.SimulationScrollBar.getValue();

		if (SC.eventStatus == 5) {
			Random rand = new Random();
			offset = Simulevator.SimulationScrollBar.getValue()
					+ rand.nextInt(20) - rand.nextInt(20);
		}

		// DRAWING STUFF
		Graphics2D gui_stuff = (Graphics2D) g;
		gui_stuff.setStroke(new BasicStroke(3.0f));

		// DRAW FLOORS, PENDING REQUESTS, AND ELEVATOR DESTINATION FLOORS
		gui_stuff.setComposite(AlphaComposite.getInstance(type, 0.40f));

		// FOR ALL FLOORS
		for (int i = 0; i < numFloors; i++) {
			// IF WITHIN THE CURRENT POSITION ON THE SCROLLABLE APPLET THEN DRAW
			if (((50 + (60 * i) - offset) > -60)
					&& ((50 + (60 * i) - offset) < 950)) {
				// FOR ALL ELEVATORS
				for (int j = 0; j < numElevators; j++) {
					// IF CURRENT FLOOR TO BE DRAWN IS THE CURRENT ELEVATORS
					// DESTINATION - DRAW WHITE AND INCREMENT FLOOR REQUEST STAT
					if (SC.getElevatorNextDestinations(j) == numFloors - i - 1)
						gui_stuff.setColor(Color.GREEN);

					// IF PENDING DOWN REQUEST - DRAW RED
					else if (SC.getNumPassengerGoingDOWN(numFloors - i - 1) > 0)
						gui_stuff.setColor(Color.RED);

					// IF PENDING UP REQUEST - DRAW YELLOW
					else if (SC.getNumPassengerGoingUP(numFloors - i - 1) > 0)
						gui_stuff.setColor(Color.YELLOW);

					// IF NO REQUEST - DRAW DARK GRAY
					else
						gui_stuff.setColor(Color.BLACK);

					// ACTUALLY DRAW
					gui_stuff.drawRect(140 + (65 * j), 50 + (60 * i) - offset,
							25, 40);
				}
			}
		}

		// LABEL STUFF
		drawfloor = numFloors - 1;
		gui_stuff.setFont(new Font("Monospaced", Font.BOLD
				+ Font.ROMAN_BASELINE, 13));
		gui_stuff.setColor(new Color((float) 0.2, (float) 0.6, (float) 0.2));
		gui_stuff.drawString(sdf.format(new Date(sysClock.getTimeMillis())),
				800, 25);
		gui_stuff.drawString(SC.getCurrentAlgorithm(), 700, 555);
		gui_stuff.drawString("My IP:", 700, 565);
		gui_stuff.drawString(myIP, 700, 575);
		if (myCommander.getConnectedUsers().size() > 0) {
			gui_stuff.drawString("Connected User:", 25, 510);
			for (int user = 0; user < myCommander.getConnectedUsers().size(); user++)
				gui_stuff.drawString(myCommander.getConnectedUsers().get(user),
						25, 520 + 10 * user);
		}
		gui_stuff.drawString("Simulation Speed", 730, 510);
		gui_stuff.drawLine(732, 530, 858, 530);
		gui_stuff.drawLine(795, 525, 795, 535);
		gui_stuff.drawLine(732, 525, 732, 535);
		gui_stuff.drawLine(858, 525, 858, 535);
		gui_stuff.setColor(new Color((float) 0.0, (float) 0.2, (float) 1.0));
		if (sysClock.getTimeScale() >= 1)
			gui_stuff.drawLine((int) (795 + (sysClock.getTimeScale() / 2)),
					520, (int) (795 + (sysClock.getTimeScale() / 2)), 540);
		else if (sysClock.getTimeScale() < 1)
			gui_stuff.drawLine(
					(int) (795 - ((1 / sysClock.getTimeScale()) / 2)), 520,
					(int) (795 - ((1 / sysClock.getTimeScale()) / 2)), 540);

		// THIS GOES THROUGH ALL THE NUMBER OF FLOORS AND LABELS THE FLOOR
		// NUMBER
		for (int i = 0; i < numFloors; i++) {
			if (((50 + (60 * i) - offset) > -60)
					&& ((50 + (60 * i) - offset) < 950)) {
				gui_stuff.setColor(new Color((float) 0.2, (float) 0.6,
						(float) 0.2));
				gui_stuff.drawString(Integer.toString(drawfloor), 80, 78
						+ (60 * i) - offset);
				gui_stuff.setColor(new Color((float) 1.0, (float) 0.2,
						(float) 0.2));
				for (int j = 0; j < SC.getNumPassengerGoingDOWN(drawfloor)
						+ SC.getNumPassengerGoingUP(drawfloor); j++) {
					gui_stuff.drawOval(770 + (20 * j), 55 + (60 * i) - offset,
							6, 6);
					gui_stuff.drawLine(773 + (20 * j), 61 + (60 * i) - offset,
							773 + (20 * j), 68 + (60 * i) - offset);
					gui_stuff.drawLine(773 + (20 * j), 63 + (60 * i) - offset,
							768 + (20 * j), 67 + (60 * i) - offset);
					gui_stuff.drawLine(773 + (20 * j), 63 + (60 * i) - offset,
							778 + (20 * j), 67 + (60 * i) - offset);
					gui_stuff.drawLine(773 + (20 * j), 68 + (60 * i) - offset,
							770 + (20 * j), 74 + (60 * i) - offset);
					gui_stuff.drawLine(773 + (20 * j), 68 + (60 * i) - offset,
							776 + (20 * j), 74 + (60 * i) - offset);
				}
				if (SC.isLockedOut(drawfloor)) {
					gui_stuff.setColor(new Color((float) 0.1, (float) 0.1,
							(float) 0.1));
					gui_stuff.fillRect(140, 48 + (60 * i) - offset, 610, 44);
				}
			}
			drawfloor--;
		}

		// DRAW ELEVATORS AND ELEVATOR INFO
		gui_stuff.setColor(new Color((float) 0.2, (float) 0.6, (float) 0.2));
		// gui_stuff.drawString("Elevator Destinations:", 10, 15);

		// FOR ALL ELEVATORS
		for (int i = 0; i < numElevators; i++) {

			// IF FOLLOWING ELEVATOR, OVERLAY DETAILED INFO
			if (follow_elev[i + 1] == true) {
				gui_stuff.setFont(new Font("Monospaced", Font.BOLD
						+ Font.ROMAN_BASELINE, 16));
				gui_stuff.setColor(new Color((float) 0.0, (float) 0.0,
						(float) 0.0, (float) 1.0));
				if (i <= 4) {
					gui_stuff.drawString("Elevator " + String.valueOf(i + 1),
							140 + (65 * i) + 40,
							(numFloors * 60) - (SC.getElevatorPosition(i))
									- offset);

					gui_stuff.setColor(Color.RED);

					gui_stuff.drawString("WEIGHT:", 140 + (65 * i) + 45,
							(numFloors * 60) - (SC.getElevatorPosition(i))
									- offset + 25);
					gui_stuff.drawString(
							String.valueOf(SC.getElevatorWeight(i)),
							140 + (65 * i) + 150,
							(numFloors * 60) - (SC.getElevatorPosition(i))
									- offset + 25);

					gui_stuff.drawString("SPEED:", 140 + (65 * i) + 45,
							(numFloors * 60) - (SC.getElevatorPosition(i))
									- offset + 45);
					gui_stuff.drawString(String.valueOf(SC.getElevatorSpeed()),
							140 + (65 * i) + 150,
							(numFloors * 60) - (SC.getElevatorPosition(i))
									- offset + 45);

					gui_stuff.drawString("POSITION:", 140 + (65 * i) + 45,
							(numFloors * 60) - (SC.getElevatorPosition(i))
									- offset + 65);
					gui_stuff.drawString(
							String.valueOf(SC.getElevatorPosition(i)),
							140 + (65 * i) + 150,
							(numFloors * 60) - (SC.getElevatorPosition(i))
									- offset + 65);

					gui_stuff.drawString("PASSENGERS:", 140 + (65 * i) + 45,
							(numFloors * 60) - (SC.getElevatorPosition(i))
									- offset + 85);
					gui_stuff.drawString(SC.getPassengerString(i),
							140 + (65 * i) + 155,
							(numFloors * 60) - (SC.getElevatorPosition(i))
									- offset + 85);

					gui_stuff.drawString("DEST:", 140 + (65 * i) + 45,
							(numFloors * 60) - (SC.getElevatorPosition(i))
									- offset + 105);
					gui_stuff.drawString(
							String.valueOf(SC.getElevatorNextDestinations(i)),
							140 + (65 * i) + 150,
							(numFloors * 60) - (SC.getElevatorPosition(i))
									- offset + 105);
					gui_stuff.drawString("UPTIME:", 140 + (65 * i) + 45,
							(numFloors * 60) - (SC.getElevatorPosition(i))
									- offset + 125);
					gui_stuff.drawString(
							String.valueOf(SC.getElevatorUpTime(i)),
							140 + (65 * i) + 150,
							(numFloors * 60) - (SC.getElevatorPosition(i))
									- offset + 125);

					int w_offset = 80 > 52 * SC.getPassengerString(i).length() / 5 ? 80
							: 52 * SC.getPassengerString(i).length() / 5;
					gui_stuff.drawRoundRect(140 + (65 * i) + 37,
							(numFloors * 60) - (SC.getElevatorPosition(i))
									- offset + 10, 150 + w_offset, 140, 20, 20);
					gui_stuff.setColor(new Color((float) 0.1, (float) 0.1,
							(float) 0.1, (float) 0.3));
					gui_stuff.fillRoundRect(140 + (65 * i) + 37,
							(numFloors * 60) - (SC.getElevatorPosition(i))
									- offset + 10, 150 + w_offset, 140, 20, 20);
				}

				else {
					gui_stuff.drawString("Elevator " + String.valueOf(i + 1),
							40 + (65 * i),
							(numFloors * 60) - (SC.getElevatorPosition(i))
									- offset);

					gui_stuff.setColor(Color.RED);

					gui_stuff.drawString("WEIGHT:", 40 + (65 * i) + 5,
							(numFloors * 60) - (SC.getElevatorPosition(i))
									- offset + 25);
					gui_stuff.drawString(
							String.valueOf(SC.getElevatorWeight(i)),
							40 + (65 * i) + 110,
							(numFloors * 60) - (SC.getElevatorPosition(i))
									- offset + 25);

					gui_stuff.drawString("SPEED:", 40 + (65 * i) + 5,
							(numFloors * 60) - (SC.getElevatorPosition(i))
									- offset + 45);
					gui_stuff.drawString(String.valueOf(SC.getElevatorSpeed()),
							40 + (65 * i) + 110,
							(numFloors * 60) - (SC.getElevatorPosition(i))
									- offset + 45);

					gui_stuff.drawString("POSITION:", 40 + (65 * i) + 5,
							(numFloors * 60) - (SC.getElevatorPosition(i))
									- offset + 65);
					gui_stuff.drawString(
							String.valueOf(SC.getElevatorPosition(i)),
							40 + (65 * i) + 110,
							(numFloors * 60) - (SC.getElevatorPosition(i))
									- offset + 65);

					gui_stuff.drawString("PASSENGERS:", 40 + (65 * i) + 5,
							(numFloors * 60) - (SC.getElevatorPosition(i))
									- offset + 85);
					gui_stuff.drawString(SC.getPassengerString(i),
							40 + (65 * i) + 115,
							(numFloors * 60) - (SC.getElevatorPosition(i))
									- offset + 85);

					gui_stuff.drawString("DEST:", 40 + (65 * i) + 5,
							(numFloors * 60) - (SC.getElevatorPosition(i))
									- offset + 105);
					gui_stuff.drawString(
							String.valueOf(SC.getElevatorNextDestinations(i)),
							40 + (65 * i) + 110,
							(numFloors * 60) - (SC.getElevatorPosition(i))
									- offset + 105);
					gui_stuff.drawString("UPTIME:", 40 + (65 * i) + 5,
							(numFloors * 60) - (SC.getElevatorPosition(i))
									- offset + 125);
					gui_stuff.drawString(
							String.valueOf(SC.getElevatorUpTime(i)),
							40 + (65 * i) + 110,
							(numFloors * 60) - (SC.getElevatorPosition(i))
									- offset + 125);

					int w_offset = 80 > 52 * SC.getPassengerString(i).length() / 5 ? 80
							: 52 * SC.getPassengerString(i).length() / 5;
					gui_stuff.drawRoundRect(40 + (65 * i) - 3, (numFloors * 60)
							- (SC.getElevatorPosition(i)) - offset + 10,
							150 + w_offset, 140, 20, 20);
					gui_stuff.setColor(new Color((float) 0.1, (float) 0.1,
							(float) 0.1, (float) 0.3));
					gui_stuff.fillRoundRect(40 + (65 * i) - 3, (numFloors * 60)
							- (SC.getElevatorPosition(i)) - offset + 10,
							150 + w_offset, 140, 20, 20);

				}

			}
			// DRAW CURRENT ELEVATOR DESTINATIONS ON THE TOP OF THE APPLET
			if (Simulevator.SC.getElevatorNextDestinations(i) == -1)
				gui_stuff.drawString("None",
						140 + (65 * i) + 5, 15);
			else
				gui_stuff.drawString(String.valueOf(Simulevator.SC
						.getElevatorNextDestinations(i)), 140 + (65 * i) + 5,
						15);


			// IF THE ELEVATOR POSITION IS WITHIN THE CURRENT VIEWABLE PORTION
			// OF APPLET - DRAW
			if (((numFloors * 60) - (SC.getElevatorPosition(i)) - 10 - offset > -60)
					&& ((numFloors * 60) - (SC.getElevatorPosition(i)) - 10
							- offset < 950)) {

				// DRAW THE ELEVATOR AT THE CORRECT POSITION
				gui_stuff.setStroke(new BasicStroke(2.0f));
				gui_stuff.setColor(new Color((float) 0.2, (float) 0.4,
						(float) 1.0, (float) 0.9));
				// You can see when elevator needs maintenance
				if (SC.getElevatorState(i) != 0) {
					switch (SC.getElevatorState(i)) {
					case 1: // Out of Order
						gui_stuff.setColor(new Color((float) 1.0, (float) 0.0,
								(float) 0.0, (float) 0.8));

						break;
					case 2: // Need Maintenance
						gui_stuff.setColor(new Color((float) 0.2, (float) 0.2,
								(float) 0.2, (float) 0.8));
						break;
					}

				}
				gui_stuff.fillRect(140 + (65 * i),
						(numFloors * 60) - (SC.getElevatorPosition(i)) - 10
								- offset, 25, 40);

				// DECORATION TO MAKE ELEVATOR PRETTY
				if (SC.getDoorSensor(i) == 0) {
					gui_stuff.setColor(new Color((float) 0.1, (float) 0.1,
							(float) 1.0, (float) 0.9));
					gui_stuff.drawLine(140 + (65 * i) + 12, (numFloors * 60)
							- (SC.getElevatorPosition(i)) - 10 - offset,
							140 + (65 * i) + 12,
							(numFloors * 60) - (SC.getElevatorPosition(i))
									- offset + 30);
				}

				// DECORATION TO MAKE ELEVATOR PRETTY
				gui_stuff.setStroke(new BasicStroke(3, BasicStroke.CAP_SQUARE,
						BasicStroke.JOIN_BEVEL, 0, new float[] { 3, 1 }, 0));
				gui_stuff.setColor(new Color((float) 0.0, (float) 0.4,
						(float) 0.1, (float) 1.0));
				gui_stuff.drawRect(140 + (65 * i),
						(numFloors * 60) - (SC.getElevatorPosition(i)) - 10
								- offset, 25, 40);

				// DRAW DOOR OPEN/CLOSE
				gui_stuff.setColor(Color.BLACK);
				gui_stuff.fillRect(140 + (65 * i) + 12
						- (SC.getDoorSensor(i) / 8),
						(numFloors * 60) - (SC.getElevatorPosition(i)) - 10
								- offset, SC.getDoorSensor(i) / 4, 40);

			}

		}

	}

	// DOUBLE BUFFERING SO SCREEN DOESN'T FLICKER
	public void update(Graphics g) {
		if (dbImage == null) {
			dbImage = createImage(900, 900);
			dbg = dbImage.getGraphics();
		}

		dbg.setColor(getBackground());
		dbg.fillRect(0, 0, 1000, 1000);

		dbg.setColor(getForeground());
		paint(dbg);

		g.drawImage(dbImage, 0, 0, this);
	}

	// KEY PRESSING EVENTS
	public boolean keyDown(Event e, int key) {

		if (key == Event.UP) {
			Simulevator.SimulationScrollBar
					.setValue(Simulevator.SimulationScrollBar.getValue() - 500);
		}

		if (key == Event.DOWN) {
			Simulevator.SimulationScrollBar
					.setValue(Simulevator.SimulationScrollBar.getValue() + 500);
		}

		if (key == 45) {
			follow_elev[1] = false;
			follow_elev[2] = false;
			follow_elev[3] = false;
			follow_elev[4] = false;
			follow_elev[5] = false;
			follow_elev[6] = false;
			follow_elev[7] = false;
			follow_elev[8] = false;
			follow_elev[9] = false;
			follow_elev[10] = false;

		}

		if (key == 49) {
			follow_elev[1] = true;
			follow_elev[2] = false;
			follow_elev[3] = false;
			follow_elev[4] = false;
			follow_elev[5] = false;
			follow_elev[6] = false;
			follow_elev[7] = false;
			follow_elev[8] = false;
			follow_elev[9] = false;
			follow_elev[10] = false;

		}

		if (key == 50) {
			follow_elev[1] = false;
			follow_elev[2] = true;
			follow_elev[3] = false;
			follow_elev[4] = false;
			follow_elev[5] = false;
			follow_elev[6] = false;
			follow_elev[7] = false;
			follow_elev[8] = false;
			follow_elev[9] = false;
			follow_elev[10] = false;

		}

		if (key == 51) {
			follow_elev[1] = false;
			follow_elev[2] = false;
			follow_elev[3] = true;
			follow_elev[4] = false;
			follow_elev[5] = false;
			follow_elev[6] = false;
			follow_elev[7] = false;
			follow_elev[8] = false;
			follow_elev[9] = false;
			follow_elev[10] = false;

		}

		if (key == 52) {
			follow_elev[1] = false;
			follow_elev[2] = false;
			follow_elev[3] = false;
			follow_elev[4] = true;
			follow_elev[5] = false;
			follow_elev[6] = false;
			follow_elev[7] = false;
			follow_elev[8] = false;
			follow_elev[9] = false;
			follow_elev[10] = false;

		}

		if (key == 53) {
			follow_elev[1] = false;
			follow_elev[2] = false;
			follow_elev[3] = false;
			follow_elev[4] = false;
			follow_elev[5] = true;
			follow_elev[6] = false;
			follow_elev[7] = false;
			follow_elev[8] = false;
			follow_elev[9] = false;
			follow_elev[10] = false;

		}

		if (key == 54) {
			follow_elev[1] = false;
			follow_elev[2] = false;
			follow_elev[3] = false;
			follow_elev[4] = false;
			follow_elev[5] = false;
			follow_elev[6] = true;
			follow_elev[7] = false;
			follow_elev[8] = false;
			follow_elev[9] = false;
			follow_elev[10] = false;

		}

		if (key == 55) {
			follow_elev[1] = false;
			follow_elev[2] = false;
			follow_elev[3] = false;
			follow_elev[4] = false;
			follow_elev[5] = false;
			follow_elev[6] = false;
			follow_elev[7] = true;
			follow_elev[8] = false;
			follow_elev[9] = false;
			follow_elev[10] = false;

		}

		if (key == 56) {
			follow_elev[1] = false;
			follow_elev[2] = false;
			follow_elev[3] = false;
			follow_elev[4] = false;
			follow_elev[5] = false;
			follow_elev[6] = false;
			follow_elev[7] = false;
			follow_elev[8] = true;
			follow_elev[9] = false;
			follow_elev[10] = false;

		}

		if (key == 57) {
			follow_elev[1] = false;
			follow_elev[2] = false;
			follow_elev[3] = false;
			follow_elev[4] = false;
			follow_elev[5] = false;
			follow_elev[6] = false;
			follow_elev[7] = false;
			follow_elev[8] = false;
			follow_elev[9] = true;
			follow_elev[10] = false;

		}

		if (key == 48) {
			follow_elev[1] = false;
			follow_elev[2] = false;
			follow_elev[3] = false;
			follow_elev[4] = false;
			follow_elev[5] = false;
			follow_elev[6] = false;
			follow_elev[7] = false;
			follow_elev[8] = false;
			follow_elev[9] = false;
			follow_elev[10] = true;

		}

		return true;
	}

	public void interrupt() {
		thread.interrupt();
	}

}
