package jaist.css.covis;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PDragSequenceEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.util.PBounds;

/**
 * Selectable��͈͑I��
 * @author miuramo
 *
 */
public class CoVisWindow_SelectionRegionHandler extends PDragSequenceEventHandler {
	AnchorGarden viewer;

	PPath selection;

	private Stroke[] strokes = null;

	private float strokeNum = 0;

	final static int NUM_STROKES = 10;

	final static int DASH_WIDTH = 15;

	Point2D cmp; // �h���b�O�J�n���W

	Point2D cp; //�h���b�O���̍��W

	public ArrayList<Selectable> tempSelect;

//	double dist_max_ext_cp; //���̂Ƃ��̋���
	PBounds initialBounds; //�h���b�O�J�n���̎���

	PNode targetPN;
	Point2D targetPNorigin;
	Point2D targetPNtopleft;
	
	PNode selectTarget;
	int dragCount = 0;
	
	public Hashtable<PNode,PNode> trash; // DEL�L�[�őI���폜�������̂��C��U�~���Ă����D���ɍ폜����܂ŁC��i�̂݃A���h�D�\
	public boolean undoExecuted = false;

	public CoVisWindow_SelectionRegionHandler(AnchorGarden _viewer) {
		this.viewer = _viewer;

		float[] dash = { DASH_WIDTH, DASH_WIDTH };
		strokes = new Stroke[NUM_STROKES];
		for (int i = 0; i < NUM_STROKES; i++) {
			strokes[i] = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, dash, i);
		}
		tempSelect = new ArrayList<Selectable>();
		
