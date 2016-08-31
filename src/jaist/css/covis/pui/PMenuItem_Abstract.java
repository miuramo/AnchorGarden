package jaist.css.covis.pui;

import jaist.css.covis.fm.PActionListener;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

/**
 * PMenuItem実装の共通部分
 * 
 * @author miuramo
 *
 */
public class PMenuItem_Abstract extends PPath {
	private static final long serialVersionUID = 427251829572460625L;

	public PText ptext;

	public String text;

	PActionListener al = null;

	boolean isToggle;

	public Color defaultcolor;

	public PMenuRoot parent;

	boolean isSelected = false;
	
	Point2D pressP;

	PActionListener capitalShortcutKeyListener;

	public PMenuItem_Abstract(String arg, boolean m, Color c) {
		super(new Rectangle2D.Float(0, 0, 3, 10));
		text = arg;
		isToggle = m;

		ptext = new PText(arg);
		ptext.offset(5, 3);
		setPathToRectangle(0, 0, (float) ptext.getWidth() + 10, (float) ptext
				.getHeight() + 5);
		addChild(ptext);
		setPaint(c);
		setStroke(new BasicStroke(2));
		setStrokePaint(c.darker());
		setTransparency(0.8f);
		defaultcolor = c;
		
		addInputEventListener(new PBasicInputEventHandler() {
			public void mouseEntered(PInputEvent e) {
				setPaint(defaultcolor);
			}

			public void mouseExited(PInputEvent e) {
				setSelected(isSelected);
			}

			public void mousePressed(PInputEvent e) {
				setPaint(Color.red);
				pressP = e.getPositionRelativeTo(PMenuItem_Abstract.this);
			}

			public void mouseReleased(PInputEvent e) {
				setSelected(isSelected);
				if (pressP != null && getBoundsReference().contains(pressP)
						&& getBoundsReference().contains(e.getPositionRelativeTo(PMenuItem_Abstract.this))) doClick(e);
				pressP = null;
			}

//			public void mouseClicked(PInputEvent e) {
//				doClick(e);
//			}
		});
	}

	public String getText() {
		return text;
	}

	public void addPActionListener(PActionListener a) {
		al = a;
	}

	public void doClick(PInputEvent e) {
		System.out.println(text + " is clicked.");
		if (al != null)
			al.actionPerformed(e);
		if (isToggle) {
			isSelected = !isSelected;
			parent.setSelected(PMenuItem_Abstract.this);
		}
	}

	public void setSelected(boolean f) {
		isSelected = f;
		if (f) {
			setStrokePaint(defaultcolor);
			setPaint(defaultcolor);
		} else {
			setStrokePaint(defaultcolor.darker());
			setPaint(defaultcolor.darker());
		}
	}
	public boolean isSelected(){
		return isSelected;
	}
	
	/**
	 * ショートカットキーで，大文字キーを押したときの反応 注意：イベントソースはあてにならない
	 * 
	 * @param a
	 */
	public void addCapitalShortcutKeyListener(PActionListener a) {
		capitalShortcutKeyListener = a;
	}

	public void doCapicalShortcutKeySelected(PInputEvent e) {
		if (capitalShortcutKeyListener != null)
			capitalShortcutKeyListener.actionPerformed(e);
	}
}
