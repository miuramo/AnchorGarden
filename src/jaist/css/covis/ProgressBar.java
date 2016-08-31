package jaist.css.covis;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

/**
 * 時間がかかるタスクのためのプログレスバー
 * @see DBReadTask
 * @see FileReadTask
 * 
 */
@SuppressWarnings("serial")
public class ProgressBar extends JPanel {
	public final static int ONE_SECOND = 1300;

	private JProgressBar progressBar;

	private Timer timer;

	// private JButton startButton;
	private ProgressibleTask task;

	private JTextArea taskOutput;

	private String newline = "\n";

	private Object targetobj;

	private Method aftercall;

	private JFrame parent;

	public ProgressBar(ProgressibleTask thetask, Object obj, Method ac, JFrame p) {
		super(new BorderLayout());
		targetobj = obj;
		aftercall = ac;
		parent = p;
		task = thetask;

		progressBar = new JProgressBar(0, task.getLengthOfTask());
		progressBar.setValue(0);
		progressBar.setStringPainted(true);

		taskOutput = new JTextArea(5, 20);
		taskOutput.setMargin(new Insets(5, 5, 5, 5));
		taskOutput.setEditable(false);
		taskOutput.setCursor(null);
		// inherit the panel's cursor
		// see bug 4851758

		// add(panel, BorderLayout.PAGE_START);
		add(progressBar, BorderLayout.NORTH);
		add(new JScrollPane(taskOutput), BorderLayout.SOUTH);
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Create a timer.
		timer = new Timer(ONE_SECOND, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				progressBar.setValue(task.getCurrent());
				String s = task.getMessage();
				if (s != null) {
					taskOutput.append(s + newline);
					taskOutput.setCaretPosition(taskOutput.getDocument()
							.getLength());
				}
				if (task.isDone()) {
					// Toolkit.getDefaultToolkit().beep();
					timer.stop();
					// startButton.setEnabled(true);
					setCursor(null); // turn off the wait cursor
					// progressBar.setValue(progressBar.getMinimum());
					/*
					 * snaillogloader.frame.dispose();
					 * snaillogloader.startSnailViewer(); snaillogloader.frame =
					 * null;
					 */
					parent.dispose();
					parent = null;

					// 登録された後処理メソッドがあれば実行．
					if (aftercall == null)
						return;
					try {
						@SuppressWarnings("rawtypes")
						Class[] params = {};
						@SuppressWarnings("unused")
						Object ret = aftercall.invoke(targetobj,
								(Object[]) params);
					} catch (Exception ex) {
					}
				}
			}
		});
	}

	/**
	 * Called when the user presses the playB button.
	 */
	public void actionPerformed(ActionEvent evt) {
		// startButton.setEnabled(false);
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		task.go();
		timer.start();
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread. private static void
	 * createAndShowGUI() { //Make sure we have nice window decorations.
	 * JFrame.setDefaultLookAndFeelDecorated(true);
	 * 
	 * //Create and set up the window. JFrame frame = new
	 * JFrame("ProgressBarDemo");
	 * frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 * 
	 * //Create and set up the content pane. JComponent newContentPane = new
	 * ProgressBar(null); newContentPane.setOpaque(true); //content panes must
	 * be opaque frame.setContentPane(newContentPane);
	 * 
	 * //Display the window. frame.pack(); frame.setVisible(true); }
	 * 
	 * public static void main(String[] args) { //Schedule a job for the
	 * event-dispatching thread: //creating and showing this application's GUI.
	 * javax.swing.SwingUtilities.invokeLater(new Runnable() { public void run() {
	 * createAndShowGUI(); } }); }
	 */
}
