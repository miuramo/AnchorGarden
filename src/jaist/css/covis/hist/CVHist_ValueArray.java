package jaist.css.covis.hist;

import jaist.css.covis.CoVisBuffer;
import jaist.css.covis.cls.Covis_Array;
import jaist.css.covis.cls.Covis_primitive;

public class CVHist_ValueArray extends CVHistory {
	Covis_primitive v;
	Covis_Array a;
	public CVHist_ValueArray(Covis_primitive _v, Covis_Array _a, CoVisBuffer buf){
		super(buf);
		v = _v;
		a = _a;
		setCode(a.getEditValueInfo(v),true);
	}
}
