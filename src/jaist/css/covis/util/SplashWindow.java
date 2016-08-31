package jaist.css.covis.util;

/**
 * �N�����̃X�v���b�V���E�B���h�E
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
 * SplashWindowSample �N���X�́C�G���g���|�C���g���܂ރN���X�ł���C���C���E�B���h�E�ƂȂ�t���[���ł���D
 */
@SuppressWarnings("serial")
class SplashWindowSample extends JFrame {

	/** �R���X�g���N�^ */
	public SplashWindowSample() {
		super("�X�v���b�V���E�B���h�E�̃T���v��");
		setSize(400, 150);
	}

	/** �`�� */
	public void paint(Graphics g) {
		super.paint(g);
		g.drawString("�X�v���b�V���E�B���h�E�̃T���v��", 120, 80);
	}

	/** �G���g���|�C���g */
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
 * SplashWindow �N���X�́C��莞�ԃX�v���b�V���E�B���h�E��\��������C�����������ă��C���E�B���h�E��\��������N���X�ł���D
 */
@SuppressWarnings("serial")
public class SplashWindow extends JWindow {

	public static boolean isShownOnce = false;

	/** ���C���E�B���h�E */
	private Component mainWindow;

	/** �C���[�W */
	private Image image;

	/** �C���[�W�̃T�C�Y */
	private Dimension imageSize = new Dimension();

	/** �R���X�g���N�^ */
	public SplashWindow(Component mainWindow) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		MediaTracker mt = new MediaTracker(this);

		this.mainWindow = mainWindow;

		// �C���[�W�̓ǂݍ���
		image = Toolkit.getDefaultToolkit().getImage("atnlogo.png");
		mt.addImage(image, 0);
		try {
			mt.waitForAll();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		imageSize.width = image.getWidth(this);
		imageSize.height = image.getHeight(this);

		// �E�B���h�E�̃T�C�Y�ƈʒu�̐ݒ�
		setBounds((screenSize.width - imageSize.width) / 2,
				(screenSize.height - imageSize.height) / 2, imageSize.width,
				imageSize.height);
	}

	/** �X�v���b�V���E�B���h�E�̋@�\ */
	public void doSplash() {
		Thread switchingThread = new SwitchingThread();
		// �X�v���b�V���E�B���h�E��ON
		if (!isShownOnce) {
			setVisible(true);
			isShownOnce = true;
			// �؂�ւ��X���b�h�̃X�^�[�g
			switchingThread.start();
		} else {
			mainWindow.validate();
			mainWindow.setVisible(true);
		}
	}

	/** �`�� */
	public void paint(Graphics g) {
		g.drawImage(image, 0, 0, imageSize.width, imageSize.height, this);
	}

	/** ��莞�ԂŃ��C���E�B���h�E�Ƃ̕\����؂�ւ��邽�߂̃X���b�h */
	class SwitchingThread extends Thread {
		public void run() {
			// 1.2�b�҂�
			try {
				sleep(1200);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			// �X�v���b�V���E�B���h�E��OFF
			setVisible(false);
			// ���C���E�B���h�E��ON
			mainWindow.validate();
			mainWindow.setVisible(true);
		}
	}
}
