package com.appium.start.utilities;

public enum ElementFactory {

	WELCOME_SKIP_BUTTON("id","skipSetupButton"),
	WELCOME_NEXT_BUTTON("id","nextButton"),
	PASSWORD("id","password"),
	USERNAME("id","msisdn"),
	SHOW_PASSWORD("id","showPassword"),
	CONFIRM_PASSWORD("id","confirm_password"),
	LOGIN("id","register"),
	TUTORIAL_CLOSE("xpath","//div[@class=\"tutorial2\"]/div[@class=\"ok-text\"]"),
	TOOLTIP("id","downloadAppTooltip"),
	PLAY_STORE("xpath","//a[@class=\"ic-bookmark\"]"),
	DASHBOARD_TITLE("class","title"),
	CTA_POPUP_OK("id","android:id/button1"),
	CTA_POPUP_TEXT("id","com.android.chrome:id/js_modal_dialog_message"),
	EXTRA_WELCOME_TEXT_IT("class","extra"),
	NATIVE_DIALLER("id","com.android.contacts:id/callbutton"),
	NATIVE_SMS("id","com.android.mms:id/edit_text_bottom"),
	IMPRESSUM_LINK_TEXT("id","additionalFooterLink");
	
	
	
	private final String by;
	private final String locator;
	
	ElementFactory(String by,String locator){
		
		this.by = by;
		this.locator = locator;
			
	}
	
	public String getLocator(){
		
		return this.by;
	}
	
	public String getValue(){
		
		return this.locator;
	}
}
