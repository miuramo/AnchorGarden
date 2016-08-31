package jaist.css.covis;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PInputManager;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PInputEventListener;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolo.util.PPaintContext;
import edu.umd.cs.piccolox.event.PZoomToEventHandler;
import jaist.css.covis.cls.Covis_Object;
import jaist.css.covis.cls.Informer;
import jaist.css.covis.fm.FlowMenu_TMRG;
import jaist.css.covis.fm.PActionListener;
import jaist.css.covis.pui.CameraFitConstraint.PositionEnum;
import jaist.css.covis.pui.FadeTimer;
import jaist.css.covis.pui.PMenuBar;
import jaist.css.covis.pui.PMenuItem_Color;
import jaist.css.covis.pui.PMenuRoot;
import jaist.css.covis.util.BUtil;
import jaist.css.covis.util.MouseWheelRotationListener;
import jaist.css.covis.util.MyPCanvas;

/**
 * 最初に起動されるクラス
 * @author miuramo
 *
 */
public class AnchorGarden extends RootWindow implements MouseWheelRotationListener, Runnable {

	public static final String WINDOWTITLE = "Anchor Garden 2";

	//TODO: ここでログインとソースコード記録するかどうかを決定する trueならログ記録
	//	public static final boolean LOGGINGSRC = true;
	public static final boolean LOGGINGSRC = false;

	public transient static Color messageBackColor = new Color(254,200,200);
	//	public FadeTimer[] messageFadeTimers = new FadeTimer[20];
	public Queue<FadeTimer> messageFadeTimersQueue= new ConcurrentLinkedQueue<FadeTimer>();

//	Category log;

	/**
	 * 最初に実行するmainメソッド
	 * @param args
	 */

	public static void main(String[] args){
		new AnchorGarden();
	}
	public AnchorGarden(){
		this(new CoVisBuffer());
	}
	public AnchorGarden(CoVisBuffer b){
		super(b);
//		BasicConfigurator.configure(); // Log4J Initialize
		// オープニングの鳴き声
		Informer.playSound("Rooster.wav");
//		log = Logger.getLogger(AnchorGarden.class.getName());
		initializeMenu();
		switchBuffer(buffer);
	}


	int REPAINTINTERVAL = 5000;
	Thread repaintcheckthread;

	public FlowMenu_TMRG flowmenuEventHandler;

	PPickEventHandler pickEventHandler;

	MyPPanEventHandler panEventHandler;

	//	DrawEventHandler drawEventHandler;

	PZoomToEventHandler zoomeh;

	ActionListener repaintaction;

	//	CameraFitConstraint selectorFitConstraint;

	CoversTransparencyControl coversTransparencyControl;

	PMenuBar pmenubar;

	PMenuRoot mroot;

	PMenuRoot mcolorroot; // 色パレットのルート

	PMenuRoot mpageroot; // ページ一括切り替えメニュー

	PBounds panregionbounds;

	boolean isFlash = false;

	boolean dirty;
	boolean ready = false;

	PText modenode;

	public Point2D cursorpoint;

	public PInputEvent lastCursorPointingEvent;

	DropTarget target = null;

	PInputEventListener piel;

	ViewerKeyEventListener viewerKeyListener;

	PCamera virtualCamera;


	public static BasicStroke REDINKSTROKE = new BasicStroke((float) 2.0,
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 90.0f);

	public static Color SYSTEMINKCOLOR = Color.red;// StudentPanel.inkcolor;
	// public static int REDINKCOLORINT=3;

	PNode tooltipNode;

	boolean showToolTip = true;

	ArrayList<PNode> bunruiExplainTexts = new ArrayList<PNode>();

	public static void setRedInkStroke(float f) {
		REDINKSTROKE = new BasicStroke((float) f, BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND, 90.0f);
	}

	public static void setRedInkColor(Color c) {
		SYSTEMINKCOLOR = c;
	}

	public void setShowToolTip(boolean f) {
		showToolTip = f;
		if (!f) if (tooltipNode != null) tooltipNode.setVisible(false);
	}

	public PBounds zoomBounds_focusbyCursor(PBounds pb, double rate) {
		double x = pb.getX();
		double y = pb.getY();
		double w = pb.getWidth();
		double h = pb.getHeight();
		double nw = w * rate;
		double nh = h * rate;
		Point2D camcp = canvas.getCamera().getViewBounds().getCenter2D();
		double camcx = camcp.getX();
		double camcy = camcp.getY();
		double curx = cursorpoint.getX();
		double cury = cursorpoint.getY();
		double nx = x - (nw - w) / 2 + curx - camcx;
		double ny = y - (nh - h) / 2 + cury - camcy;
		PBounds ret = new PBounds(nx, ny, nw, nh);
		cursorpoint = ret.getCenter2D();
		moveCursorPointCenter();
		return ret;
	}

