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
		//��̬��תҳ��
		return formName;
	}
	
	//�ϴ��ļ����Զ��󶨵�MultipartFile��
	@RequestMapping(value="/upload",method=RequestMethod.POST)
	public String upload(
			HttpServletRequest request,
			@RequestParam("description") String description,
			@RequestParam("file") MultipartFile file
			)throws Exception{
		System.out.println("�ļ�����:"+description);
		//����ļ���Ϊ��,д���ϴ�·��
		if(!file.isEmpty()){
			//�ϴ��ļ�·��
			String path=request.getServletContext().getRealPath("/images");
			System.out.println("-----------�ϴ�·��:"+path);
			//�ϴ��ļ�����
			String fileName=file.getOriginalFilename();
			System.out.println("�ϴ��ļ���:"+fileName);
			File filePath=new File(path,fileName);
			//�ж�·���Ƿ����,��������ھʹ���һ��
			if(!filePath.getParentFile().exists()){
				filePath.getParentFile().mkdirs();
			}
			System.out.println("filePath:"+filePath);
			//���ϴ��ļ����浽һ��Ŀ���ļ���,File.separator��·���ָ����Ч��"\"
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
		System.out.println("�û���:"+user.getUsername());
		//����ļ���Ϊ��,д���ϴ�·��
		if(!user.getImage().isEmpty()){
			//�ϴ��ļ�·��
			String path=request.getServletContext().getRealPath("/images/");
			//�ϴ��ļ�����-
			String fileName=user.getImage().getOriginalFilename();
			//File file=new File(String parent,String child);
			File filePath=new File(path,fileName);
			//�ж�·���Ƿ����,��������ھʹ���һ��
			if(!filePath.getParentFile().exists()){
				filePath.getParentFile().mkdirs();
			}
			//���ϴ��ļ����浽һ��Ŀ���ļ���
			user.getImage().transferTo(new File(path+File.separator+fileName));
			//���û���ӵ�model
			model.addAttribute("user", user);
			//��ת������ҳ��
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
		//�����ļ�·��
		String path=request.getServletContext().getRealPath("/images");
		File file=new File(path+File.separator+filename);
		HttpHeaders headers=new HttpHeaders();
		//������ʾ���ļ���,���������������
		String downloadFileName=new String(filename.getBytes("UTF-8"),"iso-8859-1");
		//֪ͨ�������attachment(���ط�ʽ)��ͼƬ
		headers.setContentDispositionFormData("attachment", downloadFileName);
		//application/octet-stream:������������(������ļ�����)
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		//201  HttpStatus.CREATED
		System.out.println("�ļ�����-----------·��:"+file);
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),headers,HttpStatus.CREATED);
	}
}
