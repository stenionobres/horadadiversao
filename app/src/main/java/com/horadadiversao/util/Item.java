package com.horadadiversao.util;

import android.graphics.Bitmap;

/**
 * 
 * @author CASSIO SOUZA
 *
 */

public class Item {
	Bitmap image;

	
	public Item(Bitmap image) {
		super();
		this.image = image;

	}
	public Bitmap getImage() {
		return image;
	}
	public void setImage(Bitmap image) {
		this.image = image;
	}

	

}
