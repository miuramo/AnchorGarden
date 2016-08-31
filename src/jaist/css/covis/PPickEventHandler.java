package jaist.css.covis;

import java.util.ArrayList;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PDragSequenceEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.util.PDimension;

/**
 * ATNWindow�E�チ�j���[��[pick]
 * @author miuramo
 *
 */
public class PPickEventHandler extends PDragSequenceEventHandler {
	AnchorGarden window;

	long lastRelease; //�_�u���N���b�N����p

	public PPickEventHandler(AnchorGarden _window){
		window = _window;
	}

	PNode targetPN; // �^�[�Q�b�g

//	public void mousePressed(PInputEvent e){
//	super.mousePressed(e);
//	if (e.getPickedNode().getAttribute("info")!=null){
//	System.out.println(e.getPickedNode().getAttribute("info"));
//	}
//	}
	public void startDrag(PInputEvent e){
		super.startDrag(e);
		if (e.getPickedNode() instanceof PNode && (e.getPickedNode().getAttribute("moveTarget")!=null)){
			targetPN = (PNode) e.getPickedNode().getAttribute("moveTarget");
		} else {
			targetPN = null;
		}
	}

	public void drag(PInputEvent e){
		super.drag(e);
		if (targetPN != null){
			PDimension d = e.getDeltaRelativeTo(window.canvas.getLayer());
			targetPN.translate(d.getWidth(), d.getHeight());
			targetPN.moveToFront();

			if (targetPN instanceof Selectable){
				Selectable sel = (Selectable)targetPN;
				if (sel.isSelected()){
					//�I�����ꂽ�ق���Selectable���ꏏ�ɓ������Ă��������D�������C�q���x���͊��ɓ������Ă���̂ŏ���
					ArrayList<Selectable> synchroMoveNodes = new ArrayList<Selectable>();
					synchroMoveNodes.addAll(window.flowmenuEventHandler.selectionHandler.tempSelect);
					synchroMoveNodes.remove(sel);
					for(Selectable s: sel.getAllChildren()){
						synchroMoveNodes.remove(s);
					}
//					for(Selectable s: synchroMoveNodes){
//						((Historical)s).moveWithHist(d.getWidth(), d.getHeight());
//					}
				}
			}
		} else {
			PDimension d = e.getDelta();
			window.canvas.getCamera().translateView(d.getWidth(), d.getHeight());
		}
	}

	public void endDrag(PInputEvent e){
		super.endDrag(e);

//		if (System.currentTimeMillis() - lastRelease < 350){ //�_�u���N���b�N���� dbl
//			if (targetPN != null){
//				PBounds pb = targetPN.getGlobalFullBounds();
//				pb = BUtil.zoomBounds(pb, 1.05);
//				window.canvas.getCamera().animateViewToCenterBounds(pb, true, 500);
//
////				if (targetPN instanceof Student){
////				((Student)targetPN).createNewLabel();
////				}
//			}
//		}
		targetPN = null;
		lastRelease = System.currentTimeMillis();
	}
}
