package com.jeff.rest;

import com.jeff.service.MenuService;
import com.jeff.service.dto.MenuDto;
import com.jeff.service.mapstruct.MenuMapper;
import com.jeff.utils.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menus")
public class MenuController {
//    @GetMapping(value = "/build")
//    @ApiOperation("获取前端所需菜单")
//    public ResponseEntity<Object> buildMenus() {
//        //这里返回的简单信息只会在控制台输出
//        return new ResponseEntity<>("菜单接口有待完善！", HttpStatus.OK);
//    }

    private final MenuService menuService;
    private final MenuMapper menuMapper;

    @GetMapping(value = "/build")
    @ApiOperation("获取前端所需菜单")
    public ResponseEntity<Object> buildMenus(){
        List<MenuDto> menuDtoList = menuService.findByUser(SecurityUtils.getCurrentUserId());
        List<MenuDto> menuDtos = menuService.buildTree(menuDtoList);
        return new ResponseEntity<>(menuService.buildMenus(menuDtos),HttpStatus.OK);
    }
}
