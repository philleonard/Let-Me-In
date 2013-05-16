package testcode;
import java.awt.image.BufferedImage;
import java.io.Serializable;
/**
 * 
 * @author James
 *
 * To create an object of this type:
 * ourObject (name) = new ourObject(name, defaultaction, group, img);
 * 
 * To create an array of this type (size must be specified on creation):
 * ourObject[] (name) = new ourObject[size];
 * 
 * To read from object:
 * (name).name
 * (name).defaultaction
 * (name).group
 * (name).img
 * 
 * To read from array:
 * (name)[num].name
 * (name)[num].defaultaction
 * (name)[num].group
 * (name)[num].img
 * 
 * 
 */
public class ourObject implements Serializable{
	public String name;
	public String defaultaction;
	public String group;
	public BufferedImage img;

	public ourObject(String name, String defaultaction, String group, BufferedImage img){
		this.name = name;
		this.defaultaction = defaultaction;
		this.group = group;
		this.img = img;
	}
}
