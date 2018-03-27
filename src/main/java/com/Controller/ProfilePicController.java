package com.Controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;


import com.dao.ProfilePicDao;
import com.model.ErrorClazz;
import com.model.ProfilePic;




@Controller
public class ProfilePicController {
	@Autowired
	private ProfilePicDao profilePicDao;
	
	
    @RequestMapping(value="/uploadprofilepic" ,method=RequestMethod.POST)
	public ResponseEntity<?> uploadProfilePic(@RequestParam CommonsMultipartFile image, HttpSession session) {
		String email=(String) session.getAttribute("loginId");
       if(email==null){
    	   ErrorClazz error=new ErrorClazz(6, "Unauthorized access");
    	   return new ResponseEntity<ErrorClazz> (error,HttpStatus.UNAUTHORIZED);
       }
       ProfilePic profilePic=new ProfilePic();
       profilePic.setEmail(email);
       profilePic.setImage(image.getBytes());
       profilePicDao.uploadProfilePic(profilePic);
       return new ResponseEntity<ProfilePic>(profilePic ,HttpStatus.OK);		   
	}

    @RequestMapping(value="/getimage/{email:.+}", method=RequestMethod.GET)
    public @ResponseBody byte[] getImage(@PathVariable String email , HttpSession session)
    { 
    	System.out.println(email);
    	String  auth=(String) session.getAttribute("loginId");
    	if(auth==null){
    		return null;
    	}
    	
    	ProfilePic profilePic=profilePicDao.getImage(email);
		if(profilePic==null)
			return null;
		
    	
      		return profilePic.getImage();
    	
    }
}
