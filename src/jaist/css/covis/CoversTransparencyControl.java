package jaist.css.covis;

import java.util.ArrayList;

import edu.umd.cs.piccolo.PNode;

public class CoversTransparencyControl implements Runnable {
	Thread transparencyThread = null;
	ArrayList<PNode> effectAry; //ダイナミックに作成する?(計算コストを考え，ここではしない)
	AnchorGarden window;
	float target_transparency[] = new float[]{0.0f, 1.0f};
	float tick_transparency[] = new float[]{-0.035f, 0.05f};
	int direction = 0;
	int msec;
	float animcount = 20;
	float currentValue = 1f;
	boolean quitTransparencyThread = true;
	public CoversTransparencyControl(AnchorGarden _win){
		window = _win;
		
		effectAry = new ArrayList<PNode>();
		// ここに，影響をあたえるPNodeを追加する

	}
	
	void transparencyThread_Start(int direct){
		if (direct == direction) return;
		direction = direct;
//		System.out.println("direction changes to "+direction);
		
		if (transparencyThread == null){
			quitTransparencyThread = false;
			transparencyThread = new Thread(this);
			transparencyThread.start();
		}
	}
	public void run() {
		while(!quitTransparencyThread){
			currentValue += tick_transparency[direction];
			if ((direction == 0 && currentValue < target_transparency[direction]) || 
					(direction == 1 && target_transparency[direction] < currentValue )) {
				currentValue = target_transparency[direction];
				quitTransparencyThread = true;
			}
			for(PNode pn: effectAry){
				pn.setTransparency(currentValue);
			}
			window.canvas.repaint();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		transparencyThread = null;
//		System.out.println("Thread exit");
	}
}
