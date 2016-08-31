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
		//普通に0でもよいが，最後に追加したものをなるべく使ったほうがよいかと思って．
	}
}
