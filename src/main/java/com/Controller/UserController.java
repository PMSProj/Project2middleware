package com.Controller;

import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dao.UserDao;
import com.model.ErrorClazz;
import com.model.User;

@Controller
public class UserController {
	@Autowired
	private UserDao userDao;
	public UserController()
	{
		System.out.println("UserController bean is created");
	}
	@RequestMapping(value="/registerUser",method=RequestMethod.POST)
	public ResponseEntity<?> registerUser(@RequestBody User user)
	{
		//check for duplicate email
		System.out.println(user.toString());
		String s=user.getEmail();
		if(!userDao.isEmailUnique(s))
		{
			ErrorClazz error=new ErrorClazz(1,"Email already exists..please enter a different email id");
			return new ResponseEntity<ErrorClazz>(error,HttpStatus.CONFLICT);
		}
		try
		{
			userDao.registerUser(user);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			ErrorClazz error=new ErrorClazz(2,"Some Required fields are empty.."+e.getMessage());
			return new ResponseEntity<ErrorClazz>(error,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<User>(user,HttpStatus.OK);
	}
 @RequestMapping(value="/login",method=RequestMethod.POST)
 public ResponseEntity<?> login(@RequestBody User user,HttpSession session)
 {
	 System.out.println(user);
	 User validUser=userDao.login(user);
	 System.out.println(validUser);
	 if(validUser==null)
	 {
		 ErrorClazz error=new ErrorClazz(5,"Login failed..Invalid email/password");
	     return new ResponseEntity<ErrorClazz>(error,HttpStatus.UNAUTHORIZED);
	 }
	 else
	 {
		 validUser.setOnline(true);
		 userDao.update(validUser);
		 session.setAttribute("loginId",user.getEmail());
		 return new ResponseEntity<User>(validUser,HttpStatus.OK);
	 }
	 
	 }
 @RequestMapping(value="/logout",method=RequestMethod.PUT)
 public ResponseEntity<?> logout(HttpSession session){
	 String email=(String) session.getAttribute("loginId");
	 if(email==null){
		 ErrorClazz  error=new ErrorClazz(4,"Please Login...");
		 return new ResponseEntity<ErrorClazz>(error,HttpStatus.UNAUTHORIZED);
	 }
	 User user=userDao.getUser(email);
	 user.setOnline(false);
	 userDao.update(user);
	 session.removeAttribute("loginId");
     session.invalidate();
	 return new ResponseEntity<User>(user,HttpStatus.OK);
 }
 
}
