package jaist.css.covis.cls;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Informer implements Runnable {
	//	public static void send(String to, String subject, String mes){
	//	try {
	//	Properties props = System.getProperties();
	//	// SMTP�T�[�o�[�̃A�h���X���w��
	//	props.put("mail.smtp.host","moto.qee.jp");
	//	Session session=Session.getDefaultInstance(props,null);
	//	MimeMessage mimeMessage=new MimeMessage(session);
	//	// ���M�����[���A�h���X�Ƒ��M�Җ����w��
	//	mimeMessage.setFrom(new InternetAddress("icluster@moto.qee.jp","�m�I�N���X�^","iso-2022-jp"));
	//	// ���M�惁�[���A�h���X���w��
	//	mimeMessage.setRecipients(Message.RecipientType.TO, to);
	//	// ���[���̃^�C�g�����w��
	//	mimeMessage.setSubject(subject,"iso-2022-jp");
	//	// ���[���̓��e���w��
	//	mimeMessage.setText(mes,"iso-2022-jp");
	//	// ���[���̌`�����w��
	//	mimeMessage.setHeader("Content-Type","text/plain; charset=iso-2022-jp");
	//	// ���M���t���w��
	//	mimeMessage.setSentDate(new Date());
	//	mimeMessage.setHeader("Content-Transfer-Encoding", "7bit");
	//	// ���M���܂�
	//	Transport.send(mimeMessage);
	//	} catch (Exception e) {
	//	e.printStackTrace();
	//	}	
	//	}
	String file;
	static Hashtable<String,Informer> hash = new Hashtable<String, Informer>();
	public static boolean canPlaySound = true;
//	public static boolean isSignedJAR = false;

	public Informer(String s){
		file = s;
		try {
			preload();
		} catch (Exception e) {
			canPlaySound = false;
			e.printStackTrace();
		}
	}
	public void play(){
		Thread t = new Thread(this);
		t.start();
	}

	public static Hashtable<String,AudioClip> soundCache = new Hashtable<String, AudioClip>();
	public static void playSound2(String file){
		if (!canPlaySound) return;
		AudioClip pong = soundCache.get(file);
		if (pong == null){
			// �T�E���h�����[�h
			pong = Applet.newAudioClip(Informer.class.getResource(file));
			soundCache.put(file, pong);
		}
		pong.play();
	}
	public static Informer informer;

	public static void playSound(String file){
		if (!canPlaySound) return;
		if (hash.containsKey(file)){
			Informer inf = hash.get(file);
			inf.play();
		} else {
			Informer inf = new Informer(file);
			hash.put(file, inf);
			inf.play();
		}
		//				informer.playSoundJARFile(file);
	}


	public byte[] path2byte(String path) {
		File f = new File(path);
		long fsize = f.length();

		ByteArrayOutputStream varyBuf = new ByteArrayOutputStream((int) fsize);
		InputStream in = null;
		int b;
		try {
			in = new BufferedInputStream(audioInputStream);
			final int LS = 1024;
			byte buf[] = new byte[LS];

			while ((b = in.read(buf, 0, buf.length)) != -1) {
				varyBuf.write(buf, 0, b);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
		return varyBuf.toByteArray();
	}

	InputStream is;
	AudioInputStream audioInputStream;
	AudioFormat audioFormat;
	DataLine.Info info;
	SourceDataLine line;
	byte[] wav;
	private void preload() throws Exception {
		try{
			is = getClass().getResourceAsStream("/"+file);
			try {
				audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			audioFormat = audioInputStream.getFormat();
		} catch(NullPointerException np){
			try {
				System.out.println("Second try");
				audioInputStream = AudioSystem.getAudioInputStream(new File("res/"+file));
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// �I�[�f�B�I�`�����擾���܂�
			audioFormat = audioInputStream.getFormat();
		}
		// �f�[�^���C���̏��I�u�W�F�N�g�𐶐����܂�
		info = new DataLine.Info(SourceDataLine.class, audioFormat);
		// �w�肳�ꂽ�f�[�^���C�����Ɉ�v���郉�C�����擾���܂�
		try {
			line = (SourceDataLine) AudioSystem.getLine(info);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		wav = path2byte(file);

	}

	public void run() {
		try {
			// �w�肳�ꂽ�I�[�f�B�I�`���Ń��C�����J���܂�
			if (!canPlaySound) return;
			if (line == null) return;
			line.open(audioFormat);
			// ���C���ł̃f�[�^���o�͂��\�ɂ��܂�
			line.start();

			line.write(wav, 0, wav.length);

			// ���C������L���[�ɓ����Ă���f�[�^��r�o���܂�
			line.drain();
			// ���C������܂�
			line.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//	public void run2() {
	//		final int EXTERNAL_BUFFER_SIZE = 128000;
	//		try {
	//			// �w�肳�ꂽ�I�[�f�B�I�`���Ń��C�����J���܂�
	//			line.open(audioFormat);
	//			// ���C���ł̃f�[�^���o�͂��\�ɂ��܂�
	//			line.start();
	//
	//			int nBytesRead = 0;
	//			byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];
	//			while (nBytesRead != -1) {
	//				// �I�[�f�B�I�X�g���[������f�[�^��ǂݍ��݂܂�
	//				nBytesRead = audioInputStream.read(abData, 0, abData.length);
	//				if (nBytesRead >= 0) {
	//					// �I�[�f�B�I�f�[�^���~�L�T�[�ɏ������݂܂�
	//					int nBytesWritten = line.write(abData, 0, nBytesRead);
	//				}
	//			}
	//			// ���C������L���[�ɓ����Ă���f�[�^��r�o���܂�
	//			line.drain();
	//			// ���C������܂�
	//			line.close();
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		}
	//	}
}