	public MyPCanvas getCanvas() {
		return canvas;
	}

	public void moveCursorPointCenter() {
		Point canvasglobalp = getCanvas().getLocationOnScreen();
		int canvasw = getCanvas().getWidth();
		int canvash = getCanvas().getHeight();
		try {
			Robot r = new Robot();
			r.mouseMove((int) (canvasglobalp.getX() + canvasw / 2),
					(int) (canvasglobalp.getY() + canvash / 2));
		} catch (AWTException ex) {
		}
	}

	/**
	 * ATNRootWindow.switchBuffer から呼ばれる（重要！！）
	 *
	 */
	public void initialize() {
		getCanvas().setDefaultRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
		getCanvas().setAnimatingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
		getCanvas().setInteractingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);

		disablemask.rejectAllEventTypes();
		// flowmenu
		flowmenuEventHandler = new FlowMenu_TMRG(this);
		flowmenuEventHandler.setEventFilter(b1mask);
		getCanvas().addInputEventListener(flowmenuEventHandler);

		// pick
		pickEventHandler = new PPickEventHandler(this);
		pickEventHandler.setEventFilter(disablemask);
		getCanvas().addInputEventListener(pickEventHandler);
		//		getCanvas().getRoot().getDefaultInputManager().setKeyboardFocus(pickEventHandler);

		// pan
		panEventHandler = new MyPPanEventHandler(this);
		panEventHandler.setEventFilter(disablemask);
		getCanvas().addInputEventListener(panEventHandler);

		// draw
		//		drawEventHandler = new DrawEventHandler(this);
		//		drawEventHandler.setEventFilter(disablemask);
		//		getCanvas().addInputEventListener(drawEventHandler);

		//		// Button2 - pan
		//		getCanvas().getPanEventHandler().setEventFilter(b2mask);

		// Button 3 - regionzoom
		CoVisWindow_ZoomRegionHandler zrh = new CoVisWindow_ZoomRegionHandler(this);
		getCanvas().addInputEventListener(zrh);
		zrh.setEventFilter(b3mask);
		getCanvas().removeInputEventListener(getCanvas().getZoomEventHandler());

		// check current cursor position
		getCanvas().addInputEventListener(new PInputManager() {
			public void mouseMoved(PInputEvent e) {
				cursorpoint = e.getPosition();
				lastCursorPointingEvent = e;
			}
		});

		// ショートカットキーイベント
		viewerKeyListener = new ViewerKeyEventListener(this);
		getCanvas().getCamera().addInputEventListener(viewerKeyListener);
		//		frame.addKeyListener(viewerKeyListener);
		getCanvas().addKeyListener(viewerKeyListener);

		//		mroot = new PMenuRoot();
		//		PMenuItem_String pm1 = new PMenuItem_String("menu", true, Color.orange);
		//		PMenuItem_String pm2 = new PMenuItem_String("pick", true, Color.orange);
		//		PMenuItem_String pm3 = new PMenuItem_String("pan", true, Color.orange);
		//		PMenuItem_String pm4 = new PMenuItem_String("draw", true, Color.orange);
		//		// ^^^^ true if the menu is a "radio button"
		//		mroot.add(pm1);
		//		mroot.add(pm2);
		//		mroot.add(pm3);
		//		mroot.add(pm4);
		//		mroot.setSelected(pm1);
		//		pm1.addPActionListener(new PActionListener() {
		//			public void actionPerformed(PInputEvent e) {
		//				flowmenuEventHandler.setEventFilter(b1mask);
		//				pickEventHandler.setEventFilter(disablemask);
		//				panEventHandler.setEventFilter(disablemask);
		//				drawEventHandler.setEventFilter(disablemask);
		//			}
		//		});
		//		pm2.addPActionListener(new PActionListener() {
		//			public void actionPerformed(PInputEvent e) {
		//				flowmenuEventHandler.setEventFilter(disablemask);
		//				pickEventHandler.setEventFilter(b1mask);
		//				panEventHandler.setEventFilter(disablemask);
		//				drawEventHandler.setEventFilter(disablemask);
		//			}
		//		});
		//		pm3.addPActionListener(new PActionListener() {
		//			public void actionPerformed(PInputEvent e) {
		//				flowmenuEventHandler.setEventFilter(disablemask);
		//				pickEventHandler.setEventFilter(disablemask);
		//				panEventHandler.setEventFilter(b1mask);
		//				drawEventHandler.setEventFilter(disablemask);
		//			}
		//		});
		//		pm4.addPActionListener(new PActionListener() {
		//			public void actionPerformed(PInputEvent e) {
		//				flowmenuEventHandler.setEventFilter(disablemask);
		//				pickEventHandler.setEventFilter(disablemask);
		//				panEventHandler.setEventFilter(disablemask);
		//				drawEventHandler.setEventFilter(b1mask);
		//			}
		//		});
		//		// getCanvas().getCamera().addChild(pm_root);
		//		mroot.setOffset(660, 0);

