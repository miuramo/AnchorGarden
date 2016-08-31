package jaist.css.covis.hist;

import jaist.css.covis.CoVisBuffer;
import jaist.css.covis.cls.Variable;

public class CVHist_Var extends CVHistory {
	Variable var;
	boolean isShow;
	public CVHist_Var(Variable v, CoVisBuffer buf, boolean iS){
		super(buf);
		var = v;
		isShow = iS;
		setCode(var.getConstructorInfo(), iS);
//		System.out.println("setCode: "+var.getConstructorInfo());
	}
}
