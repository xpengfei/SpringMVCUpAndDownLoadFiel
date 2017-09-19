package com.xing.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.xing.domain.User;

/**
 *@author xpengfei
 *@creat  9:34:01 PM   Sep 17, 2017
 */
@Controller
public class FileUploadController {
	@RequestMapping(value="{formName}")
	public String upload(
			@PathVariable String formName
			){
		//动态跳转页面
		return formName;
	}
	
	//上传文件会自动绑定到MultipartFile中
	@RequestMapping(value="/upload",method=RequestMethod.POST)
	public String upload(
			HttpServletRequest request,
			@RequestParam("description") String description,
			@RequestParam("file") MultipartFile file
			)throws Exception{
		System.out.println("文件描述:"+description);
		//如果文件不为空,写入上传路径
		if(!file.isEmpty()){
			//上传文件路径
			String path=request.getServletContext().getRealPath("/images");
			System.out.println("-----------上传路径:"+path);
			//上传文件名称
			String fileName=file.getOriginalFilename();
			System.out.println("上传文件名:"+fileName);
			File filePath=new File(path,fileName);
			//判断路径是否存在,如果不存在就创建一个
			if(!filePath.getParentFile().exists()){
				filePath.getParentFile().mkdirs();
			}
			System.out.println("filePath:"+filePath);
			//将上传文件保存到一个目标文件中,File.separator是路径分割符等效于"\"
			file.transferTo(new File(path+File.separator+fileName));
			return "success";
		}else{
			return "error";
		}
	}
	
	
	@RequestMapping(value="/register")
	public String register(
				HttpServletRequest request,
				@ModelAttribute User user,
				Model model
			)throws Exception{
		System.out.println("用户名:"+user.getUsername());
		//如果文件不为空,写入上传路径
		if(!user.getImage().isEmpty()){
			//上传文件路径
			String path=request.getServletContext().getRealPath("/images/");
			//上传文件名名-
			String fileName=user.getImage().getOriginalFilename();
			//File file=new File(String parent,String child);
			File filePath=new File(path,fileName);
			//判断路径是否存在,如果不存在就创建一个
			if(!filePath.getParentFile().exists()){
				filePath.getParentFile().mkdirs();
			}
			//将上传文件保存到一个目标文件中
			user.getImage().transferTo(new File(path+File.separator+fileName));
			//将用户添加到model
			model.addAttribute("user", user);
			//跳转到下载页面
			return "userInfo";
		}else{
			return "error";
		}
	}
	@RequestMapping(value="/download")
	public ResponseEntity<byte[]>download(HttpServletRequest request,
			@RequestParam("filename")String filename,
			Model model
			)throws Exception{
		//下载文件路径
		String path=request.getServletContext().getRealPath("/images");
		File file=new File(path+File.separator+filename);
		HttpHeaders headers=new HttpHeaders();
		//下载显示的文件名,解决中文乱码问题
		String downloadFileName=new String(filename.getBytes("UTF-8"),"iso-8859-1");
		//通知浏览器以attachment(下载方式)打开图片
		headers.setContentDispositionFormData("attachment", downloadFileName);
		//application/octet-stream:二进制流数据(最常见的文件下载)
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		//201  HttpStatus.CREATED
		System.out.println("文件下载-----------路径:"+file);
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),headers,HttpStatus.CREATED);
	}
}
