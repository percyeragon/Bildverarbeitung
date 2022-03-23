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

package einfBV.serie_01.aufgabe_1;

import de.unihalle.informatik.Alida.exceptions.ALDOperatorException;
import de.unihalle.informatik.Alida.annotations.ALDAOperator;
import de.unihalle.informatik.Alida.annotations.ALDAOperator.Level;
import de.unihalle.informatik.Alida.annotations.Parameter;
import de.unihalle.informatik.MiToBo.core.datatypes.images.*;
import de.unihalle.informatik.MiToBo.core.datatypes.images.MTBImage.MTBImageType;
import de.unihalle.informatik.MiToBo.core.operator.*;

/**
 * Operator to reduce spatial resolution of an image.
 */

// This annotation is necessary for MiToBo to automatically index the operator.
@ALDAOperator(genericExecutionMode=ALDAOperator.ExecutionMode.ALL,
		level=Level.APPLICATION)
public class MiToBoReduceSpatialResolution extends MTBOperator {

	/**
	 * Input image.
	 */
	@Parameter( label= "Input Image", required = true, dataIOOrder = 0,
		direction = Parameter.Direction.IN, description = "Input image.")
	private MTBImageByte inImg = null;

	/**
	 * N value for reducing resolution.
	 */
	@Parameter( label= "N", required = true, dataIOOrder = 2, 
		direction = Parameter.Direction.IN, description = "N.")
	private int nvalue = 2;

	/**
	 * Scaled image.
	 */
	@Parameter( label= "Scaled Image", required = true,
		direction = Parameter.Direction.OUT, description = "Scaled image.")
	private MTBImageByte scaledImg = null;

	/**
	 * Default constructor, always required even if nothing happens here!
	 *  @throws ALDOperatorException Thrown in case of failure.
	 */
	public MiToBoReduceSpatialResolution() throws ALDOperatorException {
		// nothing to do here
	}		

	/**
	 * This method does the actual work and is mandatory.
	 */
	@Override
	protected void operate() {
		this.scaledImg=(MTBImageByte)MTBImage.createMTBImage((int) Math.ceil((double)this.inImg.getSizeX()/this.nvalue), (int) Math.ceil((double)this.inImg.getSizeY()/this.nvalue), this.inImg.getSizeZ(), this.inImg.getSizeT(), this.inImg.getSizeC(), MTBImageType.MTB_BYTE);
		int x=0;
		int y=0;
		int scx=0;
		int scy=0;
		int ddvalue=0;
		while(y<this.scaledImg.getSizeY()) {
			while(x<this.scaledImg.getSizeX()) {
				    scx=x*this.nvalue;
				    scy=y*this.nvalue;
					ddvalue=this.inImg.getValueInt(scx, scy);
					this.scaledImg.putValueInt(x, y, ddvalue);
					x=x+1;
				}
			x=0;
			y=y+1;
		}
		
	}
		
}
