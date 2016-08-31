package jaist.css.covis;

import jaist.css.covis.util.FileReadWriter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.StringTokenizer;

/**
 * 設定項目（プロパティ）オブジェクト．コンストラクタで設定ファイル(*.ssf)を読み込み，自分自身の変数のデフォルト値をリフレクションによって変更する．
 * @author miuramo
 *
 */
public class CoVisProperty {
	/**
	 * 生徒パネルの行数（横にいくつ並べるか）
	 */
//	public int column = 7;
	/**
	 * 生徒パネルの列数（縦にいくつ並べるか）
	 */
//	public int row = 6;

	/**
	 * 生徒パネルの幅ピクセル数
	 */
//	public int stsizex = 700;
	/**
	 * 生徒パネルの高さピクセル数
	 */
//	public int stsizey = 990;
	/**
	 * Web公開用生徒パネルの幅ピクセル数
	 */
//	public int webstsizex = 500;
	/**
	 * Web公開用生徒パネルの高さピクセル数
	 */
//	public int webstsizey = 707;
	/**
	 * 並べるときの間隔
	 */
//	public int space = 10;
	/**
	 * ATNWindow表示の幅
	 */
	public int viewsizex = 570;
	/**
	 * ATNWindow表示の高さ
	 */
	public int viewsizey = 560;
	/**
	 * ATNWindow表示の位置（x座標）
	 */
	public int viewlocx = 420;
	/**
	 * ATNWindow表示の位置（y座標）
	 */
	public int viewlocy = 5;
	/**
	 * 再描画の間隔
	 */
//	public int repaintinterval = 1000;

	/**
	 * 筆記縮小率（超音波ペンのときは，A4の横幅解像度が3300で，stsizex=600だった）
	 */
//	public float inkscale = 0.181818f; // 600/3300
	/**
	 * マウス描画の筆記を，DBに保存するときに変換するために使用
	 */
//	public float inkscale700 = 0.212121f; // 700/3300

	/**
	 * 筆記の線幅
	 */
//	public float inkwidth = 1.0f;

	// public float imagewidth = 600.0f;
	// public float imageoffsetx = 0.0f;
	// public float imageoffsety = 10.0f;
	// for control panel
	/**
	 * コントロールパネルの生徒ボタンの幅
	 */
//	public int cpstsizex = 30;
	/**
	 * コントロールパネルの生徒ボタンの高さ
	 */
//	public int cpstsizey = 20;
	/**
	 * コントロールパネルの生徒ボタンの配置間隔
	 */
//	public int cpspace = 2;
	/**
	 * コントロールパネルの生徒ボタンを配置する左上座標
	 */
//	public int cplocx = 0;
	/**
	 * コントロールパネルの生徒ボタンを配置する左上座標
	 */
//	public int cplocy = 0;

	/**
	 * （現在は未使用）いくつ筆記が貯まったら保存するか
	 */
//	public int maxstorestrokes = 1000000;
	/**
	 * （現在は未使用）PDAからのアクセス用，AirTransNote Serverポート番号
	 */
//	public int snail_port = 10007;
	/**
	 * デバッグ用のログを吐き出すかどうか
	 * （現在は，筆記の圧力を標準出力に表示する=true）
	 */
//	public boolean printsendlog = false;

	/**
	 * デバッグ用の接続線を表示するかどうか
	 */
//	public boolean printscanline = false;
	/**
	 * （現在は未使用）生徒の席順配置
	 */
//	public String seatlayout = "";
	/**
	 * 起動時に読み込むPage定義ファイル(*.ser)
	 * : （コロン）でくぎって１～４ページまで設定できる
	 * 例：math27-1.ser:math27-2.ser:math27-3.ser:pat2.ser
	 */
//	public String book_file = "";

	/**
	 * mysql接続ユーザ名
	 */
//	public String mysql_user = "root";
	/**
	 * mysql接続パスワード
	 */
//	public String mysql_pass = "root";
	/**
	 * mysql接続ホスト
	 */
//	public String mysql_host = "localhost";
	/**
	 * mysql接続DB名
	 */
//	public String mysql_db = "tomarigi_rfid";

//	public boolean enquete_mode = false;

//	public boolean dbconnect = true;

	/**
	 * 座席設定ファイル
	 */
//	public String seatmap_file = "";

	/**
	 * ホイール回転ズーム方向の設定(-1 または 1)
	 */
	public int wheelrotationdirection = -1;

	/**
	 * 設定ファイルを読み込んだ，プロパティオブジェクトを返します
	 * @param fname 設定ファイル
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
//	// クラスを取得
//	Class c = Class.forName("jaist.css.atn.ATNProperty");
//	// アクセス可能なフィールドをリフレクトする
//	Field[] field = c.getFields();
//	System.out.println("----- アクセス可能なフィールド -------");
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

	// obj: 対象オブジェクト，fieldname 対象フィールド, o 変更後の値
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
