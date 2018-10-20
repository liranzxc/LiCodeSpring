package com.example.demo;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.http.ResponseEntity;

public interface LiCodeControllerInterface {

	Integer upload_image_to_group_facebook_auto(PostRequest postrequest,String groupname) throws Exception;

	Integer upload_image_to_wall_auto(PostRequest postrequest);

	ResponseEntity<Status> isGroupNameValid(group_valid group_valid);

	ResponseEntity<Status> isLoginValid(Credits credits);


	boolean GoTo(String url);

	boolean Login(String email_text, String password_text);
	

	boolean init();

	boolean clickTabTimes(int i);

	boolean Upload_wall(PostRequest postrequest) throws InterruptedException;
	boolean Upload_group(PostRequest postrequest);

	boolean OpenGroupPage(String groupname);

	boolean quit();

}
