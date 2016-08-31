package jaist.css.covis.fm;

import java.awt.Color;

import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;

/**
 * 状態
 * 
 * @author miuramo
 *
 */
@SuppressWarnings("serial")
public class State extends PPath {
	protected final double RD = 3.141592 / 180.0;

	protected final int CENTER = 1;

	protected AbstractFlowMenu owner;

	public static int SIZE = 80;

	public State[] nextstates = new State[10];

	public PPath[] itempath = new PPath[18];

	int orig_index = 1, old_index = 1, index = 1, distance;

	String name;

	State prevstate;

	boolean isenable = true;

	//色の設定
	static Color no = new Color(0, 0, 0, 120);

	static Color fill = new Color(153, 153, 255, 220);
	static Color strColor = new Color(0, 0, 0, 255);

	static Color choose = new Color(255, 0, 0, 180);

	PInputEvent currentevent;

//	Category log; // Log4J
	
	public State(AbstractFlowMenu f, String n) {
		owner = f;
		name = n;
		isenable = true;
//		log = Logger.getLogger(State.class.getName());
	}

	public void setMenuSize(int size) {
		SIZE = size;
	}

	public void addNextState(State s, int index) {
		nextstates[index] = s;
	}

	public int getSIZE() {
		return SIZE;
	}

	public String getName() {
		return name;
	};

	public boolean isEnabled() {
		return isenable;
	}

	public void setEnable(boolean b) {
		isenable = b;
	}

	public boolean isEnable(int n) {
		return nextstates[n].isEnabled();
	}

	public void setPrevState(State s) {
		prevstate = s;
	}

	public void setOrigIndex(int i) {
		orig_index = i;
		old_index = i;
	}

	public AbstractFlowMenu getOwner() {
		return owner;
	}

	public void setOwner(FlowMenu_TMRG _owner) {
		owner = _owner;
	}

	public void paint() {
	};

	public String getString(int i) {
		if (nextstates[i] != null) {
			return nextstates[i].getName();
		}
		return null;
	}

	public void drag(PInputEvent e) {
		currentevent = e;
		// Point2D cp = e.getPositionRelativeTo(owner.camera);
		// System.out.println("x: "+cp.getX()+" y: "+cp.getY() +" st "+index);
	}

	public void endDrag(PInputEvent e) {
		currentevent = e;
		// owner.reset();
		// owner.repaint();
	}

	public void updateMenu(int index) {
		// int temp = old_index;
		if (index != 1 && nextstates[index] != null
				&& nextstates[index].isEnabled() && itempath[index] != null)
			itempath[index].setPaint(choose);
		if (index != old_index) {
			// state has changed
			// System.out.println("state changed from "+old_index+" to "+index +
			// " (orig: "+orig_index+")");
			// if (index != 1 && nextstates[index] != null &&
			// nextstates[index].isEnabled()) itempath[index].setPaint(choose);
			if (old_index != 1 && nextstates[old_index] != null
					&& nextstates[old_index].isEnabled())
				itempath[old_index].setPaint(fill);
			if (orig_index == 1) { // 1 to 2-9
				if (nextstates[index] != null && nextstates[index].isEnabled()) {
//					log.debug("[" + nextstates[index].getName()
//							+ "] selected,  i: " + index + " oldi: "
//							+ old_index + " ");
					nextstates[index].setPrevState(this);
					nextstates[index].setOrigIndex(index);
					owner.changeState(nextstates[index]);
				} else {
					if (prevstate != null) {
						// System.out.println("["+getName()+"] deselected, i:
						// "+index+" oldi: "+old_index+" ");
						prevstate.updateMenu(index);
						owner.changeState(prevstate);
					}
				}
			} else { // 2-9 to 1
				if (index == 1) {
					if (nextstates[old_index] != null
							&& nextstates[old_index].isEnabled()) {
//						log.debug("["
//								+ nextstates[old_index].getName()
//								+ "] selected,  i: " + index + " oldi: "
//								+ old_index + " ");
						nextstates[old_index].setPrevState(this);
						nextstates[old_index].setOrigIndex(index);
						owner.changeState(nextstates[old_index]);
						if (nextstates[old_index] instanceof StateAction) {
							StateAction sa = (StateAction) nextstates[old_index];
							sa.action(currentevent);
						}
					} else {
						if (prevstate != null) {
							// System.out.println("["+getName()+"] deselected,
							// i: "+index+" oldi: "+old_index+" ");
							prevstate.updateMenu(index);
							owner.changeState(prevstate);
						}
					}
				}
			}
			old_index = index;
		}
	}

	public int getPlace(int x, int y) {
		int xx, yy, index;
		int rr;
		int mx = owner.getX();
		int my = owner.getY();
		xx = x - mx;
		yy = my - y;
		distance = rr = (int) (Math.sqrt(xx * xx + yy * yy));
		if (rr < SIZE / 2.0 + SIZE / 16.0) { // メニューの中心
			index = 1;
		} else { // メニューの外側
			double r = Math.atan((double) yy / (double) xx) / RD;
			if (xx < 0) {
				r += 180.0;
			}
			if (r < 67.5) {
				r += 360.0;
			}
			index = (int) ((r - 67.5) / 45) + 2;
			// itempath[index].setPaint(choose);
		}
		return index;
	}

}
