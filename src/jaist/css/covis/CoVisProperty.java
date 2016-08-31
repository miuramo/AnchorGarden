package jaist.css.covis;

import jaist.css.covis.util.FileReadWriter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.StringTokenizer;

/**
 * �ݒ荀�ځi�v���p�e�B�j�I�u�W�F�N�g�D�R���X�g���N�^�Őݒ�t�@�C��(*.ssf)��ǂݍ��݁C�������g�̕ϐ��̃f�t�H���g�l�����t���N�V�����ɂ���ĕύX����D
 * @author miuramo
 *
 */
public class CoVisProperty {
	/**
	 * ���k�p�l���̍s���i���ɂ������ׂ邩�j
	 */
//	public int column = 7;
	/**
	 * ���k�p�l���̗񐔁i�c�ɂ������ׂ邩�j
	 */
//	public int row = 6;

	/**
	 * ���k�p�l���̕��s�N�Z����
	 */
//	public int stsizex = 700;
	/**
	 * ���k�p�l���̍����s�N�Z����
	 */
//	public int stsizey = 990;
	/**
	 * Web���J�p���k�p�l���̕��s�N�Z����
	 */
//	public int webstsizex = 500;
	/**
	 * Web���J�p���k�p�l���̍����s�N�Z����
	 */
//	public int webstsizey = 707;
	/**
	 * ���ׂ�Ƃ��̊Ԋu
	 */
//	public int space = 10;
	/**
	 * ATNWindow�\���̕�
	 */
	public int viewsizex = 570;
	/**
	 * ATNWindow�\���̍���
	 */
	public int viewsizey = 560;
	/**
	 * ATNWindow�\���̈ʒu�ix���W�j
	 */
	public int viewlocx = 420;
	/**
	 * ATNWindow�\���̈ʒu�iy���W�j
	 */
	public int viewlocy = 5;
	/**
	 * �ĕ`��̊Ԋu
	 */
//	public int repaintinterval = 1000;

	/**
	 * �M�L�k�����i�����g�y���̂Ƃ��́CA4�̉����𑜓x��3300�ŁCstsizex=600�������j
	 */
//	public float inkscale = 0.181818f; // 600/3300
	/**
	 * �}�E�X�`��̕M�L���CDB�ɕۑ�����Ƃ��ɕϊ����邽�߂Ɏg�p
	 */
//	public float inkscale700 = 0.212121f; // 700/3300

	/**
	 * �M�L�̐���
	 */
//	public float inkwidth = 1.0f;

	// public float imagewidth = 600.0f;
	// public float imageoffsetx = 0.0f;
	// public float imageoffsety = 10.0f;
	// for control panel
	/**
	 * �R���g���[���p�l���̐��k�{�^���̕�
	 */
//	public int cpstsizex = 30;
	/**
	 * �R���g���[���p�l���̐��k�{�^���̍���
	 */
//	public int cpstsizey = 20;
	/**
	 * �R���g���[���p�l���̐��k�{�^���̔z�u�Ԋu
	 */
//	public int cpspace = 2;
	/**
	 * �R���g���[���p�l���̐��k�{�^����z�u���鍶����W
	 */
//	public int cplocx = 0;
	/**
	 * �R���g���[���p�l���̐��k�{�^����z�u���鍶����W
	 */
//	public int cplocy = 0;

	/**
	 * �i���݂͖��g�p�j�����M�L�����܂�����ۑ����邩
	 */
//	public int maxstorestrokes = 1000000;
	/**
	 * �i���݂͖��g�p�jPDA����̃A�N�Z�X�p�CAirTransNote Server�|�[�g�ԍ�
	 */
//	public int snail_port = 10007;
	/**
	 * �f�o�b�O�p�̃��O��f���o�����ǂ���
	 * �i���݂́C�M�L�̈��͂�W���o�͂ɕ\������=true�j
	 */
//	public boolean printsendlog = false;

	/**
	 * �f�o�b�O�p�̐ڑ�����\�����邩�ǂ���
	 */
//	public boolean printscanline = false;
	/**
	 * �i���݂͖��g�p�j���k�̐ȏ��z�u
	 */
//	public String seatlayout = "";
	/**
	 * �N�����ɓǂݍ���Page��`�t�@�C��(*.ser)
	 * : �i�R�����j�ł������ĂP�`�S�y�[�W�܂Őݒ�ł���
	 * ��Fmath27-1.ser:math27-2.ser:math27-3.ser:pat2.ser
	 */
//	public String book_file = "";

