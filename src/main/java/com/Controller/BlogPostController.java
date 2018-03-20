package com.Controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dao.BlogPostDao;
import com.dao.UserDao;
import com.model.BlogPost;
import com.model.ErrorClazz;
import com.model.User;

@Controller
public class BlogPostController {
@Autowired
private BlogPostDao blogPostDao;
@Autowired
private UserDao userDao;
@RequestMapping(value="/addblogpost" ,method=RequestMethod.POST)
public ResponseEntity<?> addBlogPost(@RequestBody BlogPost blogPost ,HttpSession session)
{
	System.out.println("Welcome");
	String email=(String) session.getAttribute("loginId");
	if(email==null){
		ErrorClazz error=new ErrorClazz(5,"Unauthorized access...");
		return new ResponseEntity<ErrorClazz>(error,HttpStatus.UNAUTHORIZED);
	}
	blogPost.setPostedOn(new Date());
	User postedBy=userDao.getUser(email);
	blogPost.setPostedBy(postedBy);
	try{
		blogPostDao.addBlogPost(blogPost);
		System.out.println("Hello");
		return new ResponseEntity<BlogPost>(blogPost,HttpStatus.OK);
	}catch(Exception e){
		ErrorClazz error=new ErrorClazz(6,"Unable to post blog.."+e.getMessage());
		return new ResponseEntity<ErrorClazz>(error,HttpStatus.INTERNAL_SERVER_ERROR);
	}
			
}
 
  


}