		pmenubar = new PMenuBar(getCanvas().getCamera(), this);
		//		pmenubar.addPMenuRoot(mroot, PositionEnum.TOPCENTER, new Dimension());

		//		mcolorroot = new PMenuRoot();
		//		PMenuItem_Color pmc_black = new PMenuItem_Color("k", true, Color.black);
		////		PMenuItem_Color pmc_white = new PMenuItem_Color("w", true, Color.white);
		//		PMenuItem_Color pmc_blue = new PMenuItem_Color("b", true, Color.blue);
		//		PMenuItem_Color pmc_green = new PMenuItem_Color("g",true, new Color(0,120,0));
		////		System.out.println("緑　"+(long)(new Color(0,120,0).getRGB()));
		//		PMenuItem_Color pmc_red = new PMenuItem_Color("r", true, Color.red);
		////		PMenuItem_Color pmc_orange = new PMenuItem_Color("o",true,Color.orange);
		//		mcolorroot.add(pmc_black);
		////		mcolorroot.add(pmc_white);
		//		mcolorroot.add(pmc_blue);
		//		mcolorroot.add(pmc_green);
		//		mcolorroot.add(pmc_red);
		////		mcolorroot.add(pmc_orange);
		//		mcolorroot.setSelected(pmc_red);
		//		pmenubar.addPMenuRoot(mcolorroot, PositionEnum.TOPRIGHT, new Dimension());
		//		pmc_black.addPActionListener(new PActionListener() {
		//			public void actionPerformed(PInputEvent e) {
		//				CoVisWindow.setRedInkColor(Color.black);
		//			}
		//		});
		////		pmc_white.addPActionListener(new PActionListener() {
		////			public void actionPerformed(PInputEvent e) {
		////				GKJWindow.setRedInkColor(Color.red);
		////			}
		////		});
		//		pmc_red.addPActionListener(new PActionListener() {
		//			public void actionPerformed(PInputEvent e) {
		//				CoVisWindow.setRedInkColor(Color.red);
		//			}
		//		});
		//		pmc_blue.addPActionListener(new PActionListener() {
		//			public void actionPerformed(PInputEvent e) {
		//				CoVisWindow.setRedInkColor(Color.blue);
		//			}
		//		});
		//		pmc_green.addPActionListener(new PActionListener(){
		//			public void actionPerformed(PInputEvent e){
		//				CoVisWindow.setRedInkColor(new Color(0,120,0));
		//			}
		//		});
		////		pmc_orange.addPActionListener(new PActionListener(){
		////			public void actionPerformed(PInputEvent e){
		////				GKJWindow.setRedInkColor(Color.orange);
		////			}
		////		});
		//		viewerKeyListener.addPMenuToShortcutList(pmc_black);
		////		viewerKeyListener.addPMenuToShortcutList(pmc_white);
		//		viewerKeyListener.addPMenuToShortcutList(pmc_red);
		//		viewerKeyListener.addPMenuToShortcutList(pmc_blue);
		//		viewerKeyListener.addPMenuToShortcutList(pmc_green);
		////		viewerKeyListener.addPMenuToShortcutList(pmc_orange);

