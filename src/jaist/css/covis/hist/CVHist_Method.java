package jaist.css.covis.hist;

import jaist.css.covis.CoVisBuffer;

public class CVHist_Method extends CVHistory {
	boolean isShow;
	Object retobj;
	public CVHist_Method(Object v, CoVisBuffer buf, String code, boolean iS){
		super(buf);
		retobj = v;
		isShow = iS;
		setCode(code, iS);
//		System.out.println("setCode: "+var.getConstructorInfo());
	}
}
