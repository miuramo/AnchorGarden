package jaist.css.covis.pui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Queue;

import javax.swing.Timer;

import edu.umd.cs.piccolo.PNode;

public class FadeTimer implements ActionListener, Runnable{
	int keepMsec;
	int fadeMsec;
	public PNode pn;
	Thread thread;
	Timer timer = null;
	public Queue<FadeTimer> fadeTimers;
	//親がFadeTimerのリストを管理しており、消したらリストから自分自身を削除する場合
	public void setFadeTimers(Queue<FadeTimer> ft){
		fadeTimers = ft;
	}
	public FadeTimer(PNode p, int kM, int fM) {
		keepMsec = kM;
		pn = p;
		fadeMsec = fM;
		thread = new Thread(this);
		thread.start();
	}
	public void run() {
		try {
			Thread.sleep(keepMsec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		timer = new Timer(fadeMsec, this);
		timer.setRepeats(false);
		pn.animateToTransparency(0f, fadeMsec);
		timer.start();
	}
	public void fadeNow(){
		if (timer == null){
			timer = new Timer(fadeMsec, this);
			timer.setRepeats(false);
			pn.animateToTransparency(0f, fadeMsec);
			timer.start();
		}
	}
	public void invisibleNow(){
		if (timer == null){
			timer = new Timer(fadeMsec, this);
			timer.setRepeats(false);
			pn.setTransparency(0f);
			timer.start();
		}
	}

	public void actionPerformed(ActionEvent e) {
		pn.removeFromParent();
		if (fadeTimers != null){
			fadeTimers.remove(this);
		}
	}


}
