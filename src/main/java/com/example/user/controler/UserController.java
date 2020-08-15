package com.example.user.controler;

import com.example.user.dto.AddUser;
import com.example.user.dto.LoginInfo;
import com.example.user.dto.UpdateUser;
import com.example.user.entiy.Accesslog;
import com.example.user.entiy.User;
import com.example.user.service.AccesslogService;
import com.example.user.service.UserService;
import com.example.user.utils.FilesUtils;
import com.example.user.utils.JSONMessageView;
import com.example.user.utils.MD5;
import com.example.user.utils.MemberToken;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * auto zhengshilong
 * Date 2020-08-08
 */
@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AccesslogService accesslogService;
    /**
     * 首页
     * @param request
     * @return
     */
    @RequestMapping(value = "/index")
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("index");
        User user = (User)request.getSession().getAttribute("user");
        if (user!=null){
            modelAndView.addObject("islogin","Y");
            List<Accesslog> accesslogList = accesslogService.getByUserId(user.getId());
            modelAndView.addObject("accesslogList",accesslogList);
        }else{
            modelAndView.addObject("islogin","N");
            modelAndView.addObject("accesslogList",null);
        }
        modelAndView.addObject("user",user);
        return  modelAndView;
    }

    /**
     * 添加用户页面
     * @param request
     * @return
     */
    @RequestMapping(value = "regedit",method = RequestMethod.GET)
    public ModelAndView regedit(HttpServletRequest request){
        return new ModelAndView("regedit");
    }

    /**
     * 添加用户
     * @param request
     * @return
     */
    @RequestMapping(value = "save",method = RequestMethod.POST)
    public JSONMessageView save(HttpServletRequest request, AddUser addUser){
        JSONMessageView jsonMessageView = new JSONMessageView(-1,"添加用户失败！",null);
        String result = userService.addUser(addUser);
        if (result.equals("SUCCESS")){
            jsonMessageView.setCode(0);
            jsonMessageView.setMessage("添加用户成功");
        }else{
            jsonMessageView.setMessage(result);
        }
        return jsonMessageView;
    }

    /**
     * 修改用户页面
     * @param request
     * @return
     */
    @RequestMapping(value = "update")
    public ModelAndView update(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView("updateUser");
        User user = (User)request.getSession().getAttribute("user");
        modelAndView.addObject("user",user);
        return modelAndView;
    }

    /**
     * 修改用户
     * @param request
     * @return
     */
    @RequestMapping(value = "updateSubmit",method = RequestMethod.POST)
    public JSONMessageView updateSubmit(HttpServletRequest request, UpdateUser updateUser){
        JSONMessageView jsonMessageView = new JSONMessageView(-1,"修改用户失败！",null);
        try {
            if (StringUtils.isEmpty(updateUser.getPassWord()) && StringUtils.isEmpty(updateUser.getImageUrl())) {
                jsonMessageView.setCode(0);
                jsonMessageView.setMessage("修改成功");
            }
            if (!StringUtils.isEmpty(updateUser.getPassWord()) && StringUtils.isEmpty(updateUser.getImageUrl())) {
                User user = userService.getById(Integer.parseInt(updateUser.getUserId()));
                user.setPassWord(MD5.MD5Encode(updateUser.getPassWord()));
                userService.update(user);
                jsonMessageView.setCode(0);
                jsonMessageView.setMessage("修改成功");
            }
            if (StringUtils.isEmpty(updateUser.getPassWord()) && !StringUtils.isEmpty(updateUser.getImageUrl())) {
                User user = userService.getById(Integer.parseInt(updateUser.getUserId()));
                Map<String, String> map = FilesUtils.saveImgByBase64(updateUser.getImageUrl(), user.getUserName());
                String url = map.get("code");
                int index = url.indexOf("/static/images");
                String imageurl = url.substring(index);
                user.setImageUrl(imageurl);
                int i = userService.update(user);
                if (i>0){
                    jsonMessageView.setCode(0);
                    jsonMessageView.setMessage("修改成功");
                }

            }
            if (!StringUtils.isEmpty(updateUser.getPassWord()) && !StringUtils.isEmpty(updateUser.getImageUrl())) {
                User user = userService.getById(Integer.parseInt(updateUser.getUserId()));
                user.setPassWord(MD5.MD5Encode(updateUser.getPassWord()));
                Map<String, String> map = FilesUtils.saveImgByBase64(updateUser.getImageUrl(), user.getUserName());
                String url = map.get("code");
                int index = url.indexOf("/static/images");
                String imageurl = url.substring(index);
                user.setImageUrl(imageurl);
                int i = userService.update(user);
                if (i>0){
                    jsonMessageView.setCode(0);
                    jsonMessageView.setMessage("修改成功");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonMessageView;
    }

    /**
     * 登录页面
     * @param request
     * @return
     */
    @RequestMapping(value = "tologin",method = RequestMethod.GET)
    public ModelAndView tologin(HttpServletRequest request){
        return new ModelAndView("tologin");
    }

    /**
     * 图片上传测试
     * @param request
     * @return
     */
    @RequestMapping(value = "uploadImage")
    public ModelAndView uploadImage(HttpServletRequest request){
        return new ModelAndView("uploadImage");
    }
    /**
     * 登录
     * @param request
     * @return
     */
    @RequestMapping(value = "loginsubmit",method = RequestMethod.POST)
    public JSONMessageView loginsubmit(HttpServletRequest request, LoginInfo loginInfo){
        JSONMessageView jsonMessageView = new JSONMessageView(-1,"登录失败！",null);
        if (StringUtils.isEmpty(loginInfo.getUserName())){
            jsonMessageView.setMessage("用户名称不可为空");
            return jsonMessageView;
        }
        if (StringUtils.isEmpty(loginInfo.getPassWord())){
            jsonMessageView.setMessage("密码不可为空");
            return jsonMessageView;
        }
        User user = userService.getByUserName(loginInfo.getUserName());
        if (user==null){
            jsonMessageView.setMessage("不存在该用户");
            return jsonMessageView;
        }
        System.out.println(MD5.MD5Encode(loginInfo.getPassWord()));
        if(!(user.getPassWord().equals(MD5.MD5Encode(loginInfo.getPassWord())))){
            jsonMessageView.setMessage("密码错误");
            return jsonMessageView;
        }
        SecurityUtils.getSubject().login(new MemberToken(user.getUserName(),user.getPassWord()));
        request.getSession().setAttribute("user",user);
        Accesslog accesslog = new Accesslog();
        accesslog.setOperateTime(new Date());
        accesslog.setType("登录");
        String userName = request.getParameter("userName");
        accesslog.setUserName(userName);
        accesslog.setUserId(user.getId());
        accesslogService.save(accesslog);
        jsonMessageView.setCode(0);
        jsonMessageView.setMessage("登录成功");
        return jsonMessageView;
    }

    /**
     * 退出登录
     * @param request
     * @param session
     * @param response
     */
    @RequestMapping(value = "signOut")
    public JSONMessageView signOut(HttpServletRequest request, HttpSession session, HttpServletResponse response){
        JSONMessageView jsonMessageView = new JSONMessageView(-1,"退出登录失败！",null);
        try {
            User user = (User) request.getSession().getAttribute("user");
            Accesslog accesslog = new Accesslog();
            accesslog.setOperateTime(new Date());
            accesslog.setType("退出登录");
            accesslog.setUserName(user.getUserName());
            accesslog.setUserId(user.getId());
            accesslogService.save(accesslog);
            SecurityUtils.getSubject().logout();
            jsonMessageView.setCode(0);
            jsonMessageView.setMessage("登录成功");
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonMessageView;
    }
}
