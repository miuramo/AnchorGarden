package jaist.css.covis;

import java.util.ArrayList;
import java.util.HashSet;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PRoot;

/**
 * ATNBuffer(バッファ)のスーパークラス．画面表示モデルのルート(root), および表示レイヤ(layer)を持つ．
 * 
 * 
 */
public class RootBuffer {
	public static ArrayList<CoVisBuffer> buffers = new ArrayList<CoVisBuffer>();
	
	/**
	 * このBufferを表示中のウィンドウリスト
	 */
	public HashSet<AnchorGarden> showingWindows;
	public PRoot root;
	public PLayer layer;
	public String name;
	public static String bufferName = "World";
	static int bufferid;
	boolean hasModifiedUnsaved;
	public RootBuffer(){
		root = new PRoot();
		layer = new PLayer();
		root.addChild(layer);
		
		name = bufferName+" "+String.valueOf(RootBuffer.bufferid);
		RootBuffer.bufferid++;
		buffers.add((CoVisBuffer)this);
//		PText test = new PText(name);
//		layer.addChild(test);
//		test.offset(-200, -200);
		RootBuffer.updateAllBufferMenu();
	}
	public void addCamera(PCamera cam){
		root.addChild(cam);
		cam.addLayer(layer);
	}
	public void delete(){
		buffers.remove(this);
		RootBuffer.updateAllBufferMenu();
	}
	public static void updateAllBufferMenu(){
		for(RootWindow pw: RootWindow.windows){
			pw.updateBufferMenu();
		}
	}
	public static CoVisBuffer find(String s){
		for(CoVisBuffer b: buffers){
			if (b.name.equals(s)) return b;
		}
		return null;
	}
	
	public void addShowingWindow(AnchorGarden w){
		if (showingWindows == null) showingWindows = new HashSet<AnchorGarden>();
		showingWindows.add(w);
	}
	public void removeShowingWindow(AnchorGarden w){
		if (showingWindows == null) showingWindows = new HashSet<AnchorGarden>();
		showingWindows.remove(w);
//		if(showingWindows.size()==0){ // もし、参照するウィンドウが0になったら
//			((ATNBuffer)this).updateContAction.setSelected(false); //DB連続更新を切る
//		}
//              ここでupdateContActionのプロパティを変更するのではなく、実際の更新箇所でウィンドウ数をチェックする。
		// 　　　ほうが便利（頻繁に繰り返し切り替えることがあるかもしれないので）DBUpdateContinuousThread.runでチェック。
	}
	public void repaint(){
		for(AnchorGarden w:showingWindows){
			w.canvas.repaint();
		}
	}
	public AnchorGarden getWindow(){
		if (showingWindows == null) return null;
		for(AnchorGarden w:showingWindows){
			return w; // どれでもいいから返す
		}
		return null;
	}
}
