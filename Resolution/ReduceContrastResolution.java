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

package einfBV.serie_01.aufgabe_2;

import de.unihalle.informatik.Alida.exceptions.ALDOperatorException;
import de.unihalle.informatik.Alida.annotations.ALDAOperator;
import de.unihalle.informatik.Alida.annotations.ALDAOperator.Level;
import de.unihalle.informatik.Alida.annotations.Parameter;
import de.unihalle.informatik.MiToBo.core.datatypes.images.*;
import de.unihalle.informatik.MiToBo.core.datatypes.images.MTBImage.MTBImageType;
import de.unihalle.informatik.MiToBo.core.operator.*;

/**
 * Operator reducing the contrast resolution of an image.
 */

// This annotation is necessary for MiToBo to automatically index the operator.
@ALDAOperator(genericExecutionMode=ALDAOperator.ExecutionMode.ALL,
		level=Level.APPLICATION)
public class MiToBoReduceContrastResolution extends MTBOperator {

	/**
	 * 8-bit input image.
	 */
	@Parameter( label= "Input Image", required = true, dataIOOrder = 0,
		direction = Parameter.Direction.IN, description = "Input image.")
	private MTBImageByte inImg = null;

	/**
	 * 4-grays image (2-bit-coding).
	 */
	@Parameter( label= "4-grayscales Image", required = true,
			direction = Parameter.Direction.OUT, description = "4-gray image.")
	private MTBImageByte image2bit = null;

	/**
	 * 16-grays image (4-bit coding).
	 */
	@Parameter( label= "16-grayscales Image", required = true,
			direction = Parameter.Direction.OUT, description = "16-gray image.")
	private MTBImageByte image4bit = null;

	/**
	 * Default constructor, always required even if nothing happens here!
	 *  @throws ALDOperatorException Thrown in case of failure.
	 */
	public MiToBoReduceContrastResolution() throws ALDOperatorException {
		// nothing to do here
	}		

	/**
	 * This method does the actual work and is mandatory.
	 */
	@Override
	protected void operate() {
		int i=0;
		int in=0;
		int inn=0;
		this.image4bit = (MTBImageByte)MTBImage.createMTBImage(
				this.inImg.getSizeX(),	this.inImg.getSizeY(), this.inImg.getSizeZ(), 
				this.inImg.getSizeT(),	this.inImg.getSizeC(), MTBImageType.MTB_BYTE);
		this.image2bit = (MTBImageByte)MTBImage.createMTBImage(
				this.inImg.getSizeX(),	this.inImg.getSizeY(), this.inImg.getSizeZ(), 
				this.inImg.getSizeT(),	this.inImg.getSizeC(), MTBImageType.MTB_BYTE);
		
		
		for (int y=0; y<this.inImg.getSizeY(); y++) {
			for (int x=0; x<this.inImg.getSizeX(); x++) {
				i=this.inImg.getValueInt(x, y);
				in=i/64;
				inn=i/16;
				this.image2bit.putValueInt(x, y, in);
				this.image4bit.putValueInt(x, y, inn);
			}
			
		}
		
	}
}
