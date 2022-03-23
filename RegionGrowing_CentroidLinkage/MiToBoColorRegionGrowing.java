/*
 * This file is part of MiToBo, the Microscope Image Analysis Toolbox.
 *
 * Copyright (C) 2010 - 2014
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Fore more information on MiToBo, visit
 *
 *    http://www.informatik.uni-halle.de/mitobo/
 *
 */
 
package einfBV.serie_10.aufgabe_1;
 
import de.unihalle.informatik.Alida.exceptions.ALDOperatorException;
import de.unihalle.informatik.Alida.exceptions.ALDProcessingDAGException;
import de.unihalle.informatik.MiToBo.core.datatypes.images.MTBImage;
import de.unihalle.informatik.MiToBo.core.datatypes.images.MTBImageDouble;
import de.unihalle.informatik.MiToBo.core.datatypes.images.MTBImageInt;
import de.unihalle.informatik.MiToBo.core.datatypes.images.MTBImageRGB;
import de.unihalle.informatik.MiToBo.core.datatypes.images.MTBImage.MTBImageType;
import de.unihalle.informatik.MiToBo.core.operator.MTBOperator;
import de.unihalle.informatik.Alida.annotations.ALDAOperator;
import de.unihalle.informatik.Alida.annotations.ALDAOperator.Level;
import de.unihalle.informatik.Alida.annotations.Parameter;
import java.util.*;
 
/**
 * Operator for color segmentation by region growing.
 */
 
// This annotation is necessary for MiToBo to automatically index the operator.
@ALDAOperator(genericExecutionMode=ALDAOperator.ExecutionMode.ALL,
        level=Level.APPLICATION)
public class MiToBoColorRegionGrowing extends MTBOperator {
 
    /**
     * RGB input image.
     */
    @Parameter( label= "Input Image", required = true, dataIOOrder = 0,
        direction = Parameter.Direction.IN, description = "Input image.")
    private MTBImageRGB inImg = null;
 
    /**
     * Threshold for linking.
     */
    @Parameter( label= "Linking Threshold", required = true, dataIOOrder = 0,
        direction = Parameter.Direction.IN, description = "Result label image.")
    private int threshold;
 
    /**
     * Label image.
     */
    @Parameter( label= "Result label image", required = true, dataIOOrder = 0,
        direction = Parameter.Direction.OUT, description = "Result label image.")
    private MTBImageInt labelImg = null;
 
    /**
     * Default constructor, always required even if nothing happens here!
     *  @throws ALDOperatorException Thrown in case of failure.
     */
    public MiToBoColorRegionGrowing() throws ALDOperatorException {
        // nothing to do here
    }      
   
    /**
     * Set threshold.
     * @param t Threshold.
     */
    public void setThreshold(int t) {
        this.threshold = t;
    }
 
    /**
     * Set input image.
     * @param img   Image to decode.
     */
    public void setImage(MTBImageRGB img) {
        this.inImg = img;
    }
 
    /**
     * Get result image.
     * @return  Result image.
     */
    public MTBImageInt getLabelImage() {
        return this.labelImg;
    }
   
    //global variable to handle all regions
    ArrayList<Region> allRegions = new ArrayList<Region>();
 
