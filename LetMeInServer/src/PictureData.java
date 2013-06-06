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
 * This class is used to hold the information about a picture and the picture itself. This makes it
 * easier when sending photo information as it puts all the information together.
 */
public class PictureData implements Serializable{
	public String name = null;
	public String defaultaction = null;
	public String group = null;
	public BufferedImage img = null;

	public PictureData(String name, String defaultaction, String group, BufferedImage img){
		this.name = name;
		this.defaultaction = defaultaction;
		this.group = group;
		this.img = img;
	}
}
