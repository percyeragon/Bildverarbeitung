package einfBV.serie_10.aufgabe_1;
import java.util.*;
 
public class Region {
 
    private final int label;
    private ArrayList<int[]> regionPixels;
 
   
    public Region(int label, int[] pixel)
    {
        this.label = label;
        this.regionPixels = new ArrayList<int[]>();
        this.regionPixels.add(pixel);
    }
   
    public int getLabel()
    {
        return this.label;
    }
   
    public ArrayList<int[]> getPixels()
    {
        return this.regionPixels;
    }
   
    public void addPixel(int[] pixel)
    {
        regionPixels.add(pixel);
    }
   
}