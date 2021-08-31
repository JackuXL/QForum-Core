package cn.jackuxl.qforum.controller;

import cn.jackuxl.qforum.model.Board;
import cn.jackuxl.qforum.model.User;
import cn.jackuxl.qforum.service.UserService;
import cn.jackuxl.qforum.serviceimpl.BoardServiceImpl;
import cn.jackuxl.qforum.serviceimpl.UserServiceImpl;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
@CrossOrigin
@RestController
public class BoardController {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    HttpServletResponse response;
    @Autowired
    BoardServiceImpl boardService;

    @RequestMapping(value = "/admin/addBoard", produces = "application/json;charset=UTF-8")
    public String addBoard(String sessionId, Board board) {
        JSONObject result = new JSONObject();
        User user = userService.getUserBySessionId(sessionId);
        if (user != null && sessionId != null && user.isAdmin()) {
            if (boardService.addBoard(board) > 0) {
                result.put("code", 200);
                result.put("msg", "success");
            } else {
                result.put("code", 403);
                result.put("error", "unknown");
            }
        } else {
            result.put("code", 403);
            result.put("error", "no_such_user");
        }
        response.setStatus(result.getInteger("code"));
        return result.toJSONString();
    }

    @RequestMapping(value = "/board/list", produces = "application/json;charset=UTF-8")
    public String listBoards() {
        JSONObject result = new JSONObject();
        result.put("code", 200);
        result.put("msg", "success");
        List<Board> boards = boardService.listBoards();
        result.put("boardList",boards);
        result.put("size",boards.size());
        response.setStatus(result.getInteger("code"));
        return result.toJSONString();
    }
}