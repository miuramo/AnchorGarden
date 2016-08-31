package jaist.css.covis.hist;

import jaist.css.covis.CoVisBuffer;
import jaist.css.covis.cls.Variable;

public class CVHist_Value extends CVHistory {
	Variable var;
	public CVHist_Value(Variable v, CoVisBuffer buf){
		super(buf);
		var = v;
		setCode(var.getEditValueInfo(),true);
	}
}
