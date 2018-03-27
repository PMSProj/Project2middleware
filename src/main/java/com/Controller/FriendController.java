package com.Controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dao.FriendDao;
import com.dao.UserDao;
import com.model.ErrorClazz;
import com.model.Friend;
import com.model.User;

@Controller
public class FriendController {
	@Autowired
	private FriendDao friendDao;
   @Autowired
   private UserDao userDao;
	@RequestMapping(value ="/suggestedusers", method = RequestMethod.GET)
	public ResponseEntity<?> suggestedUsers(HttpSession session) {
		String email = (String) session.getAttribute("loginId");
		if (email == null) {
			ErrorClazz error = new ErrorClazz(5, "Unauthorized access..");
			return new ResponseEntity<ErrorClazz>(error,HttpStatus.UNAUTHORIZED);
		}
		List<User> suggestedUsers = friendDao.suggestedUsers(email);
		return new ResponseEntity<List<User>>(suggestedUsers, HttpStatus.OK);
	}

	@RequestMapping(value ="/addfriend", method = RequestMethod.POST)
	public ResponseEntity<?> addFriend(@RequestBody User toId,HttpSession session) {
		String email = (String) session.getAttribute("loginId");
		if (email == null) {
			ErrorClazz error = new ErrorClazz(5, "Unauthorized access..");
			return new ResponseEntity<ErrorClazz>(error,HttpStatus.UNAUTHORIZED);
		}
		User fromId = userDao.getUser(email);
		Friend friend = new Friend();
		friend.setFromId(fromId);
		friend.setToId(toId);
		friend.setStatus('P');
		friendDao.addFriend(friend);
		return new ResponseEntity<Void> (HttpStatus.OK);
	}
	@RequestMapping(value="/pendingrequests", method = RequestMethod.GET)
	public ResponseEntity<?> pendingRequests(HttpSession session){
		String email=(String) session.getAttribute("loginId");
		if(email==null){
			ErrorClazz error = new ErrorClazz(5, "Unauthorized access..");
			return new ResponseEntity<ErrorClazz>(error,HttpStatus.UNAUTHORIZED);
		}
//		String email="p2@gmail.com" ; 
		List<Friend> pendingRequests=friendDao.pendingRequests(email);
		return new ResponseEntity<List<Friend>>(pendingRequests,HttpStatus.OK);
	}
	@RequestMapping(value ="/acceptrequest", method = RequestMethod.PUT)
	public ResponseEntity<?>acceptRequest(@RequestBody Friend request,HttpSession session){
		String email=(String) session.getAttribute("loginId");
		if(email==null){
			ErrorClazz error = new ErrorClazz(5, "Unauthorized access..");
			return new ResponseEntity<ErrorClazz>(error,HttpStatus.UNAUTHORIZED);
		}
		friendDao.acceptRequest(request);
		return new ResponseEntity<Void>(HttpStatus.OK);
}
	
	@RequestMapping(value ="/deleterequest", method = RequestMethod.PUT)
	public ResponseEntity<?>deleteRequest(@RequestBody Friend request,HttpSession session){
		String email=(String) session.getAttribute("loginId");
		if(email==null){
			ErrorClazz error = new ErrorClazz(5, "Unauthorized access..");
			return new ResponseEntity<ErrorClazz>(error,HttpStatus.UNAUTHORIZED);
		}
		friendDao.deleteRequest(request);
		return new ResponseEntity<Void>(HttpStatus.OK);
}
	@RequestMapping(value ="/friends", method = RequestMethod.GET)
	public ResponseEntity<?> getAllFriends(HttpSession session){
		String email=(String) session.getAttribute("loginId");
		if(email==null){
			ErrorClazz error = new ErrorClazz(5, "Unauthorized access..");
			return new ResponseEntity<ErrorClazz>(error,HttpStatus.UNAUTHORIZED);
		}
		//String email="p2@gmail.com";
		List<Friend> friends=friendDao.listOfFriends(email);
		return new ResponseEntity<List<Friend>>(friends,HttpStatus.OK);
	}
	
}
