package jaist.css.covis.hist;

import jaist.css.covis.CoVisBuffer;
import jaist.css.covis.cls.Covis_Object;
import edu.umd.cs.piccolo.PNode;

public class CVHist_New extends CVHistory {
	Covis_Object obj;
	boolean isShow;
	boolean consumed = false;

	public CVHist_New(Covis_Object o, CoVisBuffer buf, boolean iS){
		super(buf);
		obj = o; isShow = iS;
		setCode(obj.getConstructorInfo(),iS);
	}
	public boolean isAlive(){
		if (((PNode)obj).getTransparency() < 0.2 && code.startsWith("new")) return false;
		else return true;
	}
	public boolean isConsumed(){
		return consumed;
	}
	public void setConsumed(boolean b){
		consumed = b;
	}
	public Covis_Object getObject(){
		return obj;
	}
}
