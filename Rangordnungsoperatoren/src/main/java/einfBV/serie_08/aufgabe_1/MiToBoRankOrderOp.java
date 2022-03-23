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

package einfBV.serie_08.aufgabe_1;

import de.unihalle.informatik.Alida.exceptions.ALDOperatorException;
import de.unihalle.informatik.Alida.exceptions.ALDProcessingDAGException;
import de.unihalle.informatik.MiToBo.core.datatypes.images.MTBImage;
import de.unihalle.informatik.MiToBo.core.datatypes.images.MTBImageByte;
import de.unihalle.informatik.MiToBo.core.datatypes.images.MTBImage.MTBImageType;
import de.unihalle.informatik.MiToBo.core.operator.MTBOperator;
import de.unihalle.informatik.MiToBo.filters.nonlinear.RankOperator;
import de.unihalle.informatik.MiToBo.filters.nonlinear.RankOperator.RankOpMode;

import java.util.Arrays;

import de.unihalle.informatik.Alida.annotations.ALDAOperator;
import de.unihalle.informatik.Alida.annotations.ALDAOperator.Level;
import de.unihalle.informatik.Alida.annotations.Parameter;

/**
 * Operator for applying rank order operations to an image.
 */

// This annotation is necessary for MiToBo to automatically index the operator.
@ALDAOperator(genericExecutionMode=ALDAOperator.ExecutionMode.ALL,
		level=Level.APPLICATION)
public class MiToBoRankOrderOp extends MTBOperator {

	/**
	 * 8-bit input image.
	 */
	@Parameter( label= "Input Image", required = true, dataIOOrder = 0,
		direction = Parameter.Direction.IN, description = "Input image.")
	private MTBImageByte inImg = null;

	/**
	 * Masksize.
	 */
	@Parameter( label= "Masksize", required = true, dataIOOrder = 1,
		direction = Parameter.Direction.IN, description = "Mask size.")
	private int k = 3;

	/**
	 * Median image.
	 */
	@Parameter( label= "Median Image", required = true, dataIOOrder = 0,
		direction = Parameter.Direction.OUT, description = "Median image.")
	private MTBImageByte medianImg = null;

	/**
	 * Maximum image.
	 */
	@Parameter( label= "Maximum Image", required = true, dataIOOrder = 0,
		direction = Parameter.Direction.OUT, description = "Maximum image.")
	private MTBImageByte maxImg = null;

	/**
	 * Minimum image.
	 */
	@Parameter( label= "Minimum Image", required = true, dataIOOrder = 0,
		direction = Parameter.Direction.OUT, description = "Minimum image.")
	private MTBImageByte minImg = null;

	/**
	 * Contour image.
	 */
	@Parameter( label= "Contour Image", required = true, dataIOOrder = 0,
		direction = Parameter.Direction.OUT, description = "Contour image.")
	private MTBImageByte contourImg = null;

	/**
	 * Default constructor, always required even if nothing happens here!
	 *  @throws ALDOperatorException Thrown in case of failure.
	 */
	public MiToBoRankOrderOp() throws ALDOperatorException {
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
	 * Set mask size.
	 * @param m Mask size.
	 */
	public void setMasksize(int m) {
		this.k = m;
	}

	/**
	 * Get result image.
	 * @return	Result image.
	 */
	public MTBImageByte getMedianImage() {
		return this.medianImg;
	}
	
	/**
	 * Get result image.
	 * @return	Result image.
	 */
	public MTBImageByte getMaxImage() {
		return this.maxImg;
	}

	/**
	 * Get result image.
	 * @return	Result image.
	 */
	public MTBImageByte getMinImage() {
		return this.minImg;
	}

	/**
	 * Get result image.
	 * @return	Result image.
	 */
	public MTBImageByte getContourImage() {
		return this.contourImg;
	}

	/**
	 * This method does the actual work and is mandatory.
	 * @throws ALDOperatorException 
	 * @throws ALDProcessingDAGException 
	 */
	@Override
	protected void operate() 
			throws ALDOperatorException, ALDProcessingDAGException {
		int i=0;
		this.contourImg = (MTBImageByte)MTBImage.createMTBImage(
				this.inImg.getSizeX(),	this.inImg.getSizeY(), this.inImg.getSizeZ(), 
				this.inImg.getSizeT(),	this.inImg.getSizeC(), MTBImageType.MTB_BYTE);
		this.minImg = (MTBImageByte)MTBImage.createMTBImage(
				this.inImg.getSizeX(),	this.inImg.getSizeY(), this.inImg.getSizeZ(), 
				this.inImg.getSizeT(),	this.inImg.getSizeC(), MTBImageType.MTB_BYTE);
		this.maxImg = (MTBImageByte)MTBImage.createMTBImage(
				this.inImg.getSizeX(),	this.inImg.getSizeY(), this.inImg.getSizeZ(), 
				this.inImg.getSizeT(),	this.inImg.getSizeC(), MTBImageType.MTB_BYTE);
		this.medianImg = (MTBImageByte)MTBImage.createMTBImage(
				this.inImg.getSizeX(),	this.inImg.getSizeY(), this.inImg.getSizeZ(), 
				this.inImg.getSizeT(),	this.inImg.getSizeC(), MTBImageType.MTB_BYTE);
		int[] Arr = new int[k*k];
		for (int x=0;x<this.inImg.getSizeX();x++){
			for(int y=0;y<this.inImg.getSizeY();y++){
				for (int a=x-k/2;a<x+k/2+1;a++) {
					for (int b=y-k/2;b<y+k/2+1;b++){
						if ((b>this.inImg.getSizeY())&&(a>this.inImg.getSizeX())){
							Arr[i]=this.inImg.getValueInt(2*this.inImg.getSizeX()-a, 2*this.inImg.getSizeY()-b);
						}
						else {
						if (b>this.inImg.getSizeY()) {
							Arr[i]=this.inImg.getValueInt(Math.abs(a), 2*this.inImg.getSizeY()-b);
						}
						else {
						if (a>this.inImg.getSizeX()) {
							Arr[i]=this.inImg.getValueInt(2*this.inImg.getSizeX()-a,Math.abs(b));
						}
						else {
							Arr[i]=this.inImg.getValueInt(Math.abs(a), Math.abs(b));
						}}}
						i=i+1;
						}
					}
				Arrays.sort(Arr);
				this.minImg.putValueInt(x, y, Arr[0]);
				this.maxImg.putValueInt(x, y, Arr[Arr.length-1]);
				this.medianImg.putValueInt(x, y, Arr[Arr.length/2]);
				this.contourImg.putValueInt(x, y, Arr[Arr.length-1]-Arr[0]);
				for (int n=0;n<Arr.length-1;n++) {
					Arr[n]=0;
				}
				i=0;
			}
		}
	}
	
}