    /**
     * This method does the actual work and is mandatory.
     * @throws ALDOperatorException
     * @throws ALDProcessingDAGException
     */
    @Override
    protected void operate()
            throws ALDOperatorException, ALDProcessingDAGException {
        this.labelImg = (MTBImageInt) MTBImage.createMTBImage(inImg.getSizeX(), inImg.getSizeY(), inImg.getSizeZ(), inImg.getSizeT(), inImg.getSizeC(), MTBImageType.MTB_INT);
        int i = 0;
        for(int x=0; x<inImg.getSizeX(); x++) {
            for(int y=0; y<inImg.getSizeY(); y++){
                int[] pixel = {x,y};
                if(test(pixel)==false){
                    i++;
                    Region region = new Region(i, pixel);
                    this.allRegions.add(region);
                    int[] R = {x+1,y};
                    int[] RU = {x+1,y+1};
                    int[] U = {x,y+1};
                    int[] LU = {x-1,y+1};
                    int[] L = {x-1,y};
                    int[] LO = {x-1,y-1};
                    int[] O = {x,y-1};
                    int[] RO = { x+1,y-1};
                    if(x+1<inImg.getSizeX()) {
                        grow(region,R);
                    }
                    if(x+1<inImg.getSizeX()&& y+1<inImg.getSizeY()) {
                        grow(region,RU);
                    }
                    if(y+1<inImg.getSizeY()){
                        grow(region,U);
                    }
                    if(x-1>=0 && y+1<inImg.getSizeY()) {
                        grow(region,LU);
                    }
                    if(x-1>=0) {
                        grow(region,L);
                    }
                    if(x-1>=0 && y-1>=0) {
                        grow(region,LO);
                    }
                    if(y-1>=0) {
                        grow(region,O);
                    }
                    if(x+1<inImg.getSizeX() && y-1>=0) {
                        grow(region,RO);
                    }
                }
            }
        }
        setLabel();
    }
    void grow(Region region, int[] pixel){
        if(test(pixel)==false && homo(region, pixel)){
            region.addPixel(pixel);
            int x = pixel[0];
            int y = pixel[1];
            int[] R = {x+1,y};
            int[] RU = {x+1,y+1};
            int[] U = {x,y+1};
            int[] LU = {x-1,y+1};
            int[] L = {x-1,y};
            int[] LO = {x-1,y-1};
            int[] O = {x,y-1};
            int[] RO = { x+1,y-1};
            if(x+1<inImg.getSizeX()){
                grow(region, R);
            }
            if((x+1<inImg.getSizeX())&&(y+1<inImg.getSizeY())){
                grow(region, RU);
            }
            if(y+1<inImg.getSizeY()){
                grow(region, U);
            }
            if((x-1>=0)&&(y+1<inImg.getSizeY())){
                grow(region, LU);
            }
            if(x-1>=0){
                grow(region, L);
            }
            if((x-1>=0)&&(y-1>=0)){
                grow(region, LO);
            }
            if(y-1>=0) {
                grow(region, O);
            }
            if((x+1<inImg.getSizeX())&&(y-1>=0)) {
                grow(region, RO);
            }
        }
    }
   
    boolean homo(Region region, int[] pixel){
        int RROT=0;
        int RGRUEN=0;
        int RBLAU=0;
        int ROT = inImg.getValueR(pixel[0], pixel[1]);
        int GRUEN = inImg.getValueG(pixel[0], pixel[1]);
        int BLAU = inImg.getValueB(pixel[0], pixel[1]);
        for(int[] p : region.getPixels()){
            RROT=RROT+inImg.getValueR(p[0], p[1]);
            RGRUEN=RGRUEN+inImg.getValueG(p[0], p[1]);
            RBLAU=RBLAU+inImg.getValueB(p[0], p[1]);
        }
        double dist = Math.sqrt(Math.pow((RROT/region.getPixels().size())-ROT,2)+Math.pow((RGRUEN/region.getPixels().size())-GRUEN,2)+Math.pow((RBLAU/region.getPixels().size())-BLAU,2));
                                
                                
        if(dist <= this.threshold)
        {
            return true;
        }else {
            return false;
        }
    }
    boolean test(int[] pixel){
        boolean res = false;
        for(Region region : allRegions){
            for(int[]p : region.getPixels()){
                if(p[0]==pixel[0] && p[1]==pixel[1]){
                    res = true;
                }
            }
        }
        return res;
    }
    void setLabel() {
        for(int x = 0; x < this.labelImg.getSizeX(); x++){
            for(int y = 0; y < this.labelImg.getSizeY(); y++){
                for(Region region : allRegions) {
                    for(int[]p : region.getPixels()){
                        if(p[0]==x && p[1]==y){
                            this.labelImg.putValueInt(x, y, region.getLabel());
                        }
                    }
                }
            }
        }
    }
}  