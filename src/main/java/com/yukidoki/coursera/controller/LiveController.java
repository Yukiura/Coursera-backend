package com.yukidoki.coursera.controller;

import com.yukidoki.coursera.utils.LiveUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/livestream")
public class LiveController {
    @GetMapping("/token")
    @PreAuthorize("hasAuthority('livestream:token')")
    public String token(@RequestParam("cid") Integer cid) {
        LiveUtils liveUtils = new LiveUtils();
        return liveUtils.liveTokenGenerate(cid);
    }
}
