package client;

import java.awt.event.ActionListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.Timer;

/**
 * A Document listener that waits a set period of time after a JText
 * object has been selected to enable updating changes as they're made
 * in the JText object
 * 
 * @see https://stackoverflow.com/a/31955279
 */
public class DeferredDocumentListener implements DocumentListener {
	private final Timer timer;
	final int timeout = 100;
	final boolean repeats = true;
	
	public DeferredDocumentListener(ActionListener listener) {
		timer = new Timer(timeout, listener);
		timer.setRepeats(repeats);
	}
	
	public void start() {
		timer.start();
	}
	
	public void stop() {
		timer.stop();
	}
	
	@Override
	public void insertUpdate(DocumentEvent e) {
		timer.restart();
	}
	
	@Override
	public void removeUpdate(DocumentEvent e) {
		timer.restart();
	}
	
	@Override
	public void changedUpdate(DocumentEvent e) {
		timer.restart();
	}
 }