		trash = new Hashtable<PNode,PNode>();
	}

	public void startDrag(PInputEvent e) {
		super.startDrag(e);

		//�N���b�N�I���g�O���p
		if (e.getPickedNode().getAttribute("selectable")!=null){
			selectTarget = (PNode) e.getPickedNode().getAttribute("selectable");
		}
		dragCount = 0;

		// �͈͑I��p�@�^�[�Q�b�g�x�[�X (not �J�����r���[�x�[�X)
		if (e.getPickedNode() instanceof PNode && (e.getPickedNode().getAttribute("drawTarget")!=null)){
			targetPN = (PNode) e.getPickedNode().getAttribute("drawTarget");
			System.out.println(targetPN.getClass().getName());
			targetPNorigin = (Point2D) e.getPickedNode().getAttribute("drawOffset");
			if (targetPNorigin == null) targetPNorigin = new Point2D.Double();

			if (targetPN != null) {
				Point2D p = e.getPositionRelativeTo(e.getPickedNode());
				if (targetPNorigin != null) p.setLocation(p.getX()+targetPNorigin.getX(),p.getY()+targetPNorigin.getY());
				targetPNtopleft = p;

				//�������C�I��͈͂̐Ԙg�̓J�����ɂ���
				cmp = e.getPositionRelativeTo(viewer.getCanvas().getCamera());
				selection = new PPath();
				selection.setPathToRectangle((float) cmp.getX(), (float) cmp.getY(), 0, 0);
				selection.setStrokePaint(Color.red);//��
				selection.setPaint(null);
				selection.setStroke(strokes[0]);
				viewer.getCanvas().getCamera().addChild(selection);
			}
		} else {
			targetPN = null;
		}
//		tempSelect.clear();
		viewer.setShowToolTip(false);

	}

	public void drag(PInputEvent e) {
		super.drag(e);

		if (targetPN != null){
			// �I��͈͂̍X�V
			cp = e.getPositionRelativeTo(viewer.getCanvas().getCamera());
			Rectangle2D selectionViewRect = new Rectangle2D.Double(cmp.getX(),cmp.getY(),0,0);
			selectionViewRect.add(cp);
			selectionViewRect.add(cmp);
//			float cp_cmp_x = (float)(cp.getX() - cmp.getX());
//			float cp_cmp_y = (float)(cp.getY() - cmp.getY());
//			selection.setPathToRectangle((float) cmp.getX(), (float) cmp.getY(), cp_cmp_x, cp_cmp_y);
			selection.setPathTo(selectionViewRect);

			// �I��Ώە�(Selectable)������
			Point2D p = e.getPositionRelativeTo(e.getPickedNode());
			if (targetPNorigin != null) p.setLocation(p.getX()+targetPNorigin.getX(),p.getY()+targetPNorigin.getY());
			Rectangle2D layerb = new PBounds();
			layerb.add(targetPNtopleft);
			layerb.add(p);

//			�m�[�g���ShortStroke�@�m�[�g�ɍi���ĒT��
//			if (e.getPickedNode().getAttribute("info") != null){
//			String attr = (String)e.getPickedNode().getAttribute("info");
//			if (attr.startsWith("noteCams")){
//			PNode d = (PNode) e.getPickedNode().getAttribute("drawTarget");
			Collection<PNode> col = targetPN.getAllNodes();
			for(Object o:col){
				PNode pn = (PNode)o;
				if (pn instanceof Selectable){
//					if (layerb.contains(pn.getGlobalBounds())){ //���S�Ɋ܂܂��Ƃ��I��
					Rectangle2D pngb = pn.getBounds(); // Label ��offset�œ����Ă���̂ŁCBounds�ł͔�r�ł��Ȃ�
//					System.out.println("original BB" + pngb.toString());
//					System.out.println("original AT" + pn.getTransform().toString());
					pn.getTransform().transform(pngb, pngb);
//					System.out.println("modified BB" + pngb.toString());
					if (layerb.intersects(pngb)){ //�����ł��d�Ȃ�ΑI��
						((Selectable)pn).setSelected(true, tempSelect);
//						if (!tempSelect.contains((Selectable)pn)) tempSelect.add((Selectable)pn);
						pn.repaint();
					}
				}
			}
//			}
//			}
//			}

////			�h���b�O�J�n�����CDaishi���Label�݂̂ɍi���ĒT��
//			if (e.getPickedNode().getAttribute("info") != null){
//			String attr = (String)e.getPickedNode().getAttribute("info");
//			if (attr.startsWith("Daishi")){
//			Daishi d = (Daishi) e.getPickedNode();
//			for(PPath cd: d.childDaishies){
//			Collection col = cd.getAllNodes();
//			for(Object o:col){
//			PNode pn = (PNode)o;
////			if (layerb.contains(pn.getGlobalBounds())){ //���S�Ɋ܂܂��Ƃ��I��
//			if (layerb.intersects(pn.getBounds())){ //�����ł��d�Ȃ�ΑI��
//			if (pn instanceof Selectable){
//			((Selectable)pn).setSelected(true, tempSelect);
////			if (!tempSelect.contains((Selectable)pn)) tempSelect.add((Selectable)pn);
//			pn.repaint();
//			}
//			}
//			}
//			}
//			}
//			}
			ArrayList<Selectable> tempSelect4Check = new ArrayList<Selectable>(tempSelect);
			for(Selectable sl : tempSelect4Check){
				PNode pn = (PNode) sl;
				Rectangle2D pngb = pn.getBounds();
				pn.getTransform().transform(pngb, pngb);
//				if (!layerb.contains(((PNode)sl).getGlobalBounds())){ //���S�Ɋ܂܂��Ƃ��I��
				if (!layerb.intersects(pngb)){ //�����ł��d�Ȃ�ΑI��
					sl.setSelected(false,tempSelect);
				}
			}

			// �E���Ƀh���b�O���Ă���Ƃ��́C���_����`�̓_���p�^�[����ω�������
			float origStrokeNum = strokeNum;
			strokeNum = (strokeNum + 0.3f) % NUM_STROKES; 
			if ((int) strokeNum != (int) origStrokeNum) {
				selection.setStroke(strokes[(int) strokeNum]);
			}
		}
		dragCount++;

//		// �I��͈͂Ɋ��S�Ɋ܂܂��Selectable������ΑI������//�J�����r���[���S��`
//		PAffineTransform at = viewer.getCanvas().getCamera().getViewTransform();
//		Rectangle2D globalb = selection.getGlobalBounds();
//		Rectangle2D layerb = new Rectangle2D.Double();
//		at.inverseTransform(globalb, layerb); //�O���[�o������J������

		// �ȑO�͑S���T���@�������C��������邢
//		for(Student stu: viewer.buffer.id2stuHash.values()){
//		for(PPath d: stu.daishi.childDaishies){
//		Collection col = d.getAllNodes();
//		for(Object o:col){
//		PNode pn = (PNode)o;
////		if (layerb.contains(pn.getGlobalBounds())){ //���S�Ɋ܂܂��Ƃ��I��
//		if (layerb.intersects(pn.getGlobalBounds())){ //�����ł��d�Ȃ�ΑI��
//		if (pn instanceof Selectable){
//		((Selectable)pn).setSelected(true);
//		if (!tempSelect.contains((Selectable)pn)) tempSelect.add((Selectable)pn);
//		pn.repaint();
//		}
//		}
//		}
//		}
//		}
	}

	public void endDrag(PInputEvent e) {
		super.endDrag(e);
		cp = e.getPositionRelativeTo(viewer.getCanvas().getCamera());
		if (selection != null) selection.removeFromParent();
		selection = null;
		
		if (selectTarget != null && dragCount < 3){
			if (selectTarget.getBounds().contains(e.getPositionRelativeTo(selectTarget))){//�������Ƃ������x���̏�Ȃ�
				((Selectable)selectTarget).toggleSelected(tempSelect); //�I�����g�O������
//				System.out.println("Toggle");
			}
			selectTarget = null;
		}

//		tempSelect.clear();
		viewer.setShowToolTip(true);

//		if (cmp.getX() < cp.getX() && cmp.getY() < cp.getY()) {
//		// �J�������W����C�O���[�o���i�p�l���j���W�ւ̕ϊ�
//		PAffineTransform at = viewer.getCanvas().getCamera().getViewTransform();
//		Rectangle2D globalb = selection.getGlobalBounds();
//		Rectangle2D layerb = new Rectangle2D.Double();
//		at.inverseTransform(globalb, layerb);
//		viewer.getCanvas().getCamera().animateViewToCenterBounds(layerb, true, 1000);
//		}
	}

	public void removeSelected() {
		trash.clear();
		for(Selectable sel: tempSelect){
			if (sel.isSelected()) sel.removeLabel(trash);
		}
		tempSelect.clear();
		undoExecuted = false;
		viewer.canvas.cam.repaint();
	}
//	public void undoRemoveSelected(){
//		if (trash.size() == 0){
//			System.out.println("No Undoable Items.");
//			return;
//		}
//		if (!undoExecuted){
//			for(PNode pn: trash.keySet()){
//				PNode parent = trash.get(pn);
//				((Historical)pn).addWithHist(parent);
//				tempSelect.add((Selectable)pn);
//			}
//			undoExecuted = true;
//		} else {
//			for(PNode pn: trash.keySet()){
//				((Historical)pn).removeWithHist();
//			}
//			undoExecuted = false;
//			tempSelect.clear();
//		}
//		viewer.canvas.cam.repaint();
//	}

	public void toFrontSelected() {
		for(Selectable sel : tempSelect){
			PNode pn = (PNode) sel;
			PNode parentnode = pn.getParent();
			parentnode.addChild(pn);
		}
	}

	//�I������
	public void DeSelect() {
		for(Selectable sel : tempSelect){
			sel.setSelected(false, null);
		}
		tempSelect.clear();
	}
}
