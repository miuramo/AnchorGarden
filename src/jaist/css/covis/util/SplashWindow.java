package jaist.css.covis.util;

/**
 * 起動時のスプラッシュウィンドウ
 */

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JWindow;

/**
 * SplashWindowSample クラスは，エントリポイントを含むクラスであり，メインウィンドウとなるフレームである．
 */
@SuppressWarnings("serial")
class SplashWindowSample extends JFrame {

	/** コンストラクタ */
	public SplashWindowSample() {
		super("スプラッシュウィンドウのサンプル");
		setSize(400, 150);
	}

	/** 描画 */
	public void paint(Graphics g) {
		super.paint(g);
		g.drawString("スプラッシュウィンドウのサンプル", 120, 80);
	}

	/** エントリポイント */
	public static void main(String[] args) {
		JFrame frame = new SplashWindowSample();
		SplashWindow splashWindow = new SplashWindow(frame);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				System.exit(0);
			}
		});
		frame.setResizable(false);

		splashWindow.doSplash();
	}
}

/**
 * SplashWindow クラスは，一定時間スプラッシュウィンドウを表示した後，自分を消してメインウィンドウを表示させるクラスである．
 */
@SuppressWarnings("serial")
public class SplashWindow extends JWindow {

	public static boolean isShownOnce = false;

	/** メインウィンドウ */
	private Component mainWindow;

	/** イメージ */
	private Image image;

	/** イメージのサイズ */
	private Dimension imageSize = new Dimension();

	/** コンストラクタ */
	public SplashWindow(Component mainWindow) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		MediaTracker mt = new MediaTracker(this);

		this.mainWindow = mainWindow;

		// イメージの読み込み
		image = Toolkit.getDefaultToolkit().getImage("atnlogo.png");
		mt.addImage(image, 0);
		try {
			mt.waitForAll();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		imageSize.width = image.getWidth(this);
		imageSize.height = image.getHeight(this);

		// ウィンドウのサイズと位置の設定
		setBounds((screenSize.width - imageSize.width) / 2,
				(screenSize.height - imageSize.height) / 2, imageSize.width,
				imageSize.height);
	}

	/** スプラッシュウィンドウの機能 */
	public void doSplash() {
		Thread switchingThread = new SwitchingThread();
		// スプラッシュウィンドウをON
		if (!isShownOnce) {
			setVisible(true);
			isShownOnce = true;
			// 切り替えスレッドのスタート
			switchingThread.start();
		} else {
			mainWindow.validate();
			mainWindow.setVisible(true);
		}
	}

	/** 描画 */
	public void paint(Graphics g) {
		g.drawImage(image, 0, 0, imageSize.width, imageSize.height, this);
	}

	/** 一定時間でメインウィンドウとの表示を切り替えるためのスレッド */
	class SwitchingThread extends Thread {
		public void run() {
			// 1.2秒待つ
			try {
				sleep(1200);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			// スプラッシュウィンドウをOFF
			setVisible(false);
			// メインウィンドウをON
			mainWindow.validate();
			mainWindow.setVisible(true);
		}
	}
}