	/**
	 * mysql�ڑ����[�U��
	 */
//	public String mysql_user = "root";
	/**
	 * mysql�ڑ��p�X���[�h
	 */
//	public String mysql_pass = "root";
	/**
	 * mysql�ڑ��z�X�g
	 */
//	public String mysql_host = "localhost";
	/**
	 * mysql�ڑ�DB��
	 */
//	public String mysql_db = "tomarigi_rfid";

//	public boolean enquete_mode = false;

//	public boolean dbconnect = true;

	/**
	 * ���Ȑݒ�t�@�C��
	 */
//	public String seatmap_file = "";

	/**
	 * �z�C�[����]�Y�[�������̐ݒ�(-1 �܂��� 1)
	 */
	public int wheelrotationdirection = -1;

	/**
	 * �ݒ�t�@�C����ǂݍ��񂾁C�v���p�e�B�I�u�W�F�N�g��Ԃ��܂�
	 * @param fname �ݒ�t�@�C��
	 */
	public CoVisProperty(String fname) {
		if (fname != null){
			String[] lines = FileReadWriter.getLines(fname);
			String line;
			StringTokenizer st2;
			for (int i = 0; i < lines.length; i++) {
				// System.out.println(lines[i]);
				int index = lines[i].indexOf(";");
				if (index > -1) {
					line = lines[i].substring(0, index);
				} else {
					line = lines[i];
				}
				// System.out.println(line);
				st2 = new StringTokenizer(line, "=");
				if (st2.countTokens() == 2) {
					String finame = st2.nextToken().trim();
					String val = st2.nextToken().trim();
					Object o = gen_primitive(this, finame, val);
					setter(this, finame, o);
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public static final Class getWrapperClass(Class primitiveClass) {
		Class wrapper = null;
		if (primitiveClass == Boolean.TYPE)
			wrapper = java.lang.Boolean.class;
		else if (primitiveClass == Character.TYPE)
			wrapper = java.lang.Character.class;
		else if (primitiveClass == Byte.TYPE)
			wrapper = java.lang.Byte.class;
		else if (primitiveClass == Short.TYPE)
			wrapper = java.lang.Short.class;
		else if (primitiveClass == Integer.TYPE)
			wrapper = java.lang.Integer.class;
		else if (primitiveClass == Long.TYPE)
			wrapper = java.lang.Long.class;
		else if (primitiveClass == Float.TYPE)
			wrapper = java.lang.Float.class;
		else if (primitiveClass == Double.TYPE)
			wrapper = java.lang.Double.class;
		else if (primitiveClass == java.lang.String.class)
			wrapper = java.lang.String.class;
		return wrapper;
	}

//	public void test() {
//	try {
//	// �N���X���擾
//	Class c = Class.forName("jaist.css.atn.ATNProperty");
//	// �A�N�Z�X�\�ȃt�B�[���h�����t���N�g����
//	Field[] field = c.getFields();
//	System.out.println("----- �A�N�Z�X�\�ȃt�B�[���h -------");
//	for (int i = 0; i < field.length; i++) {
//	String fieldname = field[i].toString();
//	System.out.print(fieldname + " ");
//	System.out.println(field[i].get(this).toString());
//	}
//	System.out.println();
//	} catch (Exception ex) {
//	ex.printStackTrace(System.out);
//	}
//	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object gen_primitive(Object obj, String fieldname,
			String value) {
		try {
			// (1) determine target wrapper class of primitive
			Class cthis = obj.getClass();
			Field f = cthis.getField(fieldname);
			// System.out.println(f.getType().toString());
			Class pclass = getWrapperClass(f.getType());
			// System.out.println(pclass.toString());
			Class[] pconstcarg = new Class[1];
			pconstcarg[0] = Class.forName("java.lang.String");
			Constructor pconst = pclass.getConstructor(pconstcarg);
			Object[] parg = new Object[1];
			parg[0] = value;
			return pconst.newInstance(parg);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}
		return null;
	}

	// obj: �ΏۃI�u�W�F�N�g�Cfieldname �Ώۃt�B�[���h, o �ύX��̒l
	@SuppressWarnings("rawtypes")
	public static void setter(Object obj, String fieldname, Object o) {
		try {
			Class c = obj.getClass();
			Field f = c.getField(fieldname);
			StringBuffer sb = new StringBuffer();
			sb.append("  | " + fieldname + " : ");
			sb.append(f.get(obj).toString() + " -> ");
			f.set(obj, o);
			sb.append(f.get(obj).toString());
			System.out.println(sb.toString());
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}
	}
}
