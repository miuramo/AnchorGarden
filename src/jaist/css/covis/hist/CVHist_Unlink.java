package jaist.css.covis.hist;

import jaist.css.covis.CoVisBuffer;
import jaist.css.covis.cls.Anchor;
import jaist.css.covis.cls.Variable;

public class CVHist_Unlink extends CVHistory {
	Anchor anchor;
//	Covis_Object obj;
	public CVHist_Unlink(Anchor a, CoVisBuffer buf){
		super(buf);
		anchor = a;
//		obj = o;
//		String[] refs = anchor.srcVariable.getVarName().split("\n");
//		setCode(refs[refs.length-1] +" = null;",true);
		setCode(Variable.getShortestName(anchor.srcVariable.getVarNamesAry()) +" = null;",true);
		//���ʂ�0�ł��悢���C�Ō�ɒǉ��������̂��Ȃ�ׂ��g�����ق����悢���Ǝv���āD
	}
}
