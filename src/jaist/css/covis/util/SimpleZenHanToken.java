package jaist.css.covis.util;


/**
 * �S�p�Ȃ�2,���p�Ȃ�1�ƒ������J�E���g����P���ȃg�[�N��
 * @author miuramo
 *
 */
public class SimpleZenHanToken implements MyBox {
	int length;
	String orig;
	public SimpleZenHanToken(String s){
		orig = s;
		char[] charary;
		charary = s.toCharArray();
		for(int i=0;i<charary.length;i++){
			Character.UnicodeBlock currentB = null;
			currentB = Character.UnicodeBlock.of(charary[i]);
			if (currentB == Character.UnicodeBlock.BASIC_LATIN){
				length++;
			} else {
				length+=2;
			}
		}
	}
	
	public int getLength(){
		return length;
	}
	public double getWidth(){
		return (double)length;
	}
	public double getHeight(){
		return 2.0d;
	}
	public String toString(){
		return orig;
	}
	public void print(){
		System.out.println(orig+" "+length+" "+getHeight());;
	}
	
}
