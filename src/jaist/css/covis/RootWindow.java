package jaist.css.covis;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.util.ArrayList;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.event.PInputEventFilter;
import edu.umd.cs.piccolo.util.PBounds;
import jaist.css.covis.cls.ClassFieldMenu;
import jaist.css.covis.util.MyPCanvas;
import jaist.css.covis.util.MyPFrame;

/**
 * ATNWindowのスーパークラス．フレーム(MyPFrame)，キャンバス(MyPCanvas)を持つ．
 * ちなみに，カメラはキャンバスがデフォルトで１つ備えている．
 * 
 * ATNBufferを関連付けて表示するには，ATNRootBufferのaddCamera(PCamera)を呼び出す．
 * この処理は switchBuffer() で行っている．
 * 
 * @author miuramo
 *
 */
public class RootWindow implements ActionListener, WindowStateListener {
	public static ArrayList<RootWindow> windows = new ArrayList<RootWindow>();

	public PInputEventFilter b1mask = new PInputEventFilter(InputEvent.BUTTON1_MASK);

	public PInputEventFilter b2mask = new PInputEventFilter(InputEvent.BUTTON2_MASK);

	public PInputEventFilter b3mask = new PInputEventFilter(InputEvent.BUTTON3_MASK);

	public PInputEventFilter disablemask = new PInputEventFilter(0);

	public MyPFrame frame;
	public MyPCanvas canvas;

	public CoVisBuffer buffer;
	public JMenuBar menuBar;
	public JMenu fileMenu;
	public JMenu extraMenu;
	public JMenu windowMenu;
	public JMenu bufferMenu;


	public SrcWindow srcWindow;

	// あまり聞いてこないモード
	public JCheckBoxMenuItem isAutoMode;
	// あまり聞いてこないモード
	public JCheckBoxMenuItem isAutoMode_obj;
	//継承のクラスを表示するモード
	public JCheckBoxMenuItem isAdvancedMode;


	//	public JCheckBoxMenuItem updateMenu;

	public static int windowid;

	public RootWindow(){
		CoVisBuffer b = new CoVisBuffer();
		constructFrame();
		switchBuffer(b);
		frame.setVisible(true);
	}
	public RootWindow(CoVisBuffer b){
		buffer = b;
		constructFrame();
		switchBuffer(b);
		frame.setSize(b.covisProperty.viewsizex,b.covisProperty.viewsizey);
		frame.setLocation(b.covisProperty.viewlocx, b.covisProperty.viewlocy);
		frame.validate();
		frame.addWindowStateListener(this);
		frame.setVisible(true);

		RootWindow.this.showSrcWin(true); // TODO:段階がすすんだら，起動時に見せるようにする
	}
	public void constructFrame(){
		frame = new MyPFrame(AnchorGarden.WINDOWTITLE+" "+String.valueOf(windowid), false, null);
		RootWindow.windowid++;
		windows.add(this);

		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowListener(){
			public void windowOpened(WindowEvent arg0) {
				//								System.out.println("open");
				//								RootWindow.this.showSrcWin(true); // TODO:段階がすすんだら，起動時に見せるようにする
			}
			public void windowClosing(WindowEvent arg0) {
				tobeclosed();
			}
			public void windowClosed(WindowEvent arg0) {}
			public void windowIconified(WindowEvent arg0) {
				if (srcWindow != null){
					srcWindow.setState(JFrame.ICONIFIED);
				}
			}
			public void windowDeiconified(WindowEvent arg0) {
				if (srcWindow != null){
					//					srcWindow.setVisible(true);
					srcWindow.setState(JFrame.NORMAL);
					srcWindow.follow();
				}
			}
			public void windowActivated(WindowEvent arg0) {
				if (srcWindow != null && srcWindow.getState()!=JFrame.ICONIFIED){
					//					srcWindow.setVisible(true);
					srcWindow.setState(JFrame.NORMAL);
					srcWindow.follow();
				}
			}
			public void windowDeactivated(WindowEvent arg0) {
			}
		});
		frame.addComponentListener(new ComponentListener(){
			@Override
			public void componentHidden(ComponentEvent arg0) {	}
			@Override
			public void componentMoved(ComponentEvent arg0) {
				if (srcWindow != null && srcWindow.getState()!=JFrame.ICONIFIED){
					srcWindow.follow();
				}
			}
			@Override
			public void componentResized(ComponentEvent arg0) {	
				if (srcWindow != null && srcWindow.getState()!=JFrame.ICONIFIED){
					srcWindow.follow();
				}
			}
			@Override
			public void componentShown(ComponentEvent arg0) {			}
		});