		PMenuRoot layoutPMR = new PMenuRoot();
		PMenuItem_Color pmc_reset = new PMenuItem_Color("Reset World", false, Color.orange);
		PMenuItem_Color pmc_layout = new PMenuItem_Color("Zoom home (ESC-key)", false, Color.orange);
		//		PMenuItem_Color pmc_src = new PMenuItem_Color("source code", false, Color.orange);
		//		PMenuItem_Color pmc_cover = new PMenuItem_Color("cover", false, Color.orange);
		//		PMenuItem_Color pmc_drawinfo = new PMenuItem_Color("peninfo", false, Color.orange);
		//		PMenuItem_Color pmc_label = new PMenuItem_Color("label", false, Color.orange);
		layoutPMR.add(pmc_reset);
		layoutPMR.add(pmc_layout);
		//		layoutPMR.add(pmc_src);
		//		layoutPMR.add(pmc_cover);
		//		layoutPMR.add(pmc_drawinfo);
		//		layoutPMR.add(pmc_label);

		pmenubar.addPMenuRoot(layoutPMR, PositionEnum.TOPLEFT, new Dimension());
		pmc_reset.addPActionListener(new PActionListener() {
			public void actionPerformed(PInputEvent e) {
				AnchorGarden.this.resetWorld();
			}
		});
		pmc_layout.addPActionListener(new PActionListener() {
			public void actionPerformed(PInputEvent e) {
				AnchorGarden.this.fitAndZoomHome(200);
			}
		});
		//		pmc_src.addPActionListener(new PActionListener(){
		//			public void actionPerformed(PInputEvent e){
		//				showSrcWin();
		//			}
		//		});
		//		pmc_drawinfo.addPActionListener(new PActionListener(){
		//			public void actionPerformed(PInputEvent e){
		//				CoVisWindow.this.buffer.viewcontroller.cblist.get("drawInfo").setSelected(!CoVisWindow.this.buffer.viewcontroller.cblist.get("drawInfo").isSelected());
		//			}
		//		});
		//		pmc_label.addPActionListener(new PActionListener(){
		//			public void actionPerformed(PInputEvent e){
		//				CoVisWindow.this.buffer.viewcontroller.showLabelButton.doClick();
		//				CoVisWindow.this.fitAndZoomHome(1);
		//			}
		//		});


		coversTransparencyControl = new CoversTransparencyControl(this);

		getCanvas().getCamera().addInputEventListener(new PBasicInputEventHandler() {
			public void mouseMoved(PInputEvent event) {
				updateToolTip(event);
			}

			public void mouseDragged(PInputEvent event) {
				updateToolTip(event);
			}

			public void updateToolTip(PInputEvent event) {
				if (showToolTip && !event.isShiftDown()) {
					PNode n = event.getInputManager().getMouseOver().getPickedNode();
					if (n.getAttribute("tooltip") != null){
						Object o = n.getAttribute("tooltip");
						if (o instanceof ToolTipProvider) {
							PNode newTTN = ((ToolTipProvider)o).getToolTipNode();
							if (tooltipNode != newTTN){
								if (tooltipNode != null) tooltipNode.removeFromParent();
								tooltipNode = newTTN;
								if (tooltipNode != null) getCanvas().getCamera().addChild(tooltipNode);
							}
							Point2D p = event.getCanvasPosition();
							//							if (tooltipNode != null) tooltipNode.setOffset(p.getX()+25, p.getY()+15);
							if (tooltipNode != null) {
								if (tooltipNode instanceof Covis_Object) tooltipNode.setOffset(p.getX()+10, p.getY()+10);
								else tooltipNode.setOffset(p.getX()+25, p.getY()+15);
							}
							if (tooltipNode != null) tooltipNode.setVisible(true);
						}
					} else {
						if (tooltipNode != null){
							tooltipNode.removeFromParent();
							tooltipNode = null;
						}
					}
					checkIfCoverIsNecessary(); //カバーシートはズームしすぎたら邪魔になるので消す
				}
			}
		});

		//		// TODO: バッファを切り替えたときに、全体画面に戻る（要カスタマイズ？）
		//		getCanvas().getCamera().animateViewToCenterBounds(buffer.grayBackSheet.getBounds(), true, 0);

