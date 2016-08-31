package jaist.css.covis;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;
/**
 * サイズを別のコンポーネントにあわせて変更するパネル
 * 
 * ViewController内で，PageManagerに表示設定用パネルの大きさをあわせるために使用している．
 * 
 */
public class ViewController_SizeConstraintPanel extends JPanel{
	
	private static final long serialVersionUID = -8408407486972598471L;
	JComponent comp;
	public ViewController_SizeConstraintPanel(JComponent c){
		comp = c;
	}
	public Dimension getPreferredSize(){
		return comp.getPreferredSize();
	}
	
	
}
