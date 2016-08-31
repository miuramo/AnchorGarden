package jaist.css.covis.hist;

import jaist.css.covis.CoVisBuffer;

import java.awt.Color;

public class CVHistory {
	public long tstamp;
	String type;
	String code;
	CoVisBuffer buffer;
	boolean isAlive = true;
	
	public CVHistory(CoVisBuffer b){
		tstamp = System.currentTimeMillis();
		buffer = b;
	}
	public String getCode(){
		return code;
	}
//	public void setCode(String s){
//		setCode(s, true);
//	}
	public void setCode(String s, boolean isShow){
		code = s;
		if (!isShow) return;
		//‘€ì‚ÌŽí—Þ‚ð”»’è
		String operation_type = "";
		Color bgcolor = null;
		String classname = this.getClass().getName();
		if (classname.endsWith("New")){
			if (code.indexOf("=")>1){
				operation_type = "Link to New Object";
				bgcolor = CoVisBuffer.linkcolor;
			} else {
				operation_type = "New Object";
				bgcolor = CoVisBuffer.typefcolor;
			}
		} else if (classname.endsWith("Link")){
			operation_type = "Link";
			bgcolor = CoVisBuffer.linkcolor;
		} else if (classname.endsWith("Unlink")){
			operation_type = "Unlink";
			bgcolor = CoVisBuffer.ulinkcolor;
		} else if (classname.endsWith("ValueArray")){
			operation_type = "Assign Value";			
			bgcolor = CoVisBuffer.varcolor;
		} else if (classname.endsWith("Value")){
			operation_type = "Assign Value";			
			bgcolor = CoVisBuffer.varcolor;
		} else if (classname.endsWith("Var")){
			operation_type = "Declare Variable";			
			bgcolor = CoVisBuffer.varfcolor;
		} else if (classname.endsWith("Method")){
			operation_type = "Method Call";			
			bgcolor = CoVisBuffer.methodcolor;
		}
		
		if (buffer.getWindow()!=null)
		buffer.getWindow().showFadingMessage(s+"   ("+operation_type+")", bgcolor, Color.black, 1.7f, 0.9f);
//		System.out.println(this.getClass().getName());
//		System.out.println("new code: "+s);
	}
	public void setAlive(boolean b) {
		isAlive = b;
	}
	public boolean isAlive(){
		return isAlive;
	}

}