		//		fitAndZoomHome(1); //最初のレイアウト
		// TODO: ウィンドウ初期化時のごたごたを隠すため、ぼやっと表示(ただし、bufferの初期化が済んでいるときは除く)
		//		if (buffer.layer.getTransparency() == 0.0f) {
		//			buffer.layer.animateToTransparency(0.0f, 100);
		buffer.layer.animateToTransparency(1.0f, 1200);
		//		getCanvas().getLayer().addChild(buffer.virtualCamera);
		getCanvas().getCamera().setViewBounds(new Rectangle2D.Float(-10,-10,20,20));
		getCanvas().getCamera().setViewScale(0.0001);
		//		setViewBounds(new Rectangle2D.Float(0,0,10000,10000));

		
// なぜかものすごく重くなるので，自前のスレッドで処理することにした 2016/8/31
		//		repaintaction = new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				checkrepaint();
//			}
//		};
		//		Timer t = new Timer(REPAINTINTERVAL, repaintaction);
		//		t.start();
		repaintcheckthread = new Thread(this);
		repaintcheckthread.start();
		ready = true;
		modenode = new PText("Select mode");
		modenode.setScale(2.0);

	}

	@Override
	public void run() {
		while(repaintcheckthread != null){
			try {
				Thread.sleep(REPAINTINTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			checkrepaint();
		}
	}


	/**
	 * MyPCanvas の MouseWheelRotationListener に登録してあれば，ホイール回転時に情報がくる
	 */
	public void mouseWheelRotated(float f) {
		checkIfCoverIsNecessary();
	}
	/**
	 * カバーシートはズームしすぎたら邪魔になるので消す
	 */
	public void checkIfCoverIsNecessary(){
		//		if (getCanvas().getCamera().getViewBounds().getHeight() > buffer.covisProperty.stsizey*4) {
		//			coversTransparencyControl.transparencyThread_Start(1);
		//		} else {
		//			coversTransparencyControl.transparencyThread_Start(0);
		//		}
	}


	public void initializeMenu(){
		super.initializeMenu();
		//		JMenuItem up = new JMenuItem("更新");
		//		up.addActionListener(new ActionListener(){
		//			public void actionPerformed(ActionEvent e){
		//				if (buffer != null) buffer.updateFromDB();
		//			}
		//		});
		//		viewMenu.add(up);

		//		updateMenu = new JCheckBoxMenuItem(); //外枠だけ作成しておく（どうせSwitchBufferでActionを設定するので）
		//
		//		viewMenu.add(updateMenu);
		menuBar.revalidate();
		menuBar.repaint();
	}

	public void checkrepaint() {
		if (dirty)
			getCanvas().repaint();
		dirty = false;
	}

	public void zoomHomePane(int animsec) {
		PBounds pb = buffer.layer.getFullBounds();
		pb = BUtil.zoomBounds(pb, 1.05);
		getCanvas().getCamera().animateViewToCenterBounds(pb, true, animsec);
	}
	public void zoomPane(int d) {
		PCamera pc = getCanvas().getCamera();
		PBounds pb = pc.getViewBounds();
		float f = 1.0f - (0.08f * d);
		pb = BUtil.zoomBounds(pb, f);
		pc.animateViewToCenterBounds(pb, true, 0);
	}
	public void fitLayout(int animsec) {
	}
	public void fitAndZoomHome(int animsec){
		zoomHomePane(animsec);
	}
	public void zoomBounds(PBounds pb, float f){
		PCamera pc = getCanvas().getCamera();
		pb = BUtil.zoomBounds(pb, f);
		pc.animateViewToCenterBounds(pb, true, 0);
	}
	public void initZoom(int i) {
	}

	public void showFadingMessage(String s, Color bc, Color fc, float scale, float trans){
		PText pt = new PText(s);
		pt.setScale(scale);
		pt.setTransparency(trans);
		pt.setPaint(bc);
		pt.setTextPaint(fc);

		double yplus = getCanvas().getCamera().getBoundsReference().getHeight()*3/4;
		for(FadeTimer f : messageFadeTimersQueue){
			if (yplus < (f.pn.getOffset().getY() + f.pn.getHeight() * f.pn.getScale() + 4))
				yplus = f.pn.getOffset().getY() + f.pn.getHeight() * f.pn.getScale() + 4;
		}
		getCanvas().getCamera().addChild(pt);
		pt.setOffset((getCanvas().getCamera().getBoundsReference().getWidth()-pt.getWidth()*pt.getScale())/2,yplus);

		FadeTimer ft = new FadeTimer(pt, 5000,2000);
		messageFadeTimersQueue.add(ft);
		ft.setFadeTimers(messageFadeTimersQueue);

		if (yplus + pt.getHeight()* pt.getScale() + 4> getCanvas().getCamera().getBoundsReference().getHeight()){
			for(FadeTimer f : messageFadeTimersQueue){
				f.pn.offset(0, getCanvas().getCamera().getBoundsReference().getHeight()-yplus - pt.getHeight()* pt.getScale() - 4);
			}
		}
	}

}
