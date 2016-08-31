package jaist.css.covis.hist;

import jaist.css.covis.CoVisBuffer;
import jaist.css.covis.cls.Anchor;
import jaist.css.covis.cls.Covis_Object;
import jaist.css.covis.cls.Variable;

import java.util.ArrayList;

public class CVHist_Link extends CVHistory {
	Anchor anchor;
	Covis_Object obj;
	public CVHist_Link(Anchor a, Covis_Object o, CoVisBuffer buf){
		super(buf);
		anchor = a; // = の左側
		obj = o; // = の右側

		ArrayList<String> srcReferredNames = new ArrayList<String>();
		for(String s: a.srcVariable.getVarNamesAry()){
			srcReferredNames.add(s);
		}
		//ひきざん
		ArrayList<String> destReferredNames = new ArrayList<String>();
		for(String s: a.destObject.referenceVarNames().split("\n")){
			if (!srcReferredNames.contains(s)) destReferredNames.add(s);
		}
		
		code = Variable.getShortestName(srcReferredNames) +" = "+ 
		Variable.getShortestName(destReferredNames) +";";
		setCode(code,true);
		
/*		
		TreeMap<String,Anchor> objRefMap = obj.referenceAnchors();
		ArrayList<String> rightCandidates = new ArrayList<String>();
		ArrayList<String> rightCandidatesNeedCast = new ArrayList<String>();
		for(Anchor ank: objRefMap.values()){
			String[] varNames = ank.getVarName().split("\n");
			for(String vN: varNames){
//				System.out.println(vN);
				if (vN.equals(a.getVarName())){
					continue;
				}
				//右側について，なるべく，オブジェクトをきちんと表現する型での既存参照を探す
				if (a.type == buf.varField.findVar(vN).elementType){
					rightCandidates.add(vN);
				} else {
					rightCandidatesNeedCast.add(vN);
				}
			}
		}
		ArrayList<String> leftCandidates = new ArrayList<String>();
		ArrayList<String> leftCandidatesNeedCast = new ArrayList<String>();
		String[] varNames = a.getVarName().split("\n");
		for(String vN: varNames){
			if (buffer.varField.findVar(vN)==null) continue;//TODO:原因究明
			if (buffer.varField.findVar(vN).elementType == obj.type){
				leftCandidates.add(vN);
			} else {
				leftCandidatesNeedCast.add(vN);
			}
		}
		System.out.println("左の候補");
		for(String s: leftCandidates) System.out.println(s);
		System.out.println("左の候補(キャスト)");
		for(String s: leftCandidatesNeedCast) System.out.println(s);
		System.out.println("右の候補");
		for(String s: rightCandidates) System.out.println(s);
		System.out.println("右の候補(キャスト)");
		for(String s: rightCandidatesNeedCast) System.out.println(s);

		//右側を選ばせるならここで．
		// キャストなし
		String left;
		if (leftCandidates.size()>0) left = leftCandidates.get(0);
		else if (leftCandidatesNeedCast.size()>0) left = leftCandidatesNeedCast.get(0);
		else left = "NOLEFT";

		String right = null;
		if (rightCandidates.size()>0) {
			for(String r: rightCandidates){
				if (r.equals(left)) continue;
				right = r;
			}
		}
		else if (rightCandidatesNeedCast.size()>0){
			for(String r: rightCandidatesNeedCast){
				if (r.equals(left)) continue;
				right = r;
			}
		}
		else right = null;
		
		System.out.println("left "+left+" "+right);
		if (right == null){
			setAlive(false);
			return;
		}
		Class<?> leftVarType;
		Class<?> rightVarType;
		if (obj instanceof Covis_Array){
			leftVarType = buffer.varField.findVar(left).type;
			rightVarType = buffer.varField.findVar(right).type;
		} else {
			if (buffer.varField.findVar(left)==null) return;//TODO:ループ時にNULL：要原因究明
			if (buffer.varField.findVar(right)==null) return;
			leftVarType = buffer.varField.findVar(left).elementType;
			rightVarType = buffer.varField.findVar(right).elementType;
		}
		if (leftVarType == rightVarType){
			code = left +" = "+right+";";
			setCode(code,true);
		} else {
			code = left +" = ("+Covis_Object.getClsNameStatic(leftVarType)+") "+right+";";
			setCode(code,true);
		}
	}
	//キャストが必要な場合もある．
	// 自分で選ばせる？
	 * 
	 */
	}
}
