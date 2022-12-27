package com.example.UsersApp.controllers;

import com.example.UsersApp.entity.PageNumbers;
import com.example.UsersApp.entity.User;
import com.example.UsersApp.md5.MD5;
import com.example.UsersApp.repository.UserRepo;
import com.example.UsersApp.service.UploadURLServiceImpl;
import com.example.UsersApp.service.UserService;
import com.example.UsersApp.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@ComponentScan
@Configuration
public class MainController implements WebMvcConfigurer {
    @Autowired
    UserRepo userRepo;
    @Autowired
    UploadURLServiceImpl uploadURLService;
    @Autowired
    UserServiceImpl userServiceImpl;
    @Autowired
    UserService userService;

    MD5 md5 = new MD5();
    private final String UPLOAD_DIR = "src/uploads/";

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.addViewController("/adding").setViewName("forward:/adding.html");
    }

    @GetMapping("/")
    public String viewIndex()
    {
        return "index";
    }

    @GetMapping("/adding")
    public String viewAddingForm()
    {
        return "adding";
    }

    @PostMapping("/added")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes, Model model) {
        // check if file is empty
        if (file.isEmpty()) {
            attributes.addFlashAttribute("message", "Please select a file to upload.");
            return "redirect:/";
        }

        // normalize the file path
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName = "file.xml";

        // save the file on the local file system
        try {
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // return success response
        attributes.addFlashAttribute("message", "You successfully uploaded " + originalFileName + '!');
        uploadURLService.parseSdnFile(UPLOAD_DIR + fileName);
        return "redirect:/adding";
    }

    @GetMapping("/users/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @Param("keyword") String keyword,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model
                                ) {
        int pageSize = 20;
        Page<User> page = userServiceImpl.listByPage(pageNo, pageSize, sortField, sortDir, keyword);
        List< User > userList = page.getContent();
        for(int temp=0; temp<userList.size(); temp++)
        {
            String md5Value = md5.getMd5(userList.get(temp).getName());
            userList.get(temp).setSurname(userList.get(temp).getSurname()+"_"+md5Value);
        }
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("pageNumber", new PageNumbers());
        model.addAttribute("userList", userList);
        return "users";
    }

    @GetMapping("/users")
    public String viewHomePage(Model model) {
        return findPaginated(1, null, "id", "asc", model);
    }

    @PostMapping("/users/page/{page}")
    public String PageFinder(@PathVariable(value = "pageNo") int pageNo, Model model) {
        return findPaginated(pageNo, null, "id", "asc", model);
    }


    @PostMapping("/users/page/")
    public String PageFinderByUserType(PageNumbers pageNumbers, Model model) {
        return findPaginated(pageNumbers.getNumber(), null, "id", "asc", model);
    }

}

