package jaist.css.covis.play;

import java.util.ArrayList;

import jaist.css.covis.CoVisBuffer;

public class SrcExecutor {
	CoVisBuffer buf;
	public SrcExecutor(CoVisBuffer b){
		buf = b;
	}
	
	public ArrayList<String> execute(String src){
		ArrayList<String> errors = new ArrayList<String>();
		System.out.println("ORIG:"+ src);
		String[] tokens = src.split("\"");
		for(String t: tokens){
			System.out.println(t);
		}
		
		return errors;
	}
}