		initializeMenu();
	}
	protected void initializeMenu(){
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		JMenuItem resetWorld = new JMenuItem("Reset World");
		resetWorld.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				resetWorld();
			}		
		});
		fileMenu.add(resetWorld);


		JMenuItem newBuffer = new JMenuItem("new "+RootBuffer.bufferName);
		newBuffer.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				newBuffer();
			}		
		});
		fileMenu.add(newBuffer);

		JMenuItem newFrame = new JMenuItem("new Window");
		newFrame.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				newFrame();
			}		
		});
		fileMenu.add(newFrame);

		fileMenu.addSeparator();

		//		JMenuItem printpreview = new JMenuItem("Print Preview (Portlait)");
		//		printpreview.addActionListener(new ActionListener(){
		//			public void actionPerformed(ActionEvent arg0) {
		////				canvas.setPreferredSize(new Dimension(584,816));
		//				frame.setSize(592,876);
		//				frame.validate();
		//			}		
		//		});
		//		fileMenu.add(printpreview);

		JMenuItem printpreview2 = new JMenuItem("Print Preview (Landscape)");
		printpreview2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				//				canvas.setPreferredSize(new Dimension(584,816));
				frame.setSize(876,592);
				frame.validate();
			}		
		});
		fileMenu.add(printpreview2);

		//		JMenuItem printpreview3 = new JMenuItem("set preview frame (A0 Landscape)");
		//		printpreview3.addActionListener(new ActionListener(){
		//		public void actionPerformed(ActionEvent arg0) {
		////		canvas.setPreferredSize(new Dimension(584,816));
		//		frame.setSize(3370+10,2384+60);
		//		frame.validate();
		//		}		
		//		});
		//		fileMenu.add(printpreview3);

//		JMenuItem print = new JMenuItem("Print");
//		print.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent arg0) {
////				PrintPrintable.print(canvas.cam);
//			}		
//		});
//		fileMenu.add(print);

