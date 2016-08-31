package jaist.css.covis.fm;

import edu.umd.cs.piccolo.event.PInputEvent;

/**
 * ��Ԃ�I�������Ƃ��̃A�N�V����
 * 
 * @author miuramo
 *
 */
public @SuppressWarnings("serial")
class StateAction extends State {
	PActionListener pal;

	public StateAction(AbstractFlowMenu f, String n) {
		super(f, n);
	}

	public void addPActionListener(PActionListener p) {
		pal = p;
	}

	public void action(PInputEvent e) {
		if (pal != null)
			pal.actionPerformed(e);
	}
}
