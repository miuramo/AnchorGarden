package jaist.css.covis;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;
/**
 * �T�C�Y��ʂ̃R���|�[�l���g�ɂ��킹�ĕύX����p�l��
 * 
 * ViewController���ŁCPageManager�ɕ\���ݒ�p�p�l���̑傫�������킹�邽�߂Ɏg�p���Ă���D
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