//		JMenuItem printPDF = new JMenuItem("PDF Print");
//		printPDF.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent e){
//				//					lastGraphView.display.setHighQuality(true);
//				((AnchorGarden)RootWindow.this).pmenubar.mybar.setVisible(false);
//				printPDF(canvas, "A4L", "ag2.pdf");
//				openPDF("ag2.pdf");
//				((AnchorGarden)RootWindow.this).pmenubar.mybar.setVisible(true);
//			}
//
//		});
//		fileMenu.add(printPDF);


		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if (JOptionPane.showConfirmDialog(RootWindow.this.frame, "本当に全部終了してよろしいですか？", "確認", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
					System.exit(0);
				}
			}		
		});
		fileMenu.add(exit);

		windowMenu = new JMenu("Window");

		JMenuItem fit = new JMenuItem("Fit");
		fit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				canvas.cam.animateViewToCenterBounds(
						buffer.layer.getUnionOfChildrenBounds(new PBounds()),
						true, 1000);
			}		
		});
		windowMenu.add(fit);

		final JCheckBoxMenuItem full = new JCheckBoxMenuItem("Full Screen");
		full.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent arg0) {
				frame.setFullScreenMode(full.isSelected());			
			}
		});
		windowMenu.add(full);

		extraMenu = new JMenu("Extra");
		menuBar.add(extraMenu);


		isAutoMode = new JCheckBoxMenuItem("Automatic Variable Name");
		isAutoMode.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent arg0){
			}
		});
		isAutoMode.setSelected(true);
		//		isAutoMode.setSelected(false);
		extraMenu.add(isAutoMode);


		isAutoMode_obj = new JCheckBoxMenuItem("Automatic Constructor");
		isAutoMode_obj.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent arg0){
			}
		});
		isAutoMode_obj.setSelected(true);
		//		isAutoMode.setSelected(false);
		extraMenu.add(isAutoMode_obj);

		final JMenuItem srcWin = new JMenuItem("Source Code");
		srcWin.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				showSrcWin(true);
			}
		});
		extraMenu.add(srcWin);

		extraMenu.addSeparator();

		JMenuItem[] modeMI = new JMenuItem[ClassFieldMenu.modes.length];
		for(int i=0;i<ClassFieldMenu.modes.length;i++){
			modeMI[i] = new JMenuItem(ClassFieldMenu.modes[i]);
			modeMI[i].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					String act = e.getActionCommand();
					for(int i=0;i<ClassFieldMenu.modes.length;i++){
						if (act.equals(ClassFieldMenu.modes[i])){
							buffer.showAdvancedClasses(i);
						}
					}
				}
			});
			extraMenu.add(modeMI[i]);
		}


		// 		isAdvancedMode = new JCheckBoxMenuItem("Advanced Mode (add extended classes)");
		//		isAdvancedMode.addItemListener(new ItemListener(){
		//			public void itemStateChanged(ItemEvent arg0){
		//				buffer.showAdvancedClasses(isAdvancedMode.isSelected());
		//			}
		//		});
		//		isAdvancedMode.setSelected(false);
		//		extraMenu.add(isAdvancedMode);

		//		final JMenuItem addVarField = new JMenuItem("Add Variable Field");
		//		addVarField.addActionListener(new ActionListener(){
		//			public void actionPerformed(ActionEvent arg0) {				
		//				buffer.addVariableField();
		//			}
		//		});
		//		extraMenu.add(addVarField);


		//		final JMenuItem srcWintest = new JMenuItem("Source test");
		//		srcWintest.addActionListener(new ActionListener(){
		//		public void actionPerformed(ActionEvent arg0) {
		//		if (srcWindow != null){
		//		System.out.println(srcWindow.lastLine());
		//		}
		//		}
		//		});
		//		windowMenu.add(srcWintest);

		menuBar.add(windowMenu);

		bufferMenu = new JMenu(RootBuffer.bufferName+"s");
		updateBufferMenu();
		menuBar.add(bufferMenu);

		frame.setJMenuBar(menuBar);

		JMenu helpMenu = new JMenu("Help");
		JMenuItem about = new JMenuItem("About");
		about.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				//				JOptionPane.showMessageDialog(frame, CoVisWindow.WINDOWTITLE+" ver 1.80\n\n(c)2008 Motoki Miura\n\nhttp://css.jaist.ac.jp/~miuramo/","About",JOptionPane.INFORMATION_MESSAGE);
				JOptionPane.showMessageDialog(frame, AnchorGarden.WINDOWTITLE+" ver 1.99\n\n(c)2008-2011 AnchorGarden\n\nhttp://ist.mns.kyutech.ac.jp/miura/anchorgarden/","About",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		helpMenu.add(about);
		menuBar.add(helpMenu);
	}

	public void updateBufferMenu(){
		bufferMenu.removeAll();
		for(RootBuffer b: RootBuffer.buffers){
			JMenuItem jm = new JMenuItem(b.name);
			jm.addActionListener(this);
			bufferMenu.add(jm);
		}
		menuBar.revalidate();
		menuBar.repaint();
	}

	/** 
	 * EventListener for switching Buffer
	 */
	public void actionPerformed(ActionEvent arg0) {
		CoVisBuffer b = CoVisBuffer.find(arg0.getActionCommand());
		if (b != null) switchBuffer(b);
	}	

	/**
	 * このウィンドウが表示するバッファを切り替える
	 * @param b 切り替え先のバッファ
	 */
	public void switchBuffer(CoVisBuffer b){
		// Bufferの自動更新設定が切り替わったときに，Windowのメニューアイテムを更新して
		// もらうように登録していたが，それを解除する．★
		//		buffer.updateStateAction.unregisterCheckBoxMenuItem(updateMenu);
		buffer.removeShowingWindow((AnchorGarden)this);
		PCamera cam = new PCamera();
		b.addCamera(cam);

		canvas = frame.getCanvas();
		canvas.setCamera(cam);
		canvas.initialize(); // ホイールズームイベントリスナの追加
		canvas.setWheelRotationDirection(buffer.covisProperty.wheelrotationdirection);
		canvas.setMouseWheelRotationListener((AnchorGarden)this);
		canvas.setBackground(Color.lightGray);

		buffer = b; // ここでバッファへの参照が切り替わる

		initialize();
		//		frame.setTitle("TMRGWindow "+String.valueOf(windowid)+" ("+buffer.name+")"); //ウィンドウタイトルバー更新
		frame.setTitle(AnchorGarden.WINDOWTITLE+" ("+buffer.name+")"); //ウィンドウタイトルバー更新
		((AnchorGarden)this).pmenubar.manualFit(cam.getBoundsReference()); //右上の黄色いメニューをどうしてもぴったり合わせたい

		// 継続更新チェックボックスメニューアイテムの切り替え
		//		updateMenu.setAction(buffer.updateStateAction);
		//		updateMenu.setSelected(buffer.updateStateAction.isSelected());

		//		Bufferの自動更新設定が切り替わったときに，Windowのメニューアイテムを更新して
		//		もらうように登録する．★
		//		buffer.updateStateAction.registerJCheckBoxMenuItem(updateMenu);
		buffer.addShowingWindow((AnchorGarden)this);

		//		buffer.clsFieldInitTimer.setDelay(500);
		buffer.createInitTimer();
	}

	/**
	 * GKJWindowでオーバーライドされる．（消すと動かなくなるので注意）
	 *
	 */
	public void initialize(){

	}

	public void tobeclosed(){
		if (windows.size()==1){
			if (JOptionPane.showConfirmDialog(this.frame,"Really Exit?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
//				buffer.addLogNow(1, "Exit");
				System.exit(0);
			} else {

			}
		} else {
			windows.remove(this);
			this.frame.setVisible(false);
		}
	}

	public void newFrame(){
		new AnchorGarden((CoVisBuffer)this.buffer);
	}

	public void newBuffer(){
		Thread t = new Thread(){
			public void run(){
				CoVisProperty sprop = CoVisBuffer.loadProperty();
				new AnchorGarden(new CoVisBuffer(sprop));				
			}
		};
		t.start();
	}
	//TODO: showSrcWin
	public void showSrcWin(boolean visible){
		if (srcWindow == null){
			srcWindow = new SrcWindow(buffer, (AnchorGarden)RootWindow.this);
		} else if (srcWindow.getState()==JFrame.ICONIFIED){
			srcWindow.setState(JFrame.NORMAL);
		}

		//		srcWindow.setLocation((int)(srcWindow.window.frame.getLocation().getX()+srcWindow.window.frame.getWidth()),(int)(srcWindow.window.frame.getLocation().getY()));
		//		srcWindow.setSize((int)(srcWindow.getWidth()),(int)(srcWindow.window.frame.getHeight()));
		srcWindow.setVisible(visible);
	}
	public void showSrcWin() {
		showSrcWin(true);
		//		if (srcWindow == null) showSrcWin(true);
		//		else {
		//			srcWindow.dispose();
		//			srcWindow = null;
		//		}
	}

	public void resetWorld(){
		buffer.resetWorld();
		srcWindow.jta.setText("");
//		buffer.addLog(1,"resetWorld");
	}
	@Override
	public void windowStateChanged(WindowEvent arg0) {
		displayStateMessage("", arg0);
	}
	String newline = "\n";
	String space = " ";
	//ウィンドウの最大化をしたら
	void displayStateMessage(String prefix, WindowEvent e) {
		int state = e.getNewState();
		int oldState = e.getOldState();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		if (state == Frame.MAXIMIZED_BOTH && oldState == Frame.NORMAL){
			//        	Dimension s = frame.getSize();
			if (srcWindow != null){
				// 3:2 で表示する
				int framewidth = (int)(dim.getWidth()*3/5);
				int srcwinwidth = (int)(dim.getWidth()*2/5);
				frame.setSize(framewidth, (int)dim.getHeight()-50);
				srcWindow.setSize(srcwinwidth-55, (int)(dim.getHeight()-50) );
				if (srcWindow.doFollowSrcWinLeft.isSelected()){
					frame.setLocation(srcwinwidth-55,0);
				} else {
					frame.setLocation(0,0);
				}
				showSrcWin();
			}
		}
		//        String msg = prefix
		//                   + newline + space
		//                   + "New state: "
		//                   + convertStateToString(state)
		//                   + newline + space
		//                   + "Old state: "
		//                   + convertStateToString(oldState);
		//        displayMessage(msg);
	}
	void displayMessage(String msg) {
		System.out.println(msg);
	}
	String convertStateToString(int state) {
		if (state == Frame.NORMAL) {
			return "NORMAL";
		}
		String strState = " ";
		if ((state & Frame.ICONIFIED) != 0) {
			strState += "ICONIFIED";
		}
		//MAXIMIZED_BOTH is a concatenation of two bits, so
		//we need to test for an exact match.
		if ((state & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH) {
			strState += "MAXIMIZED_BOTH";
		} else {
			if ((state & Frame.MAXIMIZED_VERT) != 0) {
				strState += "MAXIMIZED_VERT";
			}
			if ((state & Frame.MAXIMIZED_HORIZ) != 0) {
				strState += "MAXIMIZED_HORIZ";
			}
			if (" ".equals(strState)){
				strState = "UNKNOWN";
			}
		}
		return strState.trim();
	}
	void openPDF(String path){
		Runtime runtime = Runtime.getRuntime();
		String os = System.getProperty("os.name");

		try {
			if (os.startsWith("Windows"))
				runtime.exec("cmd.exe /c start " + path+"");
			else if (os.startsWith("Mac OS")){
				runtime.exec("open " + path + "");
			} else {
				runtime.exec("acroread "+path);
			}
		}
		catch(java.io.IOException ex){
			ex.printStackTrace();		
		}
	}
//	public void printPDF(PCanvas canvas, String pagesizeString, String outfilename) {
//		Hashtable<String,Rectangle> paperSizeHash = new Hashtable<String,Rectangle>();
//		paperSizeHash.put("A0L", PageSize.A0.rotate());
//		paperSizeHash.put("A1L", PageSize.A1.rotate());
//		paperSizeHash.put("A2L", PageSize.A2.rotate());
//		paperSizeHash.put("A3L", PageSize.A3.rotate());
//		paperSizeHash.put("A4L", PageSize.A4.rotate());
//		paperSizeHash.put("A0P", PageSize.A0);
//		paperSizeHash.put("A1P", PageSize.A1);
//		paperSizeHash.put("A2P", PageSize.A2);
//		paperSizeHash.put("A3P", PageSize.A3);
//		paperSizeHash.put("A4P", PageSize.A4);
//		paperSizeHash.put("A0", PageSize.A0);
//		paperSizeHash.put("A1", PageSize.A1);
//		paperSizeHash.put("A2", PageSize.A2);
//		paperSizeHash.put("A3", PageSize.A3);
//		paperSizeHash.put("A4", PageSize.A4);
//
//		Document document = new Document(new Rectangle(0,0,canvas.getWidth(),canvas.getHeight()), 0, 0, 0, 0);
//		System.out.println("f "+frame.getWidth()+" "+frame.getHeight());
//		System.out.println("c "+canvas.getWidth()+" "+canvas.getHeight());
//		//		Document document = new Document(new Rectangle(0,0,frame.getWidth()-16,frame.getHeight()-60), 0, 0, 0, 0);
//		//		step 2: creation of the writer
//		PdfWriter writer = null;
//		try {
//			writer = PdfWriter.getInstance(document, new FileOutputStream(outfilename));
//			//			writer.setPdfVersion(PdfWriter.VERSION_1_7);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (DocumentException e) {
//			e.printStackTrace();
//		}
//
//		// step 3: we open the document
//		document.open();
//
//		// step 4: we grab the ContentByte and do some stuff with it
//
//		// we create a fontMapper and read all the fonts in the font directory
//		DefaultFontMapper mapper = new DefaultFontMapper();
//		FontFactory.registerDirectories();
//		mapper.insertDirectory("c:\\windows\\fonts");
//		// we create a template and a Graphics2D object that corresponds with it
//		PdfContentByte cb = writer.getDirectContent();
//
//		Graphics2D g2 = cb.createGraphicsShapes(canvas.getWidth(), canvas.getHeight());
//
//		//		System.out.println(pagesizeString+" "+paperSizeHash.get(pagesizeString).getWidth()+" "+paperSizeHash.get(pagesizeString).getHeight());
//		//		canvas.paintDisplay(g2, new Dimension(3000,2000));
//		canvas.paint(g2);
//
//		g2.dispose();
//
//		// step 5: we close the document
//		document.close();
//	}

}


