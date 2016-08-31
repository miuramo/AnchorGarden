package jaist.css.covis.cls;

import edu.umd.cs.piccolo.PNode;

public class CovisObj_TransparencyControl implements Runnable {
	Thread transparencyThread = null;
//	ArrayList<PNode> effectAry; //ダイナミックに作成する?(計算コストを考え，ここではしない)
	float target_transparency[] = new float[]{0.0f, 1.0f};
	public float tick_transparency[] = new float[]{-0.008f, 0.5f};
	int direction = 1;
	int msec;
	int slow_percentrate = 100;
	float animcount = 1000;
	float currentValue = 1f;
	boolean quitTransparencyThread = true;
	Covis_Object obj;
	PNode window;
	public CovisObj_TransparencyControl(Covis_Object o){
		obj = o;
		
		// ここに，影響をあたえるPNodeを追加する
	}

	public void transparencyThread_Start(int direct, PNode w, int _slow_percentrate){
		window = w;
		slow_percentrate = _slow_percentrate;
		if (direct == 0 && direction == 0) return;
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

			obj.setTransparency(currentValue);
			if (window != null) window.repaint();

			try {
				Thread.sleep(100 * slow_percentrate / 100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (direction == 0) {
//			System.out.println("dead.");

			obj.detachAll();
			obj.removeFromParent();
			obj.dispose();
			

		}
		transparencyThread = null;
//		
	}
}
