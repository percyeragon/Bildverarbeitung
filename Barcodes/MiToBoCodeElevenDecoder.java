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

package einfBV.serie_03.aufgabe_1;

import de.unihalle.informatik.Alida.exceptions.ALDOperatorException;

import de.unihalle.informatik.Alida.annotations.ALDAOperator;
import de.unihalle.informatik.Alida.annotations.ALDAOperator.Level;
import de.unihalle.informatik.Alida.annotations.Parameter;
import de.unihalle.informatik.MiToBo.core.datatypes.images.*;
import de.unihalle.informatik.MiToBo.core.operator.*;

/**
 * Operator generating random barcodes of type Code-11.
 */

// This annotation is necessary for MiToBo to automatically index the operator.
@ALDAOperator(genericExecutionMode=ALDAOperator.ExecutionMode.ALL,
		level=Level.APPLICATION)
public class MiToBoCodeElevenDecoder extends MTBOperator {

	/**
	 * 8-bit input image.
	 */
	@Parameter( label= "Input Image", required = true, dataIOOrder = 0,
		direction = Parameter.Direction.IN, description = "Input image.")
	private MTBImageByte inImg = null;

	/**
	 * Output string with digits.
	 */
	@Parameter( label= "Result String", required = true,
			direction = Parameter.Direction.OUT, description = "Result.")
	private String digitString = null;

	/**
	 * Default constructor, always required even if nothing happens here!
	 *  @throws ALDOperatorException Thrown in case of failure.
	 */
	public MiToBoCodeElevenDecoder() throws ALDOperatorException {
		// nothing to do here
	}		

	/**
	 * Set input image.
	 * @param img	Image to decode.
	 */
	public void setImage(MTBImageByte img) {
		this.inImg = img;
	}
	
	/**
	 * Get result string.
	 * @return	Result string.
	 */
	public String getResult() {
		return this.digitString;
	}
	
	/**
	 * This method does the actual work and is mandatory.
	 */
	@Override
	protected void operate() {
		int x = 0;
		int y=0;
		int i=0;
		String Code ="";
		int sc=0;
		int we =0;
		//Versuch das Rauschen im Bild aufzuheben von Zeile 92 bis 127
		for (x = 0; x < inImg.getSizeX(); x++) {
             we = 0;
             sc = 0;
            for (y = 0; y < 30; y++) {
                if (inImg.getValueInt(x, y) == 255)
                	we=we+1;
                else
                	sc=sc+1;
            }
            if(we>sc) {
            	for( y=0; y < 100; y++)
            		this.inImg.putValueInt(x,y,255);
            }
            else {
            	for( y=0; y < 100; y++) {
            		this.inImg.putValueInt(x, y, 0);
            	}
            }
        }
		//Tatsaechliches Ausrechnen der Zahlen 
		x=0;
		y=0;
		while (x<this.inImg.getSizeX()) {
			if (i==3) {
				x=x+3;
				i=0;
			}
			if ((this.inImg.getValueInt(x,y)==0)&&(this.inImg.getValueInt(x+4,y)==255)) {
				Code=Code+"KW";
				x=x+3;
			}
			if ((this.inImg.getValueInt(x,y)==255)&&(this.inImg.getValueInt(x+4,y)==0)) {
				Code=Code+"KS";
				x=x+3;
				i=i+1;
			}
			if ((this.inImg.getValueInt(x,y)==255)&&(this.inImg.getValueInt(x+4,y)==255)) {
				Code=Code+"LS";
				x=x+6;
				i=i+1;
			}
			if ((this.inImg.getValueInt(x,y)==0)&&(this.inImg.getValueInt(x+4,y)==0)) {
				Code=Code+"LW";
				x=x+6;
			}
		}	
		//Zeichen zu Zahlen Konverter
		i=10;
		while (i<Code.length()) {
			i=i+2;
			if ((Code.substring(i-10, i)).equals("KSKWKSKWLS")){
				digitString=digitString+"0";
				i=i+10;
			}
			if ((Code.substring(i-10, i)).equals("LSKWKSKWLS")){
				digitString=digitString+"1";
				i=i+10;
			}
			if ((Code.substring(i-10, i)).equals("KSLWKSKWLS")){
				digitString=digitString+"2";
				i=i+10;
			}
			if ((Code.substring(i-10, i)).equals("LSLWKSKWKS")){
				digitString=digitString+"3";
				i=i+10;
			}
			if ((Code.substring(i-10, i)).equals("KSKWLSKWLS")){
				digitString=digitString+"4";
				i=i+10;
			}
			if ((Code.substring(i-10, i)).equals("LSKWLSKWKS")){
				digitString=digitString+"5";
				i=i+10;
			}
			if ((Code.substring(i-10, i)).equals("KSLWLSKWKS")){
				digitString=digitString+"6";
				i=i+10;
			}
			if ((Code.substring(i-10, i)).equals("KSKWKSLWLS")){
				digitString=digitString+"7";
				i=i+10;
			}
			if ((Code.substring(i-10, i)).equals("LSKWKSLWKS")){
				digitString=digitString+"8";
				i=i+10;
			}
			if ((Code.substring(i-10, i)).equals("LSKWKSKWKS")){
				digitString=digitString+"9";
				i=i+10;
			}
		}
	}
}
