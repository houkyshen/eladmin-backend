package com.jeff.rest;


import com.jeff.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @ApiOperation("查询用户")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('user:list','admin')")
    public ResponseEntity<Object> queryUser(Pageable pageable) {
        return new ResponseEntity<>(userService.queryAll(pageable), HttpStatus.OK);
    }
}
