package nbradham.nudger;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.IOException;

import javax.imageio.ImageIO;

final class Nudger {

	private boolean run = true;

	private void start() throws AWTException, IOException {
		TrayIcon ti = new TrayIcon(ImageIO.read(Nudger.class.getResource("/icon.png")), "Nudger (Double click to terminate)");
		ti.setImageAutoSize(true);
		SystemTray st = SystemTray.getSystemTray();
		Thread t = new Thread(() -> {
			try {
				Robot r = new Robot();
				Point p;
				boolean b = false;
				while (run) {
					p = MouseInfo.getPointerInfo().getLocation();
					r.mouseMove(p.x + ((b = !b) ? 1 : -1), p.y);
					try {
						Thread.sleep(299000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} catch (AWTException e) {
				e.printStackTrace();
			}
			st.remove(ti);
		});
		ti.addActionListener(e -> {
			run = false;
			t.interrupt();
		});
		st.add(ti);
		t.start();
	}

	public static void main(String[] args) throws AWTException, IOException {
		new Nudger().start();
	}
}