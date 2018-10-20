package com.example.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.message.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class LiCodeController {

	private LiCodeSelenuimObject selObject;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public Map<String, String> init() {

		Map<String, String> message = new HashMap<String, String>();
		message.put("status", "{title : XX , code : XX }");
		message.put("post_request ( /send)", "{email : XX , password : XX , url : XX , text : XX , groupsname : [] }");
		message.put("group_valid ( /group_valid)", "{email : XX , password : XX , groupname: XX }");
		message.put("login_valid ( /login_valid)", "{email : XX , password : XX }");

		return message;
	}

	@RequestMapping(value = "/group_valid", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Status> Group_valid(@RequestBody group_valid group_valid) {
		if (!group_valid.getGroupname().equals("")) {

			selObject = new LiCodeSelenuimObject();
			return selObject.isGroupNameValid(group_valid);

		} else {
			return new ResponseEntity<Status>(new Status("group_empty", "404"), HttpStatus.BAD_REQUEST);
		}

	}

	@RequestMapping(value = "/send", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Status> upload(@RequestBody PostRequest postrequest) throws Exception {
		// group + wall / wall

		selObject = new LiCodeSelenuimObject();

		ArrayList<String> FailGroup = new ArrayList<>();
		if (postrequest.getGroupsname().size() > 0) {
			// group + wall

			for (String group_name : postrequest.getGroupsname()) {

				if (selObject.upload_image_to_group_facebook_auto(postrequest, group_name).intValue() == 404) {
					FailGroup.add(group_name);
				}

				selObject = new LiCodeSelenuimObject();

			}
			// finish upload to all groups

			if (FailGroup.size() > 0) {
				String groups = FailGroup.stream().map(Object::toString).collect(Collectors.joining(", "));

				Status state = new Status("Some groups fail : " + groups, "404");
				return new ResponseEntity<Status>(state, HttpStatus.BAD_REQUEST);

			} else {
				Status state = new Status("Upload Completed", "200");
				return new ResponseEntity<Status>(state, HttpStatus.OK);
			}

		} else {
			// only wall

			if (selObject.upload_image_to_wall_auto(postrequest).intValue() == 404) {

				Status state = new Status("fail upload", "404");
				return new ResponseEntity<Status>(state, HttpStatus.BAD_REQUEST);

			} else {
				Status state = new Status("upload to wall complete", "200");
				return new ResponseEntity<Status>(state, HttpStatus.OK);

			}
		}
	}

	@RequestMapping(value = "/login_valid", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Status> Login_valid(@RequestBody Credits credits) {
		selObject = new LiCodeSelenuimObject();

		return selObject.isLoginValid(credits);

	}

}
