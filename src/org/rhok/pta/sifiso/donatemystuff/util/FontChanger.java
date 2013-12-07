package org.rhok.pta.sifiso.donatemystuff.util;

import android.content.Context;
import android.graphics.Typeface;

public class FontChanger {
	

	
	public static Typeface setRobotoRegular(Context context)
	{
		 return Typeface.createFromAsset(context.getAssets(),"roboto_regular.ttf");
	}
	
	
	public static Typeface setRobotoSlab(Context context)
	{
		 return Typeface.createFromAsset(context.getAssets(),"roboto_slab.ttf");
	}
	
	public static Typeface setRobotoLight(Context context)
	{
		return Typeface.createFromAsset(context.getAssets(),"roboto_light.ttf");
	}
	
	public static Typeface setRobotoCondesed(Context context)
	{
		return Typeface.createFromAsset(context.getAssets(),"roboto_condensed.ttf");
	}

}
