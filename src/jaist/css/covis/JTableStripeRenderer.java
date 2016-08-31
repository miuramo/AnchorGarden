package jaist.css.covis;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

//http://terai.xrea.jp/Swing/StripeTable.html
public class JTableStripeRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 5385937199280359421L;
	private static final Color ec = new Color(240, 240, 255);
	public JTableStripeRenderer() {
		super();
		setOpaque(true);
		setBorder(BorderFactory.createEmptyBorder(0,5,0,5));
	}
	public Component getTableCellRendererComponent(
			JTable table, Object value,
			boolean isSelected, boolean hasFocus,
			int row, int column) {
		super.getTableCellRendererComponent(table, value, 
				isSelected, hasFocus, row, column);
		if(isSelected) {
			setForeground(table.getSelectionForeground());
			setBackground(table.getSelectionBackground());
		}else{
			setForeground(table.getForeground());
			setBackground((row%2==0)?ec:table.getBackground());
		}
		setHorizontalAlignment((value instanceof Number)?RIGHT:LEFT);
		return this;
	}
}