package jaist.css.covis.hist;

import jaist.css.covis.CoVisBuffer;
import jaist.css.covis.cls.Covis_Object;
import edu.umd.cs.piccolo.PNode;

public class CVHist_MethodNew extends CVHist_New {
//	Covis_Object obj;
	
	public CVHist_MethodNew(Covis_Object o, CoVisBuffer buf, String src){
		super(o,buf,true);
		obj = o;
		setCode(src,true);
	}
	public boolean isAlive(){
		if (((PNode)obj).getTransparency() < 0.2) return false;
		else return true;
	}
}
