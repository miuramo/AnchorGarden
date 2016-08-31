package jaist.css.covis.pui;

import jaist.css.covis.AnchorGarden;

import java.awt.Dimension;
import java.util.ArrayList;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.util.PBounds;

public class PMenuBar extends PBasicInputEventHandler implements CameraFittable, Runnable {
	private static final long serialVersionUID = 1776018940471391776L;
	
	public PPath mybar;
	PCamera camera;
	AnchorGarden window;
//	float transparency = 0.8f;
	float base_transparency = 0.9f;
	Thread transparencyThread = null;
	boolean quitTransparencyThread = true;
	ArrayList<CameraFitConstraint> cfcList;
	public PMenuBar(PCamera _cam, AnchorGarden _win){
		camera = _cam;
		window = _win;
		mybar = new PPath();
		mybar.setTransparency(base_transparency);
		camera.addChild(mybar);
		camera.addInputEventListener(this);
		cfcList = new ArrayList<CameraFitConstraint>();
	}
	public void addPMenuRoot(PMenuRoot root, CameraFitConstraint.PositionEnum pos, Dimension _defaultGap){
		mybar.addChild(root);
//		Point2D initP = new Point2D.Double(window.buffer.gkjProperty.viewsizey - root.getFullBoundsReference().getWidth(),0);
//		if (pos == CameraFitConstraint.PositionEnum.TOPLEFT) initP.setLocation(0,0);
//		root.setOffset(initP);
		CameraFitConstraint cfc = new CameraFitConstraint(root, camera, pos, _defaultGap);
		cfc.fitTo(camera.getBoundsReference());
		camera.addPropertyChangeListener(cfc);
	}
	public PBounds getFullBounds() {
		return mybar.getFullBounds();
	}
	public void setOffset(double x, double y) {
		mybar.setOffset(x,y);
	}
	public void setPathToRectangle(float x, float y, float w, float h) {
		mybar.setPathToRectangle(x,y,w,h);
	}
	
	public void mouseMoved(PInputEvent event) {
		double y = event.getPositionRelativeTo(camera).getY();
		if (y < getFullBounds().getHeight()*2){
			if (transparencyThread != null) quitTransparencyThread = true;
			mybar.setTransparency(base_transparency);
			mybar.repaint();
		} else {
			if (mybar.getTransparency() == base_transparency) transparencyThread_Start();
		}
	}
	void transparencyThread_Start(){
		if (transparencyThread == null){
			quitTransparencyThread = false;
			transparencyThread = new Thread(this);
			transparencyThread.start();
		}
	}
	public void run() {
		for(float f = base_transparency; f>=0.0f; f-=0.05){
			mybar.setTransparency(f);
			mybar.repaint();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (quitTransparencyThread) {
				mybar.setTransparency(base_transparency);
				mybar.repaint();
				transparencyThread = null;
				break;
			}
		}
		mybar.setTransparency(0.0f);
		mybar.repaint();
		transparencyThread = null;
	}
	public void manualFit(PBounds pb) {
		for(CameraFitConstraint cfc: cfcList){
			cfc.fitTo(pb);
		}
	}

}
