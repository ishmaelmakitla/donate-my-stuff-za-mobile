package org.rhok.pta.sifiso.donatemystuff.util;
/**
 * This is a utility class for Globals.
 * 
 * @author Ishmael Makitla
 *         GDG/RHoK Pretoria
 *         South Africa
 *         2013
 *
 */
public class DonateMyStuffGlobals {
    //operation code for GCM HTTP Server
	public static final int SEND = 1;
	public static final int REGISTER = 0;
	//Google Cloud Console Project ID used as a sender ID for GCM
	public static final String GCM_SENDER_ID = "881964398257";
	//The URL to Donate-My-Stuff Service - for making donation offers
	public static final String MAKE_DONATION_OFFER_SERVLET_URL = "http://za-donate-my-stuff.appspot.com/makedonationoffer";
	//The URL to Donate-My-Stuff Service - for making donation requests
	public static final String MAKE_DONATION_REQUEST_SERVLET_URL = "http://za-donate-my-stuff.appspot.com/makedonationrequest";
	
	public static final String GET_DONATION_OFFER_SERVLET_URL = "http://za-donate-my-stuff.appspot.com/donationrequests";
	//The URL to Donate-My-Stuff Service - for making donation requests
	public static final String GET_DONATION_REQUEST_SERVLET_URL = "http://za-donate-my-stuff.appspot.com/donationoffers";
	
	
	public static final int MODE_REQUESTS_LIST = 0;
	public static final int MODE_OFFERS_LIST = 1;
	
	//Literals for Bundle Keys
	public static final String KEY_MODE = "mode";
	
	public static String FLAG_VIEW_MINE_ONLY = "personal";
}
