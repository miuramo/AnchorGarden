package jaist.css.covis.util;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * マス目に文字列などのデータを結びつけるクラス
 * 
 * ここでは，特に(int,int) -> String を意識して，putメソッドを作成している
 * 複数行のテキストトークンを扱えるようにカスタマイズ
 */
public class MyBoxMatrixHolder {
	ArrayList<ArrayList<MyBox>> datahash;
	ArrayList<MyBox> lastList;

	public MyBoxMatrixHolder() {
		datahash = new ArrayList<ArrayList<MyBox>>();
		lastList = new ArrayList<MyBox>();
		datahash.add(lastList);
	}
	public MyBoxMatrixHolder(ArrayList<MyBox> boxes, double trialWidth){
		this();
		double t = 0;
		for(MyBox b: boxes){
			if (trialWidth < (t + b.getWidth())){
				addNewLine();
				t = 0;
			}
			lastList.add(b);
			t += b.getWidth();
		}
	}
	public void addNewLine(){
		lastList = new ArrayList<MyBox>();
		datahash.add(lastList);
	}
	public void add(int y, MyBox val) {
		datahash.get(y).add(val);
	}
	public double getLineLength(int y){
		Point2D p = getAllLength(datahash.get(y));
		return p.getX();
	}
	public Point2D getDimension(){
		double w = 0;
		double h = 0;
		for(int i=0;i<datahash.size();i++){
			Point2D tempp = getAllLength(datahash.get(i));
			if (w < tempp.getX()) w = tempp.getX();
			h += tempp.getY();
		}
		return new Point2D.Double(w,h);
	}
	/**
	 * 傾き値(width/height)を返す
	 * @return
	 */
	public double getFitRate(){
		Point2D p = getDimension();
		return (p.getX()/p.getY());
	}

	public ArrayList<MyBox> get(int y) {
		return datahash.get(y);
	}

	public void removeAll() {
		datahash.clear();
	}
	public String toString(){
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<datahash.size();i++){
			for(MyBox b : datahash.get(i)){
				sb.append(b.toString());
			}
			if (i < (datahash.size()-1)) sb.append("\n");
		}
		return sb.toString();
	}
	
	/**トークンリストの長さを合計して返すだけのメソッド
	 本当は，Dimension2Dを使いたいが，クラスがないのでPointで代用*/
	public static Point2D getAllLength(ArrayList<MyBox> tokenlist){
		double length = 0;
		double height = 0;
		for(MyBox szht:tokenlist) {
			length += szht.getWidth();
			if (height < szht.getHeight()) height = szht.getHeight();
		}
		return new Point2D.Double(length,height);
	}
	public static String getPreferredStringLineBreak(String s, double parentrate){
		String src = s.replaceAll("\n", "");
//		InputTokenizer it = new InputTokenizer(src);
		ArrayList<String> tokens = new InputTokenizer(src).tokens;
		ArrayList<MyBox> blist = new ArrayList<MyBox>();
		for(String k:tokens){
			SimpleZenHanToken szht = new SimpleZenHanToken(k);
			blist.add(szht);
		}
		MyBoxMatrixHolder best = getPreferredHolder(blist, parentrate);
		return best.toString();
	}
	
	public static MyBoxMatrixHolder getPreferredHolder(ArrayList<MyBox> blist, double parentrate){
		TreeMap<Double,MyBoxMatrixHolder> resultRates = new TreeMap<Double,MyBoxMatrixHolder>();
		double trialLen = getAllLength(blist).getX();
//		double ratediff = Double.MAX_VALUE;
		for(int i=0;i<9;i++){
			MyBoxMatrixHolder mbmh = new MyBoxMatrixHolder(blist, trialLen);
			double fitrate = mbmh.getFitRate();
			resultRates.put(Math.abs(parentrate - fitrate), mbmh);
//			System.out.println(i+" "+fitrate);
//			if (ratediff < Math.abs(parentrate - fitrate)) break;
			if (parentrate < fitrate){
				trialLen *= 0.75;
			} else {
				trialLen *= 1.3;
			}
//			if (Math.abs(trialLen-lastTrialLen)<2.0d) break;
		}
		for(double d: resultRates.keySet()){
			System.out.println(d+" "+resultRates.get(d).toString());
		}
		ArrayList<MyBoxMatrixHolder> intres = new ArrayList<MyBoxMatrixHolder>(resultRates.values());
		return intres.get(0);
	}
}
