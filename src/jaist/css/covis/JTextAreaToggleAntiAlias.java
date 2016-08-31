package jaist.css.covis;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTextArea;

public class JTextAreaToggleAntiAlias extends JTextArea implements MouseListener{
    
	private static final long serialVersionUID = -8299619738797635210L;
	private boolean aaOn;
	Font font;
	
//	private HighlightedDocument document = new HighlightedDocument();
	public JTextAreaToggleAntiAlias(){
//		setDocument(document);
		setAntiAlias(true);
//		document.setHighlightStyle(HighlightedDocument.JAVA_STYLE);
		
		addMouseListener(this);
	}
	public JTextAreaToggleAntiAlias(int w, int h){
		this();
	}
	public JTextAreaToggleAntiAlias(String s){
		this();
		setText(s);
	}
	public void setFont(Font f){
		font = f;
		super.setFont(f);
		repaint();
	}
	public void setFontSize(int s){
		Font f = new Font(font.getName(), font.getStyle(), s);
		setFont(f);
	}
	public void setFontSizePlus(int s){
		Font f = new Font(font.getName(), font.getStyle(), font.getSize()+s);
		setFont(f);
	}
    public void setAntiAlias(boolean aaOn)
    {
        this.aaOn = aaOn;
    }
    
    public void paintComponent(Graphics g)
    {
        if (aaOn)
        {
            ((Graphics2D) g).addRenderingHints(new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON));
        }
        else
        {
            ((Graphics2D) g).addRenderingHints(new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF));
        }
        super.paintComponent(g);
    }
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.isControlDown()){
			if (e.getButton()==MouseEvent.BUTTON1){
				setFontSizePlus(-2);
			} else {
				setFontSizePlus(2);
			}
		}
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
};
