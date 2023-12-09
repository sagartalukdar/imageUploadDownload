package com.imupl.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.imupl.Entity.image;
import com.imupl.Repository.ImageRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class Homecontroller {
 
	@Autowired
	private ImageRepository imr;
	@GetMapping("/")
	public String index(Model m) {
		List<image> iml=imr.findAll();
		m.addAttribute("imglist", iml);
		return "index";
	}
	@PostMapping("upload")
	public String imageupload(@RequestParam MultipartFile Mpimage,HttpSession hs) {
		image img=new image();
		img.setName(Mpimage.getOriginalFilename());
		
		image saveimg= imr.save(img);
		if(saveimg!=null) {
			try {
				File file=new ClassPathResource("static/img").getFile();
				
			Path path=	Paths.get(file.getAbsolutePath()+File.separator+Mpimage.getOriginalFilename());
			Files.copy(Mpimage.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			hs.setAttribute("msg", "saved");
			} catch (Exception e) {
				System.out.print(e);
			}
		}else {
			hs.setAttribute("msg", "notsaved");
		}
		
		return "redirect:/";
	}
}
